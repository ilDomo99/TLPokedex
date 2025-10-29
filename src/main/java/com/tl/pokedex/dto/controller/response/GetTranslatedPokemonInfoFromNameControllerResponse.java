package com.tl.pokedex.dto.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTranslatedPokemonInfoFromNameControllerResponse {
    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;
}
