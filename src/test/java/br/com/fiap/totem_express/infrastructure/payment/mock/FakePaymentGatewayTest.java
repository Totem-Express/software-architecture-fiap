package br.com.fiap.totem_express.infrastructure.payment.mock;

import br.com.fiap.totem_express.infrastructure.payment.mercadopago.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FakePaymentGatewayTest {

    @Test
    void should_generate_payment_qr_code() {
        FakePaymentGateway fakePaymentGateway = new FakePaymentGateway();
        PaymentQRCodeRequest input = new PaymentQRCodeRequest(
                UUID.randomUUID().toString(),
                "Test Order",
                "Order Description",
                new BigDecimal("100.00"),
                List.of(
                        new PaymentQRCodeItem("Item 1", "Description 1", new BigDecimal("50.00"), 1L, "unit", new BigDecimal("50.00")),
                        new PaymentQRCodeItem("Item 2", "Description 2", new BigDecimal("50.00"), 1L, "unit", new BigDecimal("50.00"))
                )
        );

        PaymentProcessorResponse response = fakePaymentGateway.createPaymentQRCode(input);

        assertNotNull(response);
        assertNotNull(response.getQrData());
        assertNotNull(response.getStoreOrderId());
    }
}