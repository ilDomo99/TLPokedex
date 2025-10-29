package controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class PokedexControllerTest {

    protected MockMvc mockMvc;
    private final WebApplicationContext wac;

    public PokedexControllerTest(WebApplicationContext wac) {
        this.wac = wac;
    }

    @BeforeEach
    public void initialization() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /*
    @Test
    public void givenName_whenGetPokemonInfoFromName_thenReturnPokemonInformation(){

        String URI = AppConstant.BASE_ENDPOINT + AppConstant.GET_POKEMON_INFORMATION_ENDPOINT;

        String name = "mewtwo";

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get(URI)
                                .param("name", "TestName")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        SendWithholdingFromWorkflowResource res = mapper.readValue(mvcResult.getResponse().getContentAsString(), SendWithholdingFromWorkflowResource.class);
        Assertions.assertNotNull(res);

    }

     */
}
