package br.com.fiap.totem_express.infrastructure.payment;

import br.com.fiap.totem_express.application.payment.PaymentGateway;
import br.com.fiap.totem_express.domain.payment.Payment;

import java.util.Optional;

public class PaymentGatewayImpl implements PaymentGateway {

    private final PaymentRepository repository;

    public PaymentGatewayImpl(PaymentRepository paymentRepository) {
        this.repository = paymentRepository;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return repository.findById(id).map(PaymentEntity::toDomain);
    }

    @Override
    public Payment create(Payment payment) {
        PaymentEntity save = repository.save(new PaymentEntity(payment));
        Payment domain = save.toDomain();
        return domain;
    }
}
