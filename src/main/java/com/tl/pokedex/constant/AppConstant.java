package com.tl.pokedex.constant;

public class AppConstant {
    private AppConstant() {}

    //PATH VARIABLE
    public static final String POKEMON_NAME_PATH_VARIABLE = "pokemonName";

    //ENDPOINT
    public static final String BASE_ENDPOINT = "/pokemon";

    public static final String GET_POKEMON_INFORMATION_ENDPOINT = "/{" + POKEMON_NAME_PATH_VARIABLE + "}";
    public static final String GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT = "/translated/{" + POKEMON_NAME_PATH_VARIABLE + "}";

    //PLACEHOLDER
    public static final String NOT_AVAILABLE_PLACEHOLDER = "N.A";
}
