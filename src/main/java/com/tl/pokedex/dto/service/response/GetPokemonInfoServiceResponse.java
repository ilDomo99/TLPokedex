package com.tl.pokedex.dto.service.response;

import com.tl.pokedex.dto.model.PokemonInformation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPokemonInfoServiceResponse {
    private PokemonInformation pokemonInformation;
}
