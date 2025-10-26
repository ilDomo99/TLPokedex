package com.tl.pokedex.client;

import com.tl.pokedex.constant.FunTranslationApiConstant;
import com.tl.pokedex.dto.api.funtranslation.Contents;
import com.tl.pokedex.dto.api.funtranslation.request.TranslateRequest;
import com.tl.pokedex.dto.api.funtranslation.response.TranslateResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
public class FunTranslationClient {

    private final RestTemplate restTemplate;

    public FunTranslationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getYodaTranslation(@NotBlank String textToTranslate){
        return getFunTranslation(FunTranslationApiConstant.YODA_TRANSLATION_ENDPOINT, textToTranslate);
    }

    public String getShakespeareTranslation(@NotBlank String textToTranslate){
        return getFunTranslation(FunTranslationApiConstant.SHAKESPEARE_TRANSLATION_ENDPOINT, textToTranslate);
    }

    private String getFunTranslation(@NotBlank String translationTypeUrl, @NotBlank String textToTranslate){
        TranslateRequest request = new TranslateRequest();
        request.setText(textToTranslate);

        TranslateResponse response;

        try {
            response = restTemplate.postForObject(translationTypeUrl, request, TranslateResponse.class);
        } catch (RestClientException exception){
            return textToTranslate;
        }

        if(response == null) return textToTranslate;

        Contents contents = response.getContents();

        if(contents == null) return textToTranslate;

        String translated = contents.getTranslated();

        return translated != null ? translated : textToTranslate;
    }
}
