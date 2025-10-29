package service;

import com.tl.pokedex.client.FunTranslationClient;
import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.funtranslation.Contents;
import com.tl.pokedex.dto.api.funtranslation.response.TranslateClientResponse;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.dto.service.request.GetPokemonInfoServiceRequest;
import com.tl.pokedex.dto.service.request.GetTranslatedPokemonInformationServiceRequest;
import com.tl.pokedex.dto.service.response.GetPokemonInfoServiceResponse;
import com.tl.pokedex.dto.service.response.GetTranslatedPokemonInformationServiceResponse;
import com.tl.pokedex.exception.PokedexGenericException;
import com.tl.pokedex.service.PokedexService;
import config.TestConfig;
import constant.PokemonNameConstant;
import constant.TranslationConstant;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

@SpringBootTest(classes = TestConfig.class)
public class PokedexServiceTest {
    @Autowired
    private PokedexService pokedexService;

    @MockitoBean
    private FunTranslationClient funTranslationClient;

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void givenGetPokemonInfoServiceRequestWithPokemonName_whenGetPokemonInfo_thenReturnGetPokemonInfoServiceResponse(){
        GetPokemonInfoServiceRequest request = new GetPokemonInfoServiceRequest();
        request.setPokemonName(PokemonNameConstant.LEGENDARY_POKEMON_NAME);

        GetPokemonInfoServiceResponse response = pokedexService.getPokemonInfo(request);

        Assertions.assertNotNull(response);
    }

    @Test
    public void givenGetPokemonInfoServiceRequestWithoutPokemonName_whenGetPokemonInfo_thenThrowPokedexGenericException(){
        GetPokemonInfoServiceRequest request = new GetPokemonInfoServiceRequest();
        request.setPokemonName(StringUtils.EMPTY);

        Assertions.assertThrows(PokedexGenericException.class, () -> pokedexService.getPokemonInfo(request));
    }

    @Test
    public void givenPokemonInformationWithBlankDescription_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        PokemonInformation pokemonInformationWithoutDescription = new PokemonInformation(PokemonNameConstant.NORMAL_POKEMON_NAME, StringUtils.EMPTY, "test", true);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(pokemonInformationWithoutDescription);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(pokemonInformationWithoutDescription, response.getPokemonInformation());
    }

    @Test
    public void givenLegendaryPokemon_whenGetTranslatedPokemonInformation_thenReturnYodaTranslation() {
        final String originalDescription = "This is a legendary Pokemon";

        //Sometimes the service does not answer, so I'll mock the response
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(TranslationConstant.MEWTWO_YODA_DESCRIPTION_TRANSLATION);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getYodaTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstant.LEGENDARY_POKEMON_NAME, originalDescription, "test", true);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(originalDescription, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenPokemonWithCaveHabitat_whenGetTranslatedPokemonInformation_thenReturnYodaTranslation() {
        final String originalDescription = "This is a cave habitat Pokemon";

        //Sometimes the service does not answer, so I'll mock the response
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(TranslationConstant.MEWTWO_YODA_DESCRIPTION_TRANSLATION);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getYodaTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstant.NORMAL_POKEMON_NAME, originalDescription, PokeApiConstant.CAVE_HABITAT, false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(originalDescription, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenNormalPokemon_whenGetTranslatedPokemonInformation_thenReturnShakespeareTranslation() {
        final String originalDescription = "This is a normal Pokemon";

        //Sometimes the service does not answer, so I'll mock the response
        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(TranslationConstant.PIKACHU_SHAKESPEARE_DESCRIPTION_TRANSLATION);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getShakespeareTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstant.NORMAL_POKEMON_NAME, originalDescription, "test", false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertNotEquals(originalDescription, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenAnErrorDuringGetFunTranslation_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        final String originalDescription = "This is a legendary Pokemon";

        Mockito.when(funTranslationClient.getYodaTranslation(Mockito.any()))
                .thenReturn(Optional.empty());

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstant.LEGENDARY_POKEMON_NAME, originalDescription, "test", true);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(originalDescription, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenNullContents_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        final String originalDescription = "This is a normal Pokemon";

        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = null;
        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getShakespeareTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstant.NORMAL_POKEMON_NAME, originalDescription, "test", false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(originalDescription, response.getPokemonInformation().getDescription());
    }

    @Test
    public void givenNullTranslated_whenGetTranslatedPokemonInformation_thenReturnOriginalDescription() {
        final String originalDescription = "This is a normal Pokemon";

        TranslateClientResponse translateClientResponse = new TranslateClientResponse();

        Contents contents = new Contents();
        contents.setTranslated(null);

        translateClientResponse.setContents(contents);

        Mockito.when(funTranslationClient.getShakespeareTranslation(Mockito.any()))
                .thenReturn(Optional.of(translateClientResponse));

        PokemonInformation PokemonInformationOfLegendaryPokemon = new PokemonInformation(PokemonNameConstant.NORMAL_POKEMON_NAME, originalDescription, "test", false);

        GetTranslatedPokemonInformationServiceRequest request = new GetTranslatedPokemonInformationServiceRequest();
        request.setPokemonInformation(PokemonInformationOfLegendaryPokemon);

        GetTranslatedPokemonInformationServiceResponse response = pokedexService.getTranslatedPokemonInformation(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(originalDescription, response.getPokemonInformation().getDescription());
    }
}
