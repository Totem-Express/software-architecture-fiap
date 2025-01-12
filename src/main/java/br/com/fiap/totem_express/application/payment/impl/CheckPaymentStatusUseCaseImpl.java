package br.com.fiap.totem_express.application.payment.impl;

import br.com.fiap.totem_express.application.payment.CheckPaymentStatusUseCase;
import br.com.fiap.totem_express.application.payment.PaymentGateway;
import br.com.fiap.totem_express.application.payment.output.PaymentView;
import br.com.fiap.totem_express.domain.payment.Payment;

public class CheckPaymentStatusUseCaseImpl implements CheckPaymentStatusUseCase {

    private final PaymentGateway gateway;

    public CheckPaymentStatusUseCaseImpl(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public PaymentView checkStatus(Long paymentId) {
        Payment payment = gateway.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment must exists invalid id " + paymentId));
        return new PaymentView.SimpleView(payment.getId(), payment.getStatus(), payment.getQrCode());
    }
}
