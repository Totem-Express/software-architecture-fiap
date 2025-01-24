package br.com.fiap.totem_express.presentation.payment;

import br.com.fiap.totem_express.application.payment.PaymentGateway;
import br.com.fiap.totem_express.domain.payment.Payment;
import br.com.fiap.totem_express.domain.payment.Status;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentGateway paymentGateway;

    private int responseStatus;

    @Given("a payment exists with id {long}, status {string}, and qrCode {string}")
    public void a_payment_exists_with_id_status_and_qrCode(Long id, String status, String qrCode) {
        Payment payment = new Payment(id, null, null, Status.valueOf(status), null, null, qrCode);
        paymentGateway.create(payment);
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String url) throws Exception {
        MvcResult result = mockMvc.perform(get(url)).andReturn();
        responseStatus = result.getResponse().getStatus();
    }

    @Then("response status should be {int}")
    public void response_status_should_be(int statusCode) {
        assertThat(responseStatus).isEqualTo(statusCode);
    }


    @Given("no payment exists with id {long}")
    public void no_payment_exists_with_id(Long id) {
        if (paymentGateway.findById(id).isPresent()) { // Verifica se o pagamento existe
            throw new IllegalStateException("A payment with id " + id + " already exists, but the test requires it to not exist.");
        }
    }

    @When("I send a POST request to {string} with body:")
    public void i_send_a_post_request_to_with_body(String url, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(body))
                .andReturn();
        responseStatus = result.getResponse().getStatus();
    }
}