package br.com.fiap.totem_express.infrastructure.payment.mercadopago;

import br.com.fiap.totem_express.application.payment.input.GenerateQRCodeInput;
import br.com.fiap.totem_express.application.payment.input.QRCodeItemInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MercadoPagoGatewayTest {

    private RestTemplate restTemplate;
    private MercadoPagoGateway mercadoPagoGateway;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        mercadoPagoGateway = new MercadoPagoGateway(restTemplate);
        mercadoPagoGateway.API_TOKEN = "test-token";
        mercadoPagoGateway.API_URL = "https://api.mercadopago.com/qrcode";
    }

    @Test
    void should_return_payment_qr_code_when_request_is_successful() {
        PaymentQRCodeRequest input = new PaymentQRCodeRequest(
                UUID.randomUUID().toString(),
                "Test Order",
                "Order Description",
                new BigDecimal("100.00"),
                List.of(
                        new PaymentQRCodeItem("Item 1", "Description 1", new BigDecimal("50.00"), 1L, "unit", new BigDecimal("50.00")),
                        new PaymentQRCodeItem("Item 2", "Description 2", new BigDecimal("50.00"), 1L, "unit", new BigDecimal("50.00"))
                ));


        PaymentProcessorResponse expectedResponse = new PaymentProcessorResponse("qrData123", "orderId123");
        ResponseEntity<PaymentProcessorResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.exchange(any(RequestEntity.class), eq(PaymentProcessorResponse.class))).thenReturn(responseEntity);

        PaymentProcessorResponse actualResponse = mercadoPagoGateway.createPaymentQRCode(input);

        assertThat(actualResponse.getQrData()).isEqualTo(expectedResponse.getQrData());
        assertThat(actualResponse.getStoreOrderId()).isEqualTo(expectedResponse.getStoreOrderId());

        ArgumentCaptor<RequestEntity> captor = ArgumentCaptor.forClass(RequestEntity.class);

        verify(restTemplate).exchange(captor.capture(), eq(PaymentProcessorResponse.class));
        RequestEntity<PaymentQRCodeRequest> capturedRequest = captor.getValue();
        assertThat(capturedRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).isEqualTo("Bearer test-token");
        assertThat(capturedRequest.getUrl()).isEqualTo(URI.create("https://api.mercadopago.com/qrcode"));
    }

    @Test
    void should_throw_exception_when_response_status_is_not_2xx() {
        GenerateQRCodeInput input = mock(GenerateQRCodeInput.class);

        ResponseEntity<PaymentProcessorResponse> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(any(RequestEntity.class), eq(PaymentProcessorResponse.class))).thenReturn(responseEntity);

        var exception = assertThrows(RestClientException.class, () -> mercadoPagoGateway.createPaymentQRCode(input));
        assertThat(exception.getMessage()).isEqualTo("generating QRCode from provider error");
    }

    @Test
    void should_throw_exception_when_rest_template_throws_exception() {
        var item = new PaymentQRCodeItem("Item 1", "Description 1", new BigDecimal("29.90"), 1L, "unit", new BigDecimal("29.90"));
        var request = new PaymentQRCodeRequest("123456", "Name order", "Order description", new BigDecimal("89.90"), List.of(item));

        when(restTemplate.exchange(any(RequestEntity.class), eq(PaymentProcessorResponse.class)))
                .thenThrow(new RestClientException("Connection error"));

        var exception = assertThrows(RestClientException.class, () -> mercadoPagoGateway.createPaymentQRCode(request));
        assertThat(exception.getMessage()).isEqualTo("generating QRCode from provider error");
    }

}