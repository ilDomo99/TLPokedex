package com.tl.pokedex.service;

import com.tl.pokedex.client.FunTranslationClient;
import com.tl.pokedex.client.PokeApiClient;
import com.tl.pokedex.constant.HabitatConstant;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.mapper.PokemonInformationMapper;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class PokedexService {

    private final PokeApiClient pokeApiClient;
    private final PokemonInformationMapper pokemonInformationMapper;
    private final FunTranslationClient funTranslationClient;

    public PokedexService(PokeApiClient pokeApiClient, PokemonInformationMapper pokemonInformationMapper, FunTranslationClient funTranslationClient) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonInformationMapper = pokemonInformationMapper;
        this.funTranslationClient = funTranslationClient;
    }

    public PokemonInformation getPokemonInfoFromName(@NotBlank String name){
        PokemonSpecies pokemonSpecies = pokeApiClient.getPokemonSpeciesFromName(name);

        //TODO handle error properly
        if(pokemonSpecies == null){
            throw new RuntimeException();
        }

        return pokemonInformationMapper.pokemonInformationFromPokemonSpeciesMapper(pokemonSpecies);
    }

    public PokemonInformation getTranslatedPokemonInformation(@NotBlank String name){
        PokemonInformation pokemonInformation = getPokemonInfoFromName(name);

        Boolean isLegendary = pokemonInformation.getIsLegendary();

        boolean isHabitatCave =
                pokemonInformation.getHabitat() != null &&
                HabitatConstant.CAVE_HABITAT.equalsIgnoreCase(pokemonInformation.getHabitat());

        boolean useYodaTranslation = isLegendary || isHabitatCave;

        String description = pokemonInformation.getDescription();

        String translatedDescription = useYodaTranslation ?
                funTranslationClient.getYodaTranslation(description) :
                funTranslationClient.getShakespeareTranslation(description);

        pokemonInformation.setDescription(translatedDescription);

        return pokemonInformation;
    }
}
