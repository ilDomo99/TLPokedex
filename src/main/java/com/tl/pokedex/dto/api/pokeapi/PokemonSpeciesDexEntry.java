package com.tl.pokedex.dto.api.pokeapi;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PokemonSpeciesDexEntry {
    private Integer entryNumber;
    private NamedApiResource pokedex;
}
