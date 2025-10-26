package com.tl.pokedex.constant;

public class ApiConstant {
    private ApiConstant() {}

    public static final String BASE_ENDPOINT = "/pokemon";

    public static final String GET_POKEMON_INFORMATION_ENDPOINT = "/{name}";
    public static final String GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT = "/translated/{name}";
}
