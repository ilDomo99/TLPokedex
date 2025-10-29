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

    public Optional<TranslateClientResponse> getYodaTranslation(TranslateClientRequest request){
        return getFunTranslation(FunTranslationApiConstant.YODA_TRANSLATION_ENDPOINT, request);
    }

    public Optional<TranslateClientResponse> getShakespeareTranslation(TranslateClientRequest request){
        return getFunTranslation(FunTranslationApiConstant.SHAKESPEARE_TRANSLATION_ENDPOINT, request);
    }

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
