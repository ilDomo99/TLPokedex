package com.tl.pokedex.client;

import com.tl.pokedex.constant.FunTranslationApiConstant;
import com.tl.pokedex.dto.api.funtranslation.request.TranslateClientRequest;
import com.tl.pokedex.dto.api.funtranslation.response.TranslateClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class FunTranslationClient {

    private final RestTemplate restTemplate;

    public FunTranslationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Translates a given text into Yoda's speech style calling {@value FunTranslationApiConstant#YODA_TRANSLATION_ENDPOINT}
     *
     * This method interacts with an external translation service to perform the translation.
     * If the translation request fails or the response is null, an empty {@link Optional} is returned.
     *
     * @param request the request object containing the text to be translated
     * @return an {@link Optional} containing the {@link TranslateClientResponse} with the translation result,
     *         or an empty {@link Optional} if the translation fails
     */
    public Optional<TranslateClientResponse> getYodaTranslation(TranslateClientRequest request){
        return getFunTranslation(FunTranslationApiConstant.YODA_TRANSLATION_ENDPOINT, request);
    }

    /**
     * Translates a given text into Shakespearean English style calling {@value FunTranslationApiConstant#SHAKESPEARE_TRANSLATION_ENDPOINT}
     *
     * This method uses an external translation service to convert the input text
     * into a Shakespearean style. If the translation request fails or the response is null,
     * an empty {@link Optional} is returned.
     *
     * @param request the request object containing the text to be translated
     * @return an {@link Optional} containing the {@link TranslateClientResponse} with the translation result,
     *         or an empty {@link Optional} if the translation fails
     */
    public Optional<TranslateClientResponse> getShakespeareTranslation(TranslateClientRequest request){
        return getFunTranslation(FunTranslationApiConstant.SHAKESPEARE_TRANSLATION_ENDPOINT, request);
    }

    /**
     * Performs a fun translation request to an external service.
     * This method sends a POST request to the provided translation API endpoint with the given request payload.
     * If the request fails or the response is null, an empty {@link Optional} is returned.
     *
     * @param translationTypeUrl the URL of the translation API endpoint to which the request will be sent
     * @param request the {@link TranslateClientRequest} object containing the text to be translated
     * @return an {@link Optional} containing the {@link TranslateClientResponse} with the result of the translation,
     *         or an empty {@link Optional} if the request fails or no response is received
     */
    private Optional<TranslateClientResponse> getFunTranslation(String translationTypeUrl, TranslateClientRequest request){
        TranslateClientResponse response;

        try {
            response = restTemplate.postForObject(translationTypeUrl, request, TranslateClientResponse.class);
        } catch (RestClientException exception){
            log.error("Error: ", exception);
            return Optional.empty();
        }

        if(response == null) return Optional.empty();

        return Optional.of(response);
    }
}
