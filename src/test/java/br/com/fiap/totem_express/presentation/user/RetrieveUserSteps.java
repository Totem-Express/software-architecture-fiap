package br.com.fiap.totem_express.presentation.user;

import br.com.fiap.totem_express.application.user.CreateUserUseCase;
import br.com.fiap.totem_express.application.user.RetrieveUserUseCase;
import br.com.fiap.totem_express.application.user.output.DefaultUserView;
import br.com.fiap.totem_express.presentation.user.requests.CreateUserRequest;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class RetrieveUserSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RetrieveUserUseCase retrieveUserUseCase;

    @Autowired
    private CreateUserUseCase createUserUseCase;

    private MvcResult response;
    private DefaultUserView createdUser;

    @Given("a user exists with document {string} and with name {string} and with email {string}")
    public void aUserExistsWithDocument(String document, String name, String email) {
        createdUser = createUserUseCase.execute(new CreateUserRequest(name, email, document));
    }

    @Given("no user exists with document {string}")
    public void noUserExistsWithDocument(String document) {
       assertThat(retrieveUserUseCase.execute(document)).isEmpty();
    }

    @When("I send a GET request to {string} with parameter {string}")
    public void iSendAGETRequestToWithParameter(String endpoint, String parameter) throws Exception {
        String[] param = parameter.split("=");
        response = mockMvc.perform(get(endpoint).param(param[0], param[1])).andReturn();
    }

    @Then("should return http {int}")
    public void theResponseStatusShouldBe(int status) {
        assertThat(status).isEqualTo(response.getResponse().getStatus());
    }

    @Then("the response body should contain:")
    public void theResponseBodyShouldContain(DataTable dataTable) throws Exception {
        Map<String, String> expectedData = dataTable.asMaps().get(0);
        String responseBody = response.getResponse().getContentAsString();
        Long responseId = ((Integer)JsonPath.read(responseBody, "$.id")).longValue();
        assertThat(responseId).isEqualTo(createdUser.id());
        assertThat(JsonPath.read(responseBody, "$.name").toString()).isEqualTo(expectedData.get("name"));
    }
}