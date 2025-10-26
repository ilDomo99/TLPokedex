package com.tl.pokedex.controller;

import com.tl.pokedex.constant.ApiConstant;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.service.PokedexService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.BASE_ENDPOINT)
@Validated
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    @GetMapping(ApiConstant.GET_POKEMON_INFORMATION_ENDPOINT)
    public ResponseEntity<PokemonInformation> getPokemonInfoFromName(
            @PathVariable("name")
            @NotEmpty
            @Size(max = 30)
            String name){

        PokemonInformation pokemonInformation = pokedexService.getPokemonInfoFromName(name);

        return ResponseEntity.ok(pokemonInformation);
    }

    @GetMapping(ApiConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT)
    public ResponseEntity<PokemonInformation> getTranslatedPokemonInfoFromName(
            @PathVariable("name")
            @NotEmpty
            @Size(max = 30)
            String name){

        PokemonInformation pokemonInformation = pokedexService.getTranslatedPokemonInformation(name);

        return ResponseEntity.ok(pokemonInformation);
    }
}
