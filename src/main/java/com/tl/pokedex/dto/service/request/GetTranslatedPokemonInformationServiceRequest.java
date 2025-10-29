package com.tl.pokedex.dto.service.request;

import com.tl.pokedex.dto.model.PokemonInformation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTranslatedPokemonInformationServiceRequest {
    private PokemonInformation pokemonInformation;
}
