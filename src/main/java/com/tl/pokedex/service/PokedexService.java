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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PokedexService {

    private final PokeApiClient pokeApiClient;
    private final PokemonInformationMapper pokemonInformationMapper;
    private final FunTranslationClient funTranslationClient;

    public PokedexService(PokeApiClient pokeApiClient, PokemonInformationMapper pokemonInformationMapper, FunTranslationClient funTranslationClient) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonInformationMapper = pokemonInformationMapper;
        this.funTranslationClient = funTranslationClient;
    }

    public GetPokemonInfoServiceResponse getPokemonInfo(GetPokemonInfoServiceRequest request){
        String pokemonName = request.getPokemonName();

        PokemonSpeciesClientRequest pokemonSpeciesClientRequest = new PokemonSpeciesClientRequest();
        pokemonSpeciesClientRequest.setPokemonName(pokemonName);

        PokemonSpeciesClientResponse pokemonSpeciesClientResponse = pokeApiClient.getPokemonSpecies(pokemonSpeciesClientRequest);
        PokemonSpecies pokemonSpecies = pokemonSpeciesClientResponse.getPokemonSpecies();

        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        GetPokemonInfoServiceResponse response = new GetPokemonInfoServiceResponse();
        response.setPokemonInformation(pokemonInformation);

        return response;
    }

    public GetTranslatedPokemonInformationServiceResponse getTranslatedPokemonInformation(GetTranslatedPokemonInformationServiceRequest request){
        PokemonInformation pokemonInformation = request.getPokemonInformation();

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

    private String getTranslatedDescriptionOrOriginalDescription(TranslateClientResponse translateClientResponse, String originalDescription){
        if(translateClientResponse == null) return originalDescription;

        Contents contents = translateClientResponse.getContents();

        if(contents == null) return originalDescription;

        String translated = contents.getTranslated();

        return translated != null ? translated : originalDescription;
    }
}
