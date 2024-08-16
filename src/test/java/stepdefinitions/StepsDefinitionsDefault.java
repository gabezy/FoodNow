package stepdefinitions;

import com.gabezy.foodnow.FoodNowApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = FoodNowApplication.class)
public class StepsDefinitionsDefault {
}
