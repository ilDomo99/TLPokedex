package com.tl.pokedex.dto.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPokemonInfoServiceRequest {
    @NotBlank
    private String pokemonName;
}
