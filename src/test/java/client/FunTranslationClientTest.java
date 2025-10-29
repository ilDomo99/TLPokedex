package client;

import com.tl.pokedex.client.FunTranslationClient;
import com.tl.pokedex.dto.api.funtranslation.request.TranslateClientRequest;
import com.tl.pokedex.dto.api.funtranslation.response.TranslateClientResponse;
import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestConfig.class)
public class FunTranslationClientTest {

    @MockitoBean
    private RestTemplate restTemplate;

    @Autowired
    private FunTranslationClient funTranslationClient;

    @Test
    public void givenTranslateClientRequest_whenGetYodaTranslation_thenReturnTranslateClientResponse() {
        TranslateClientRequest request = new TranslateClientRequest();
        TranslateClientResponse response = new TranslateClientResponse();
        when(restTemplate.postForObject(Mockito.any(String.class), Mockito.any(TranslateClientRequest.class), any()))
                .thenReturn(response);

        Optional<TranslateClientResponse> result = funTranslationClient.getYodaTranslation(request);

        assertTrue(result.isPresent());
        assertEquals(response, result.get());
    }

    @Test
    public void givenTranslateClientRequest_whenGetYodaTranslation_thenReturnOptionalEmpty() {
        TranslateClientRequest request = new TranslateClientRequest();
        when(restTemplate.postForObject(any(String.class), any(TranslateClientRequest.class), any()))
                .thenReturn(null);

        Optional<TranslateClientResponse> result = funTranslationClient.getYodaTranslation(request);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenException_whenGetYodaTranslation_thenReturnOptionalEmpty() {
        TranslateClientRequest request = new TranslateClientRequest();

        when(restTemplate.postForObject(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("API error"));

        Optional<TranslateClientResponse> result = funTranslationClient.getYodaTranslation(request);

        assertTrue(result.isEmpty());
    }
}
