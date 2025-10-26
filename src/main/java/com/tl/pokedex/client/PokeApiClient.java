package com.tl.pokedex.client;

import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiClient {
    private final RestTemplate restTemplate;

    public PokeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PokemonSpecies getPokemonSpeciesFromName(String name){
        return restTemplate.getForObject(PokeApiConstant.POKEMON_SPECIES_ENDPOINT, PokemonSpecies.class, name);
    }


}
