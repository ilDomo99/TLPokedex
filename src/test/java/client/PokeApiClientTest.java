package client;

import com.tl.pokedex.client.PokeApiClient;
import com.tl.pokedex.constant.PokeApiConstant;
import com.tl.pokedex.dto.api.pokeapi.PokemonSpecies;
import com.tl.pokedex.dto.api.pokeapi.request.PokemonSpeciesClientRequest;
import com.tl.pokedex.dto.api.pokeapi.response.PokemonSpeciesClientResponse;
import config.TestConfig;
import constant.PokemonNameConstantTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestConfig.class)
public class PokeApiClientTest {

    @Autowired
    private PokeApiClient pokeApiClient;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    public void testGetPokemonSpecies() {
        PokemonSpeciesClientRequest request = new PokemonSpeciesClientRequest();
        request.setPokemonName(PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME);

        PokemonSpecies mockedPokemonSpecies = new PokemonSpecies();

        Mockito.when(restTemplate.getForObject(
                        PokeApiConstant.POKEMON_SPECIES_ENDPOINT,
                        PokemonSpecies.class,
                        PokemonNameConstantTest.PIKACHU_NORMAL_POKEMON_NAME))
                .thenReturn(mockedPokemonSpecies);

        PokemonSpeciesClientResponse response = pokeApiClient.getPokemonSpecies(request);

        assertNotNull(response);
        assertNotNull(response.getPokemonSpecies());
    }
}
