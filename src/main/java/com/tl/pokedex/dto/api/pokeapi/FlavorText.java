package com.tl.pokedex.dto.api.pokeapi;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FlavorText {
    private String flavorText;
    private NamedApiResource language;
    private NamedApiResource version;
}
