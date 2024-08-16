package stepdefinitions.integrationtest;

import com.gabezy.foodnow.domain.entity.Cuisine;
import com.gabezy.foodnow.repositories.CuisineRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import stepdefinitions.StepsDefinitionsDefault;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

@RequiredArgsConstructor
public class CuisineStep extends StepsDefinitionsDefault {

    private final TestRestTemplate testRestTemplate;
    private final CuisineRepository cuisineRepository;
    private ResponseEntity<Cuisine[]> response;
    private final List<Cuisine> cuisinesSaved = new ArrayList<>();

    @Given("that there are cuisines registered in the system:")
    public void that_there_are_cuisines_registered_the_system(DataTable dataTable) {
        List<Map<String, String>> cuisines = dataTable.asMaps();
        for (Map<String, String> cuisineData : cuisines) {
            var cuisine = new Cuisine();
            cuisine.setName(cuisineData.get("name"));
            cuisineRepository.save(cuisine);
            cuisinesSaved.add(cuisine);
        }

    }

    @When("i make a GET request to retrieve the cuisines")
    public void i_make_a_GET_request_to_retrieve_the_cuisines() {
        response = testRestTemplate.getForEntity("/cuisines", Cuisine[].class);
    }

    @Then("the response must have the status code {int}")
    public void the_response_must_have_the_status_code(int statusCode) {
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("the response must contain the data that was recorded earlier")
    public void the_response_must_contain_the_data_that_was_recorded_earlier() {
        cuisinesSaved.forEach(cuisine -> {
            List<Cuisine> cuisines = Arrays.asList(Objects.requireNonNull(response.getBody()));
            assertTrue(cuisines.contains(cuisine));
        });
    }
}
