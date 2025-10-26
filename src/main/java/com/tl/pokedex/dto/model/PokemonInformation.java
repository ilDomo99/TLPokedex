package com.tl.pokedex.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PokemonInformation {
    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;
}
