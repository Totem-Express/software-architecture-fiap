package br.com.fiap.totem_express.infrastructure.payment;

import br.com.fiap.totem_express.domain.payment.Payment;
import br.com.fiap.totem_express.domain.payment.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentGatewayImplTest {

    private PaymentRepository repository;
    private PaymentGatewayImpl paymentGateway;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepository.class);
        paymentGateway = new PaymentGatewayImpl(repository);
    }

    @Test
    void should_find_payment_by_id() {
        Payment payment = new Payment(1L, null, null, Status.PENDING, "transactionId", new BigDecimal("100.00"), "qrCode");
        when(repository.findById(1L)).thenReturn(Optional.of(new PaymentEntity(payment)));

        Optional<Payment> result = paymentGateway.findById(1L);

        assertThat(result.get().getTransactionId()).isEqualTo(payment.getTransactionId());
        assertThat(result.get().getAmount()).isEqualTo(payment.getAmount());
    }

    @Test
    void should_create_payment() {
        Payment payment = new Payment(new BigDecimal("100.00"));
        PaymentEntity paymentEntity = new PaymentEntity(payment);
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);

        Payment result = paymentGateway.create(payment);

        assertThat(result.getTransactionId()).isEqualTo(payment.getTransactionId());
        assertThat(result.getAmount()).isEqualTo(payment.getAmount());
    }

}