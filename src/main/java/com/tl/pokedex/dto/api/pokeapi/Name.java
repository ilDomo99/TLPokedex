package com.tl.pokedex.dto.api.pokeapi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {
    private String name;
    private NamedApiResource language;
}
