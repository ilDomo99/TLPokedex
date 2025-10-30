package com.tl.pokedex.controller;

import com.tl.pokedex.constant.AppConstant;
import com.tl.pokedex.dto.controller.response.GetPokemonInfoFromNameControllerResponse;
import com.tl.pokedex.dto.controller.response.GetTranslatedPokemonInfoFromNameControllerResponse;
import com.tl.pokedex.dto.error.ErrorMessage;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.dto.service.request.GetPokemonInfoServiceRequest;
import com.tl.pokedex.dto.service.request.GetTranslatedPokemonInformationServiceRequest;
import com.tl.pokedex.dto.service.response.GetPokemonInfoServiceResponse;
import com.tl.pokedex.dto.service.response.GetTranslatedPokemonInformationServiceResponse;
import com.tl.pokedex.service.PokedexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstant.BASE_ENDPOINT)
@Validated
@Tag(name = "Pokedex", description = "Pokedex APIs")
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;
    }

    @Operation(
            summary = "Get Pokemon Information",
            description = "Retrieves basic information about a Pokemon by its name"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved Pokemon information",
            content = @Content(schema = @Schema(implementation = GetPokemonInfoFromNameControllerResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid Pokemon name provided",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
    )
    @GetMapping(AppConstant.GET_POKEMON_INFORMATION_ENDPOINT)
    public ResponseEntity<GetPokemonInfoFromNameControllerResponse> getPokemonInfoFromName(
            @Parameter(description = "Name of the Pokemon", required = true)
            @PathVariable(AppConstant.POKEMON_NAME_PATH_VARIABLE)
            @NotEmpty
            @Size(max = 30)
            String pokemonName) {

        GetPokemonInfoServiceRequest request = new GetPokemonInfoServiceRequest();
        request.setPokemonName(pokemonName);

        GetPokemonInfoServiceResponse serviceResponse = pokedexService.getPokemonInfo(request);
        PokemonInformation pokemonInformation = serviceResponse.getPokemonInformation();

        GetPokemonInfoFromNameControllerResponse response = new GetPokemonInfoFromNameControllerResponse();
        BeanUtils.copyProperties(pokemonInformation, response);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Translated Pokemon Information",
            description = "Retrieves Pokemon information with translated description based on Pokemon's characteristics"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved translated Pokemon information",
            content = @Content(schema = @Schema(implementation = GetTranslatedPokemonInfoFromNameControllerResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid Pokemon name provided",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class))
    )
    @GetMapping(AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT)
    public ResponseEntity<GetTranslatedPokemonInfoFromNameControllerResponse> getTranslatedPokemonInfoFromName(
            @Parameter(description = "Name of the Pokemon", required = true)
            @PathVariable(AppConstant.POKEMON_NAME_PATH_VARIABLE)
            @NotEmpty
            @Size(max = 30)
            String pokemonName) {

        //PokeInformation
        GetPokemonInfoServiceRequest getPokemonInfoServiceRequest = new GetPokemonInfoServiceRequest();
        getPokemonInfoServiceRequest.setPokemonName(pokemonName);

        GetPokemonInfoServiceResponse getPokemonInfoServiceResponse = pokedexService.getPokemonInfo(getPokemonInfoServiceRequest);
        PokemonInformation pokemonInformation = getPokemonInfoServiceResponse.getPokemonInformation();

        //Translate
        GetTranslatedPokemonInformationServiceRequest getTranslatedPokemonInformationServiceRequest = new GetTranslatedPokemonInformationServiceRequest();
        getTranslatedPokemonInformationServiceRequest.setPokemonInformation(pokemonInformation);

        GetTranslatedPokemonInformationServiceResponse getTranslatedPokemonInformationServiceResponse = pokedexService.getTranslatedPokemonInformation(getTranslatedPokemonInformationServiceRequest);
        PokemonInformation translatedPokemonInformation = getTranslatedPokemonInformationServiceResponse.getPokemonInformation();

        GetTranslatedPokemonInfoFromNameControllerResponse response = new GetTranslatedPokemonInfoFromNameControllerResponse();
        BeanUtils.copyProperties(translatedPokemonInformation, response);

        return ResponseEntity.ok(response);
    }
}
