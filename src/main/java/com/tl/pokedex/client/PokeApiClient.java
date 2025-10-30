package com.tl.pokedex.client;

import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.api.pokeapi.request.PokemonSpeciesClientRequest;
import com.tl.pokedex.dto.api.pokeapi.response.PokemonSpeciesClientResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokeApiClient {
    private final RestTemplate restTemplate;

    public PokeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches detailed information about a Pokemon species using the PokeAPI endpoint {@value PokeApiConstant#POKEMON_SPECIES_ENDPOINT}
     *
     * @param request the request object containing the name of the Pokemon for which species details are to be fetched
     * @return a {@link PokemonSpeciesClientResponse} object encapsulating the retrieved {@link PokemonSpecies} information
     */
    public PokemonSpeciesClientResponse getPokemonSpecies(PokemonSpeciesClientRequest request){
        String pokemonName = request.getPokemonName();

        PokemonSpecies pokemonSpecies = restTemplate.getForObject(PokeApiConstant.POKEMON_SPECIES_ENDPOINT, PokemonSpecies.class, pokemonName);

        PokemonSpeciesClientResponse response = new PokemonSpeciesClientResponse();
        response.setPokemonSpecies(pokemonSpecies);

        return response;
    }


}
