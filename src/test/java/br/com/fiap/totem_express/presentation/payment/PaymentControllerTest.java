package br.com.fiap.totem_express.presentation.payment;

import br.com.fiap.totem_express.TestcontainersConfiguration;
import br.com.fiap.totem_express.application.payment.CheckPaymentStatusUseCase;
import br.com.fiap.totem_express.application.payment.ProcessPaymentWebhookUseCase;
import br.com.fiap.totem_express.application.payment.output.PaymentView;
import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import br.com.fiap.totem_express.presentation.payment.request.PaymentWebhookRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.fiap.totem_express.domain.payment.Status.FAILED;
import static br.com.fiap.totem_express.domain.payment.Status.PAID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CheckPaymentStatusUseCase checkPaymentStatusUseCase;
    @MockBean
    private ProcessPaymentWebhookUseCase processPaymentWebhookUseCase;

    @Test
    void should_return_http_200_and_payment_view_when_payment_exists() throws Exception {
        Long paymentId = 1L;
        var expectedView = new PaymentView.SimpleView(paymentId, PAID, "qrcode-data");

        when(checkPaymentStatusUseCase.checkStatus(paymentId)).thenReturn(expectedView);

        mockMvc.perform(get("/api/payment/{id}", paymentId))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(paymentId),
                        jsonPath("$.status").value("PAID"),
                        jsonPath("$.qrCode").value("qrcode-data")
                );
    }

    @Test
    void should_return_http_404_when_payment_does_not_exist() throws Exception {
        Long paymentId = 1L;

        doThrow(new IllegalArgumentException("Payment must exist invalid id " + paymentId)).when(checkPaymentStatusUseCase).checkStatus(paymentId);

        mockMvc.perform(get("/api/payment/{id}", paymentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_http_200_when_payment_is_processed_successfully() throws Exception {
        Long paymentId = 1L;
        var request = new PaymentWebhookRequest(paymentId, PAID);

        doNothing().when(processPaymentWebhookUseCase).process(paymentId, request);

        mockMvc.perform(post("/api/payment/process/{id}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

    @Test
    void should_return_http_404_when_payment_does_not_exist_during_processing() throws Exception {
        Long paymentId = 1L;

        var request = new PaymentWebhookRequest(paymentId, FAILED);

        doThrow(new IllegalArgumentException("Payment must exist invalid id " + paymentId))
                .when(processPaymentWebhookUseCase)
                .process(eq(paymentId), any());

        mockMvc.perform(post("/api/payment/process/{id}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound());
    }

}