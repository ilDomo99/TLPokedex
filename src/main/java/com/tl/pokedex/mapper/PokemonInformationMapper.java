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
