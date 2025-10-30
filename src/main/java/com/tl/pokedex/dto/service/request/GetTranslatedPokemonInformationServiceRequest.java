package com.tl.pokedex.dto.service.request;

import com.tl.pokedex.dto.model.PokemonInformation;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTranslatedPokemonInformationServiceRequest {
    @NotNull
    private PokemonInformation pokemonInformation;
}
