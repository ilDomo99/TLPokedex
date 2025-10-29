package com.tl.pokedex.dto.api.pokeapi.response;

import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PokemonSpeciesClientResponse {
    @NonNull
    private PokemonSpecies pokemonSpecies;
}
