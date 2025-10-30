package util;

import com.tl.pokedex.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilTest {

    @Test
    public void givenNullString_whenRemoveEscapeCharacters_thenReturnNull(){
        String testString = null;

        String result = StringUtil.removeEscapeCharacters(testString);

        Assertions.assertNull(result);
    }

    @Test
    public void givenStringWithEscapeCharacter_whenRemoveEscapeCharacters_thenReturnStringWithoutEscapeCharacter(){
        String testString = "It was created by\na scientist after\nyears of horrific\fgene splicing and\nDNA engineering\nexperiments.";

        String stringWithoutEscapeCharacters = StringUtil.removeEscapeCharacters(testString);

        String correctString = "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.";

        Assertions.assertEquals(correctString, stringWithoutEscapeCharacters);
    }
}
