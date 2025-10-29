package util;

import com.tl.pokedex.constant.ErrorConstant;
import com.tl.pokedex.exception.PokedexGenericException;
import com.tl.pokedex.util.ValidationUtil;
import config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class ValidationUtilTest {

    @Autowired
    private ValidationUtil validationUtil;

   @Test
   public void givenNullObject_whenIsValidOrFail_thenThrowPokedexGenericException(){
       String nullStringTest = null;

       Assertions.assertThrows(PokedexGenericException.class, () -> validationUtil.isValidOrFail(String.class, nullStringTest));
   }

    @Test
    public void givenNullClass_whenIsValidOrFail_thenThrowExceptionMessageWithQuestionMarkInsteadOfClassName(){
        String nullStringTest = null;

        PokedexGenericException pokedexGenericException = Assertions.assertThrows(PokedexGenericException.class, () -> validationUtil.isValidOrFail(null, nullStringTest));

        String errorMessage = ErrorConstant.VALIDATION_ERROR_MESSAGE
                .formatted("?", ErrorConstant.OBJECT_CANNOT_BE_NULL_MESSAGE);

        Assertions.assertEquals(pokedexGenericException.getReason(), errorMessage);
    }

}
