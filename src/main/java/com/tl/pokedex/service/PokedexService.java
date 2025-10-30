package com.tl.pokedex.service;

import com.tl.pokedex.client.FunTranslationClient;
import com.tl.pokedex.client.PokeApiClient;
import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.funtranslation.Contents;
import com.tl.pokedex.dto.api.funtranslation.request.TranslateClientRequest;
import com.tl.pokedex.dto.api.funtranslation.response.TranslateClientResponse;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.api.pokeapi.request.PokemonSpeciesClientRequest;
import com.tl.pokedex.dto.api.pokeapi.response.PokemonSpeciesClientResponse;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.dto.service.request.GetPokemonInfoServiceRequest;
import com.tl.pokedex.dto.service.request.GetTranslatedPokemonInformationServiceRequest;
import com.tl.pokedex.dto.service.response.GetPokemonInfoServiceResponse;
import com.tl.pokedex.dto.service.response.GetTranslatedPokemonInformationServiceResponse;
import com.tl.pokedex.mapper.PokemonInformationMapper;
import com.tl.pokedex.util.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PokedexService {

    private final PokeApiClient pokeApiClient;
    private final PokemonInformationMapper pokemonInformationMapper;
    private final FunTranslationClient funTranslationClient;
    private final ValidationUtil validationUtil;

    public PokedexService(PokeApiClient pokeApiClient, PokemonInformationMapper pokemonInformationMapper, FunTranslationClient funTranslationClient, ValidationUtil validationUtil) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonInformationMapper = pokemonInformationMapper;
        this.funTranslationClient = funTranslationClient;
        this.validationUtil = validationUtil;
    }


    /**
     * Retrieves detailed information about a specific Pokemon based on its name.
     * This method validates the input request, interacts with external APIs to
     * fetch Pokemon species details, maps the retrieved data into a structured
     * format, and returns the result.
     *
     * @param request the request object containing the name of the Pokemon to fetch information for
     * @return a {@link GetPokemonInfoServiceResponse GetPokemonInfoServiceResponse} object encapsulating the retrieved Pokemon information
     * @throws com.tl.pokedex.exception.PokedexGenericException if the request or response validation fails
     */
    public GetPokemonInfoServiceResponse getPokemonInfo(GetPokemonInfoServiceRequest request){
        validationUtil.isValidOrFail(GetPokemonInfoServiceRequest.class, request);

        String pokemonName = request.getPokemonName();

        PokemonSpeciesClientRequest pokemonSpeciesClientRequest = new PokemonSpeciesClientRequest();
        pokemonSpeciesClientRequest.setPokemonName(pokemonName);

        PokemonSpeciesClientResponse pokemonSpeciesClientResponse = pokeApiClient.getPokemonSpecies(pokemonSpeciesClientRequest);

        validationUtil.isValidOrFail(PokemonSpeciesClientResponse.class, pokemonSpeciesClientResponse);

        PokemonSpecies pokemonSpecies = pokemonSpeciesClientResponse.getPokemonSpecies();
        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        GetPokemonInfoServiceResponse response = new GetPokemonInfoServiceResponse();
        response.setPokemonInformation(pokemonInformation);

        return response;
    }

    /**
     * Retrieves translated Pokemon information, including a description that is modified based on
     * the Pokemon's characteristics such as being legendary or its habitat.
     * The method validates the input request, translates the Pokemon's description using external
     * translation APIs, and returns the updated Pokemon information with the translated description.
     *
     * @param request the input request object containing the original Pokemon information that needs
     *                translation
     * @return a {@link GetTranslatedPokemonInformationServiceResponse} object containing the updated
     *         Pokemon information with the translated description
     * @throws com.tl.pokedex.exception.PokedexGenericException if the input request or Pokemon information within the request
     *                                  is invalid
     */
    public GetTranslatedPokemonInformationServiceResponse getTranslatedPokemonInformation(GetTranslatedPokemonInformationServiceRequest request){
        PokemonInformation pokemonInformation = request.getPokemonInformation();
        validationUtil.isValidOrFail(PokemonInformation.class, pokemonInformation);

        String originalDescription = pokemonInformation.getDescription();

        boolean isDescriptionNotValid = StringUtils.isBlank(originalDescription);

        if(isDescriptionNotValid){
            GetTranslatedPokemonInformationServiceResponse response = new GetTranslatedPokemonInformationServiceResponse();
            response.setPokemonInformation(pokemonInformation);

            return response;
        }

        boolean isLegendary = Boolean.TRUE.equals(pokemonInformation.getIsLegendary());
        boolean isHabitatCave = PokeApiConstant.CAVE_HABITAT.equalsIgnoreCase(pokemonInformation.getHabitat());

        boolean useYodaTranslation = isLegendary || isHabitatCave;

        TranslateClientRequest translateClientRequest = new TranslateClientRequest();
        translateClientRequest.setText(originalDescription);

        Optional<TranslateClientResponse> translateResponse = useYodaTranslation ?
                funTranslationClient.getYodaTranslation(translateClientRequest) :
                funTranslationClient.getShakespeareTranslation(translateClientRequest);

        String description = translateResponse.isPresent() ?
                getTranslatedDescriptionOrOriginalDescription(translateResponse.get(), originalDescription) :
                originalDescription;

        pokemonInformation.setDescription(description);

        GetTranslatedPokemonInformationServiceResponse response = new GetTranslatedPokemonInformationServiceResponse();
        response.setPokemonInformation(pokemonInformation);

        return response;
    }


    /**
     * @param translateClientResponse the response object containing translation details
     * @param originalDescription the original description of the Pokemon
     * @return the translated description if available; otherwise, the original description
     */
    private String getTranslatedDescriptionOrOriginalDescription(TranslateClientResponse translateClientResponse, String originalDescription){
        Contents contents = translateClientResponse.getContents();

        if(contents == null) return originalDescription;

        String translated = contents.getTranslated();

        return translated != null ? translated : originalDescription;
    }
}
