package com.tl.pokedex.dto.api.funtranslation.response;

import com.tl.pokedex.dto.api.funtranslation.Contents;
import com.tl.pokedex.dto.api.funtranslation.Success;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslateClientResponse {
    private Success success;
    private Contents contents;
}
