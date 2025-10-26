package com.tl.pokedex.dto.api.pokeapi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Description {
    private String description;
    private NamedApiResource language;
}
