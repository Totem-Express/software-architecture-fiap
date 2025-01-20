package br.com.fiap.totem_express.presentation.payment;

import br.com.fiap.totem_express.application.payment.CheckPaymentStatusUseCase;
import br.com.fiap.totem_express.application.payment.ProcessPaymentWebhookUseCase;
import br.com.fiap.totem_express.application.payment.output.PaymentView;
import br.com.fiap.totem_express.domain.payment.Status;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentSteps {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckPaymentStatusUseCase checkPaymentStatusUseCase;

    @MockBean
    private ProcessPaymentWebhookUseCase processPaymentWebhookUseCase;

    @Given("a payment exists with id {string} and status {string} and qrCode {string}")
    public void a_payment_exists_with_id_and_status_and_qrCode(String id, String status, String qrCode) {
        Long paymentId = Long.parseLong(id);
        Status paymentStatus = Status.valueOf(status);
        var expectedView = new PaymentView.SimpleView(paymentId, paymentStatus, qrCode);

        when(checkPaymentStatusUseCase.checkStatus(paymentId)).thenReturn(expectedView);
    }

    @Given("no payment exists with id {string}")
    public void no_payment_exists_with_id(String id) {
        Long paymentId = Long.parseLong(id);

        doThrow(new IllegalArgumentException("Payment must exist invalid id " + paymentId))
                .when(checkPaymentStatusUseCase)
                .checkStatus(paymentId);
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }
}
