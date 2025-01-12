package br.com.fiap.totem_express.application.payment.impl;

import br.com.fiap.totem_express.application.payment.PaymentGateway;
import br.com.fiap.totem_express.application.payment.ProcessPaymentWebhookUseCase;
import br.com.fiap.totem_express.application.payment.input.PaymentWebhookInput;
import br.com.fiap.totem_express.domain.payment.Payment;

public class ProcessPaymentWebhookUseCaseImpl implements ProcessPaymentWebhookUseCase {

    private final PaymentGateway gateway;

    public ProcessPaymentWebhookUseCaseImpl(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void process(Long paymentId, PaymentWebhookInput input) {
        Payment payment = gateway.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment must exists invalid id " + paymentId));

        payment.processPayment(input.status());
        gateway.create(payment);
    }
}