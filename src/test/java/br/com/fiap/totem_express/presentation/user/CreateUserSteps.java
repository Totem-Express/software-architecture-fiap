package br.com.fiap.totem_express.presentation.user;

import br.com.fiap.totem_express.presentation.user.requests.CreateUserRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class CreateUserSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<CreateUserRequest> createUserRequest;

    private CreateUserRequest userRequest;
    private int responseStatus;

    @Given("I have a valid user with:")
    public void iHaveAValidUserWith(DataTable dataTable) {
        Map<String, String> userData = dataTable.asMaps().get(0);
        userRequest = new CreateUserRequest(userData.get("name"), userData.get("email"), userData.get("document"));
    }

    @Given("I have an invalid user with:")
    public void iHaveAnInvalidUserWith(DataTable dataTable) {
        Map<String, String> userData = dataTable.asMaps().get(0);
        userRequest = new CreateUserRequest(userData.get("name"), userData.get("email"), userData.get("document"));
    }

    @When("I send a POST request to {string}")
    public void iSendAPOSTRequestTo(String endpoint) throws Exception {
        MvcResult result = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequest
                        .write(userRequest)
                        .getJson()
                ))
                .andReturn();
        responseStatus = result.getResponse().getStatus();
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertThat(status).isEqualTo(responseStatus);
    }
}