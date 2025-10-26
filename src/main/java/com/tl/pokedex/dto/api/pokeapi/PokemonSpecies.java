package com.tl.pokedex.dto.api.pokeapi;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PokemonSpecies {
    private Integer id;
    private String name;
    private Integer order;
    private Integer genderRate;
    private Integer captureRate;
    private Integer baseHappiness;
    private Boolean isBaby;
    private Boolean isLegendary;
    private Boolean isMythical;
    private Integer hatchCounter;
    private Boolean hasGenderDifferences;
    private Boolean formsSwitchable;
    private NamedApiResource growthRate;
    private List<PokemonSpeciesDexEntry> pokedexNumbers;
    private List<NamedApiResource> eggGroups;
    private NamedApiResource color;
    private NamedApiResource shape;
    private NamedApiResource evolvesFromSpecies;
    private NamedApiResource evolutionChain;
    private NamedApiResource habitat;
    private NamedApiResource generation;
    private List<Name> names;
    private List<FlavorText> flavorTextEntries;
    private List<Description> formDescriptions;
    private List<Genus> genera;
    private List<PokemonSpeciesVariety> varieties;
}
