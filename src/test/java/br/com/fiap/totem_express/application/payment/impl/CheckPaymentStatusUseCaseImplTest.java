package br.com.fiap.totem_express.application.payment.impl;

import br.com.fiap.totem_express.application.payment.PaymentGateway;
import br.com.fiap.totem_express.application.payment.output.PaymentView;
import br.com.fiap.totem_express.domain.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.fiap.totem_express.domain.payment.Status.PAID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;


class CheckPaymentStatusUseCaseImplTest {

    private PaymentGateway gateway;

    private CheckPaymentStatusUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(PaymentGateway.class);
        useCase = new CheckPaymentStatusUseCaseImpl(gateway);
    }

    @Test
    void should_return_payment_view_when_payment_exists() {
        Long paymentId = 1L;
        Payment payment = new Payment(
                paymentId,
                LocalDateTime.now(),
                LocalDateTime.now(),
                PAID,
                "TXN123",
                new BigDecimal("100.00"),
                "QRCode123");

        when(gateway.findById(paymentId)).thenReturn(Optional.of(payment));

        PaymentView result = useCase.checkStatus(paymentId);

        assertThat(result.id()).isEqualTo(payment.getId());
        assertThat(result.status()).isEqualTo(payment.getStatus());
        assertThat(result.qrCode()).isEqualTo(payment.getQrCode());
    }

    @Test
    void should_return_http_404_when_payment_does_not_exist() {
        Long paymentId = 1L;
        when(gateway.findById(paymentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                useCase.checkStatus(paymentId)
        );

        assertThat(exception.getMessage()).isEqualTo("Payment must exists invalid id " + paymentId);
        verify(gateway, times(1)).findById(paymentId);
    }

}