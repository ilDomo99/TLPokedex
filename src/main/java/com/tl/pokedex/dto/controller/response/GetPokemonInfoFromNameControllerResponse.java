package com.tl.pokedex.dto.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPokemonInfoFromNameControllerResponse {
    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;
}
