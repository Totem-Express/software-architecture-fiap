package br.com.fiap.totem_express.presentation.payment;

import br.com.fiap.totem_express.application.payment.CheckPaymentStatusUseCase;
import br.com.fiap.totem_express.application.payment.PaymentGateway;
import br.com.fiap.totem_express.application.payment.ProcessPaymentWebhookUseCase;
import br.com.fiap.totem_express.application.payment.output.PaymentView;
import br.com.fiap.totem_express.domain.payment.Payment;
import br.com.fiap.totem_express.domain.payment.Status;
import br.com.fiap.totem_express.presentation.payment.request.PaymentWebhookRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import static br.com.fiap.totem_express.domain.payment.Status.PAID;
import static br.com.fiap.totem_express.domain.payment.Status.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentController paymentController;

    @Autowired
    private PaymentGateway paymentGateway;

    @Autowired
    private CheckPaymentStatusUseCase checkPaymentStatusUseCase;
    
    @Autowired
    private ProcessPaymentWebhookUseCase processPaymentWebhookUseCase;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    private ResponseEntity<PaymentView> response;
    private Payment payment;
    private int responseStatus;

    @Given("a payment exists with id {long} and status {string} and qrCode {string}")
    public void a_payment_with_id_exists(Long id, String status, String qrCode) {
        payment = new Payment(id, null, null, Status.valueOf(status), null, null, qrCode);
        paymentGateway.create(payment);
        checkPaymentStatusUseCase.checkStatus(id);
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Then("the payment response status should be {int}")
    public void theResponseStatusShouldBeForPayment(int statusCode) throws Exception {
        mockMvc.perform(get("/api/payment/{id}", 1))
                .andExpect(status().is(statusCode));
    }

    @Given("no payment exists with id 1:")
    public void no_payment_exists_with_id(DataTable dataTable) {
        Map<String, String> userData = dataTable.asMaps().get(0);
        Status status = Status.valueOf(userData.get("status"));
        payment = new Payment(null, null, null, status, null, null, userData.get("qrCode"));
        paymentGateway.create(payment);
    }

    @Then("x the payment response status should be {int}")
    public void theResponseStatusShouldBeForPayment2(int statusCode) throws Exception {
        mockMvc.perform(get("/api/payment/null"))
                .andExpect(status().is(statusCode));
    }

    @Given("xa payment exists with id {long}")
    public void a_payment_with_id_exists(Long id) {
        payment = new Payment(id, null, null, PENDING, null, null, "qrCode");
        paymentGateway.create(payment);
    }
    @When("I send a POST request to {string} with body:")
    public void iSendAPOSTRequestToWithBody(String url, String body) throws Exception {
        MvcResult andReturn = mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(body)).andReturn();
        responseStatus = andReturn.getResponse().getStatus();
    }

    @Then("ythe payment response status should be {int}")
    public void ytheResponseStatusShouldBeForPayment(int statusCode) throws Exception {
        assertThat(statusCode).isEqualTo(responseStatus);
    }

    @Given("Xno payment exists:")
    public void Xno_payment_exists_with_id(DataTable dataTable) {
        Map<String, String> userData = dataTable.asMaps().get(0);
        Status status = Status.valueOf(userData.get("status"));
        payment = new Payment(null, null, null, status, null, null, "qrCode");
        paymentGateway.create(payment);
    }

    @When("XI send a POST request to {string} with body:")
    public void XiSendAPOSTRequestToWithBody(String url, String body) throws Exception {
        MvcResult andReturn = mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(body)).andReturn();
        responseStatus = andReturn.getResponse().getStatus();
    }

    @Then("Zthe payment response status should be {int}")
    public void ZtheResponseStatusShouldBeForPayment(int statusCode) throws Exception {
        assertThat(responseStatus).isEqualTo(statusCode);
    }

}