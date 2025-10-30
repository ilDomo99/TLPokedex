package mapper;

import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.pokeapi.FlavorText;
import com.tl.pokedex.dto.api.pokeapi.NamedApiResource;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.model.PokemonInformation;
import com.tl.pokedex.mapper.PokemonInformationMapper;
import config.TestConfig;
import constant.PokemonNameConstantTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TestConfig.class)
public class PokemonInformationMapperTest {

    @Autowired
    private PokemonInformationMapper pokemonInformationMapper;

    @Test
    public void givenPokemonSpecies_whenPokemonInformationFromPokemonSpecies_thenReturnPokemonInformation() {
        final String description = "test";

        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME);
        pokemonSpecies.setIsLegendary(false);

        NamedApiResource habitat = new NamedApiResource();
        habitat.setName(PokeApiConstant.CAVE_HABITAT);

        pokemonSpecies.setHabitat(habitat);

        List<FlavorText> flavorTextList = mockFlavorText(description, PokeApiConstant.ENGLISH_LANGUAGE);
        pokemonSpecies.setFlavorTextEntries(flavorTextList);

        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        PokemonInformation expectedResult = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, description, PokeApiConstant.CAVE_HABITAT, false);

        org.assertj.core.api.Assertions.assertThat(pokemonInformation).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    public void givenPokemonSpeciesWithNullValue_whenPokemonInformationFromPokemonSpecies_thenReturnPokemonInformationWithEmptyValues() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME);

        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        PokemonInformation expectedResult = new PokemonInformation(PokemonNameConstantTest.MEWTWO_LEGENDARY_POKEMON_NAME, StringUtils.EMPTY, StringUtils.EMPTY, null);

        org.assertj.core.api.Assertions.assertThat(pokemonInformation).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    public void givenPokemonSpeciesWithNullDescription_whenPokemonInformationFromPokemonSpecies_thenReturnPokemonInformationWithoutDescription() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME);
        pokemonSpecies.setIsLegendary(false);

        NamedApiResource habitat = new NamedApiResource();
        habitat.setName(PokeApiConstant.CAVE_HABITAT);

        pokemonSpecies.setHabitat(habitat);

        List<FlavorText> flavorTextList = mockFlavorText(null, PokeApiConstant.ENGLISH_LANGUAGE);
        pokemonSpecies.setFlavorTextEntries(flavorTextList);

        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        PokemonInformation expectedResult = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, StringUtils.EMPTY, PokeApiConstant.CAVE_HABITAT, false);

        org.assertj.core.api.Assertions.assertThat(pokemonInformation).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    public void givenPokemonSpeciesWithNullFlavorText_whenPokemonInformationFromPokemonSpecies_thenReturnPokemonInformationWithoutDescription() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME);
        pokemonSpecies.setIsLegendary(false);

        NamedApiResource habitat = new NamedApiResource();
        habitat.setName(PokeApiConstant.CAVE_HABITAT);

        pokemonSpecies.setHabitat(habitat);

        List<FlavorText> flavorTextList = new ArrayList<>();
        flavorTextList.add(null);

        pokemonSpecies.setFlavorTextEntries(flavorTextList);

        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        PokemonInformation expectedResult = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, StringUtils.EMPTY, PokeApiConstant.CAVE_HABITAT, false);

        org.assertj.core.api.Assertions.assertThat(pokemonInformation).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    public void givenPokemonSpeciesWithNullLanguage_whenPokemonInformationFromPokemonSpecies_thenReturnPokemonInformationWithoutDescription() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setName(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME);
        pokemonSpecies.setIsLegendary(false);

        NamedApiResource habitat = new NamedApiResource();
        habitat.setName(PokeApiConstant.CAVE_HABITAT);

        pokemonSpecies.setHabitat(habitat);

        List<FlavorText> flavorTextList = mockFlavorText("test", null);
        FlavorText first = flavorTextList.getFirst();
        first.setLanguage(null);

        pokemonSpecies.setFlavorTextEntries(flavorTextList);

        PokemonInformation pokemonInformation = pokemonInformationMapper.pokemonInformationFromPokemonSpecies(pokemonSpecies);

        PokemonInformation expectedResult = new PokemonInformation(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME, StringUtils.EMPTY, PokeApiConstant.CAVE_HABITAT, false);

        org.assertj.core.api.Assertions.assertThat(pokemonInformation).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    private List<FlavorText> mockFlavorText(String description, String languageName){
        List<FlavorText> flavorTextList = new ArrayList<>();

        FlavorText flavorText = new FlavorText();
        flavorText.setFlavorText(description);

        NamedApiResource language = new NamedApiResource();
        language.setName(languageName);

        flavorText.setLanguage(language);

        flavorTextList.add(flavorText);

        return flavorTextList;
    }
}
