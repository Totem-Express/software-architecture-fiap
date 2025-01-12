package br.com.fiap.totem_express.domain.payment;

import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static br.com.fiap.totem_express.domain.payment.Status.FAILED;
import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @Test
    void should_initialize_payment_with_amount() {
        var payment = new Payment(new BigDecimal("100.00"));

        assertThat(payment.getAmount()).isEqualTo("100.00");
        assertThat(payment.getTransactionId()).isNotNull();
    }

    @Test
    void should_return_exception_when_initialize_payment_with_amount_is_null() {
        assertThatThrownBy(() -> new Payment(null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Payment amount must be not null");
    }

    @Test
    void should_update_status_and_updatedAt_when_processPayment_called() {
        var payment = new Payment(new BigDecimal("29.90"));

        LocalDateTime beforeUpdate = LocalDateTime.now();
        payment.processPayment(FAILED);

        assertThat(payment.getStatus()).isEqualTo(FAILED);
        assertThat(payment.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    void should_throw_exception_when_processPayment_with_null_status() {
        var payment = new Payment(new BigDecimal("14.90"));

        assertThatThrownBy(() -> payment.processPayment(null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Payment status must be not null");
    }
}