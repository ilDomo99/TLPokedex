import com.tl.pokedex.PokedexApplication;
import config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class PokedexApplicationTest {
   @Test
   public void givenArgs_whenMain_thenStartTheApplication() {
      PokedexApplication.main(new String[] {});
   }
}
