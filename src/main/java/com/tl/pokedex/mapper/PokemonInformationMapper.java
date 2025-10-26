package com.tl.pokedex.mapper;

import com.tl.pokedex.constant.LanguageCostant;
import com.tl.pokedex.dto.api.pokeapi.FlavorText;
import com.tl.pokedex.dto.api.pokeapi.NamedApiResource;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.model.PokemonInformation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PokemonInformationMapper {

    public PokemonInformation pokemonInformationFromPokemonSpeciesMapper(PokemonSpecies pokemonSpecies) {
        String name = pokemonSpecies.getName();

        List<FlavorText> flavorTextEntries = pokemonSpecies.getFlavorTextEntries();

        //Retrieve the english description from PokeAPI
        Optional<String> descriptionFromFlavorText = flavorTextEntries.stream()
                .filter(flavorText -> {
                    if(flavorText == null) return false;
                    NamedApiResource language = flavorText.getLanguage();

                    if(language == null) return false;
                    return LanguageCostant.ENGLISH_LANGUAGE.equalsIgnoreCase(language.getName());
                })
                .map(FlavorText::getFlavorText)
                .findFirst();

        String description = descriptionFromFlavorText.orElse("English descriptionFromFlavorText not available");

        String habitat = pokemonSpecies.getHabitat() != null ?
                pokemonSpecies.getHabitat().getName() :
                "Habitat not available";

        Boolean isLegendary = pokemonSpecies.getIsLegendary();

        return new PokemonInformation(name, description, habitat, isLegendary);
    }
}
