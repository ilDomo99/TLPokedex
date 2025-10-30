package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tl.pokedex.constant.AppConstant;
import com.tl.pokedex.dto.controller.response.GetPokemonInfoFromNameControllerResponse;
import com.tl.pokedex.dto.controller.response.GetTranslatedPokemonInfoFromNameControllerResponse;
import com.tl.pokedex.dto.model.PokemonInformation;
import config.TestConfig;
import constant.DescriptionConstantTest;
import constant.HabitatConstantTest;
import constant.PokemonNameConstantTest;
import jakarta.validation.Validator;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
public class PokedexControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Validator validator;

    @BeforeEach
    public void initialization() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @SneakyThrows
    public void givenPokemonName_whenGetPokemonInfoFromName_thenReturnGetPokemonInfoFromNameControllerResponse() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_POKEMON_INFORMATION_ENDPOINT;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        GetPokemonInfoFromNameControllerResponse expectedResponse = new GetPokemonInfoFromNameControllerResponse();
        expectedResponse.setName(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME);
        expectedResponse.setDescription(DescriptionConstantTest.MEWTWO_DESCRIPTION);
        expectedResponse.setHabitat(HabitatConstantTest.RARE_HABITAT);
        expectedResponse.setIsLegendary(true);

        GetPokemonInfoFromNameControllerResponse response = objectMapper.readValue(jsonResponse, GetPokemonInfoFromNameControllerResponse.class);

        org.assertj.core.api.Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @SneakyThrows
    public void givenStringWithMoreThen30Characters_whenGetPokemonInfoFromName_thenReturnBadRequest() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_POKEMON_INFORMATION_ENDPOINT;

        mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.STRING_WITH_MORE_THEN_30_CHARS))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    @SneakyThrows
    public void givenEmptyString_whenGetPokemonInfoFromName_thenReturnNotFound() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_POKEMON_INFORMATION_ENDPOINT;

        mockMvc.perform(MockMvcRequestBuilders.get(URI, StringUtils.EMPTY))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    @SneakyThrows
    public void givenNotExistingPokemonName_whenGetPokemonInfoFromName_thenReturnInternalServerError() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_POKEMON_INFORMATION_ENDPOINT;

        mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.NON_POKEMON_NAME))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
    }

    @Test
    @SneakyThrows
    public void givenLegendaryPokemonName_whenGetTranslatedPokemonInfoFromName_thenReturnDescriptionWithYodaTranslationOrOriginalDescription() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        GetTranslatedPokemonInfoFromNameControllerResponse response = objectMapper.readValue(jsonResponse, GetTranslatedPokemonInfoFromNameControllerResponse.class);
        String description = response.getDescription();

        boolean isValid = DescriptionConstantTest.MEWTWO_DESCRIPTION.equalsIgnoreCase(description) ||
                DescriptionConstantTest.MEWTWO_YODA_TRANSLATION_DESCRIPTION.equalsIgnoreCase(description);

        Assertions.assertTrue(isValid);
    }

    @Test
    @SneakyThrows
    public void givenCaveHabitatPokemon_whenGetTranslatedPokemonInfoFromName_thenReturnDescriptionWithYodaTranslationOrOriginalDescription() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.ZUBAT_CAVE_HABITAT_POKEMON_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        GetTranslatedPokemonInfoFromNameControllerResponse response = objectMapper.readValue(jsonResponse, GetTranslatedPokemonInfoFromNameControllerResponse.class);
        String description = response.getDescription();

        boolean isValid = DescriptionConstantTest.ZUBAT_DESCRIPTION.equalsIgnoreCase(description) ||
                DescriptionConstantTest.ZUBAT_YODA_TRANSLATION_DESCRIPTION.equalsIgnoreCase(description);

        Assertions.assertTrue(isValid);
    }

    @Test
    @SneakyThrows
    public void givenNormalPokemonName_whenGetTranslatedPokemonInfoFromName_thenReturnDescriptionWithShakespeareTranslationOrOriginalDescription() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        GetTranslatedPokemonInfoFromNameControllerResponse response = objectMapper.readValue(jsonResponse, GetTranslatedPokemonInfoFromNameControllerResponse.class);
        String description = response.getDescription();

        boolean isValid = DescriptionConstantTest.PIKACHU_DESCRIPTION.equalsIgnoreCase(description) ||
                DescriptionConstantTest.PIKACHU_SHAKESPEARE_TRANSLATION_DESCRIPTION.equalsIgnoreCase(description);

        Assertions.assertTrue(isValid);
    }

    @Test
    @SneakyThrows
    public void givenStringWithMoreThen30Characters_whenGetTranslatedPokemonInfoFromName_thenReturnBadRequest() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT;

        mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.STRING_WITH_MORE_THEN_30_CHARS))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    @SneakyThrows
    public void givenEmptyString_whenGetTranslatedPokemonInfoFromName_thenReturnNotFound() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT;

        mockMvc.perform(MockMvcRequestBuilders.get(URI, StringUtils.EMPTY))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    @SneakyThrows
    public void givenNotExistingPokemonName_whenGetTranslatedPokemonInfoFromName_thenReturnInternalServerError() {

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_TRANSLATED_POKEMON_INFORMATION_ENDPOINT;

        mockMvc.perform(MockMvcRequestBuilders.get(URI, PokemonNameConstantTest.NON_POKEMON_NAME))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
    }

    @Test
    void givenPokemonInformation_whenCopyProperties_thenGetPokemonInfoFromNameControllerResponseHasAllFieldsCopied() {
        PokemonInformation source = new PokemonInformation(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME, "test", "test", true);
        GetPokemonInfoFromNameControllerResponse target = new GetPokemonInfoFromNameControllerResponse();

        BeanUtils.copyProperties(source, target);

        org.assertj.core.api.Assertions.assertThat(target).usingRecursiveComparison().isEqualTo(source);
    }

    @Test
    void givenPokemonInformation_whenCopyProperties_thenGetTranslatedPokemonInfoFromNameControllerResponseHasAllFieldsCopied() {
        PokemonInformation source = new PokemonInformation(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME, "test", "test", true);
        GetTranslatedPokemonInfoFromNameControllerResponse target = new GetTranslatedPokemonInfoFromNameControllerResponse();

        BeanUtils.copyProperties(source, target);

        org.assertj.core.api.Assertions.assertThat(target).usingRecursiveComparison().isEqualTo(source);
    }
}
