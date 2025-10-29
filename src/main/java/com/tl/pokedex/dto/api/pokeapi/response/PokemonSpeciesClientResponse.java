package com.tl.pokedex.dto.api.pokeapi.response;

import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonSpeciesClientResponse {
    private PokemonSpecies pokemonSpecies;
}
