package com.tl.pokedex.mapper;

import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.pokeapi.FlavorText;
import com.tl.pokedex.dto.api.pokeapi.NamedApiResource;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.model.PokemonInformation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PokemonInformationMapper {

    /**
     * Maps a {@link PokemonSpecies} object to a {@link PokemonInformation} object, extracting relevant
     * information such as name, description, habitat, and legendary status.
     * This method processes the data retrieved from a Pokemon API, filters for English language
     * descriptions, and provides a structured representation of the Pokemon's information.
     *
     * @param pokemonSpecies the {@link PokemonSpecies} object containing detailed data of a Pokemon,
     *                       including its name, description, habitat, and legendary status
     * @return a {@link PokemonInformation} object containing the name, English description, habitat,
     *         and legendary status of the Pokemon
     */
    public PokemonInformation pokemonInformationFromPokemonSpecies(PokemonSpecies pokemonSpecies) {
        String name = pokemonSpecies.getName();

        List<FlavorText> flavorTextEntries = pokemonSpecies.getFlavorTextEntries();

        if(flavorTextEntries == null){
            flavorTextEntries = new ArrayList<>();
        }

        //Retrieve the english description from PokeAPI
        Optional<String> descriptionFromFlavorText = flavorTextEntries.stream()
                .filter(flavorText -> {
                    if(flavorText == null) return false;

                    String description = flavorText.getFlavorText();
                    if(description == null) return false;

                    NamedApiResource language = flavorText.getLanguage();
                    if(language == null) return false;

                    return PokeApiConstant.ENGLISH_LANGUAGE.equalsIgnoreCase(language.getName());
                })
                .map(FlavorText::getFlavorText)
                .findFirst();

        String description = descriptionFromFlavorText.orElse(StringUtils.EMPTY);

        String habitat = pokemonSpecies.getHabitat() != null ?
                pokemonSpecies.getHabitat().getName() :
                StringUtils.EMPTY;

        Boolean isLegendary = pokemonSpecies.getIsLegendary();

        return new PokemonInformation(name, description, habitat, isLegendary);
    }
}
