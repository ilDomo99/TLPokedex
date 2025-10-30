package service;

import com.tl.pokedex.client.FunTranslationClient;
import com.tl.pokedex.client.PokeApiClient;
import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.funtranslation.Contents;
import com.tl.pokedex.dto.api.funtranslation.response.TranslateClientResponse;
import com.tl.pokedex.dto.api.pokeapi.FlavorText;
import com.tl.pokedex.dto.api.pokeapi.NamedApiResource;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.api.pokeapi.response.PokemonSpeciesClientResponse;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.dto.service.request.GetPokemonInfoServiceRequest;
import com.tl.pokedex.dto.service.request.GetTranslatedPokemonInformationServiceRequest;
import com.tl.pokedex.dto.service.response.GetPokemonInfoServiceResponse;
import com.tl.pokedex.dto.service.response.GetTranslatedPokemonInformationServiceResponse;
import com.tl.pokedex.exception.PokedexGenericException;
import com.tl.pokedex.service.PokedexService;
import config.TestConfig;
import constant.DescriptionConstantTest;
import constant.HabitatConstantTest;
import constant.PokemonNameConstantTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = TestConfig.class)
public class PokedexServiceTest {
    @Autowired
    private PokedexService pokedexService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @MockitoBean
    private FunTranslationClient funTranslationClient;

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void givenGetPokemonInfoServiceRequestWithPokemonName_whenGetPokemonInfo_thenReturnGetPokemonInfoServiceResponse(){
        GetPokemonInfoServiceRequest request = new GetPokemonInfoServiceRequest();
        request.setPokemonName(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME);

        //DESCRIPTION
        List<FlavorText> flavorTextList = new ArrayList<>();
        FlavorText flavorText = new FlavorText();

        NamedApiResource language = new NamedApiResource();
        language.setName(PokeApiConstant.ENGLISH_LANGUAGE);

        flavorText.setFlavorText(DescriptionConstantTest.MEWTWO_DESCRIPTION);
        flavorText.setLanguage(language);

        flavorTextList.add(flavorText);

        //HABITAT
        NamedApiResource habitat = new NamedApiResource();
        habitat.setName(HabitatConstantTest.RARE_HABITAT);

        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME);
        pokemonSpecies.setIsLegendary(true);
        pokemonSpecies.setHabitat(habitat);
        pokemonSpecies.setFlavorTextEntries(flavorTextList);

        PokemonSpeciesClientResponse clientResponse = new PokemonSpeciesClientResponse();
        clientResponse.setPokemonSpecies(pokemonSpecies);

        Mockito.when(pokeApiClient.getPokemonSpecies(Mockito.any()))
                .thenReturn(clientResponse);

        GetPokemonInfoServiceResponse response = pokedexService.getPokemonInfo(request);

        PokemonInformation expectedResult = new PokemonInformation(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME, DescriptionConstantTest.MEWTWO_DESCRIPTION, HabitatConstantTest.RARE_HABITAT, true);
        PokemonInformation pokemonInformation = response.getPokemonInformation();

        org.assertj.core.api.Assertions.assertThat(pokemonInformation).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    public void givenGetPokemonInfoServiceRequestWithEmptyString_whenGetPokemonInfo_thenThrowPokedexGenericException(){
        GetPokemonInfoServiceRequest request = new GetPokemonInfoServiceRequest();
        request.setPokemonName(StringUtils.EMPTY);

        Assertions.assertThrows(PokedexGenericException.class, () -> pokedexService.getPokemonInfo(request));
    }

    @Test
    public void givenPokemonSpeciesClientResponseNull_whenGetPokemonInfo_thenThrowPokedexGenericException(){
        GetPokemonInfoServiceRequest request = new GetPokemonInfoServiceRequest();
        request.setPokemonName(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME);

        Mockito.when(pokeApiClient.getPokemonSpecies(Mockito.any()))
                .thenReturn(null);

        Assertions.assertThrows(PokedexGenericException.class, () -> pokedexService.getPokemonInfo(request));
    }

    @Test
    public void givenPokemonInformationWithBlankDescription_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        PokemonInformation pokemonInformationWithoutDescription = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, StringUtils.EMPTY, "test", true);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(pokemonInformationWithoutDescription);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(pokemonInformationWithoutDescription, response.getPokemonInformation());
    }

    @Test
    public void givenLegendaryPokemon_whenGetTranslatedPokemonInformation_thenReturnYodaTranslation() {
        //Sometimes the service does not answer, so I'll mock the response
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(DescriptionConstantTest.MEWTWO_YODA_TRANSLATION_DESCRIPTION);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getYodaTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME, DescriptionConstantTest.MEWTWO_DESCRIPTION, "test", true);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(DescriptionConstantTest.MEWTWO_DESCRIPTION, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenPokemonWithCaveHabitat_whenGetTranslatedPokemonInformation_thenReturnYodaTranslation() {
        //Sometimes the service does not answer, so I'll mock the response
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(DescriptionConstantTest.ZUBAT_YODA_TRANSLATION_DESCRIPTION);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getYodaTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstantTest.ZUBAT_CAVE_HABITAT_POKEMON_NAME, DescriptionConstantTest.ZUBAT_DESCRIPTION, PokeApiConstant.CAVE_HABITAT, false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(DescriptionConstantTest.ZUBAT_DESCRIPTION, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenNormalPokemon_whenGetTranslatedPokemonInformation_thenReturnShakespeareTranslation() {
        //Sometimes the service does not answer, so I'll mock the response
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(DescriptionConstantTest.PIKACHU_SHAKESPEARE_TRANSLATION_DESCRIPTION);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getShakespeareTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, DescriptionConstantTest.PIKACHU_DESCRIPTION, "test", false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(DescriptionConstantTest.PIKACHU_DESCRIPTION, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenAnErrorDuringGetFunTranslation_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        Mockito.when(funTranslationClient.getYodaTranslation(Mockito.any()))
                .thenReturn(Optional.empty());

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME, DescriptionConstantTest.MEWTWO_DESCRIPTION, "test", true);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(DescriptionConstantTest.MEWTWO_DESCRIPTION, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenNullContents_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = null;
        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getShakespeareTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, DescriptionConstantTest.PIKACHU_DESCRIPTION, "test", false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(DescriptionConstantTest.PIKACHU_DESCRIPTION, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenNullTranslated_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(null);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getShakespeareTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, DescriptionConstantTest.PIKACHU_DESCRIPTION, "test", false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(DescriptionConstantTest.PIKACHU_DESCRIPTION, response.getPokemonInformation().getDescription());
    }
}
