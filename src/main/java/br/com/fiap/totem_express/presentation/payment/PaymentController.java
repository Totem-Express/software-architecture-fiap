package br.com.fiap.totem_express.presentation.payment;

import br.com.fiap.totem_express.application.payment.CheckPaymentStatusUseCase;
import br.com.fiap.totem_express.application.payment.ProcessPaymentWebhookUseCase;
import br.com.fiap.totem_express.application.payment.input.PaymentWebhookInput;
import br.com.fiap.totem_express.application.payment.output.PaymentView;
import br.com.fiap.totem_express.presentation.payment.request.PaymentWebhookRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

//TODO: teste
@RestController
public class PaymentController implements PaymentDocumentation {

    private final CheckPaymentStatusUseCase checkPaymentStatusUseCase;
    private final ProcessPaymentWebhookUseCase processPaymentWebhookUseCase;

    public PaymentController(CheckPaymentStatusUseCase checkPaymentStatusUseCase, ProcessPaymentWebhookUseCase processPaymentWebhookUseCase) {
        this.checkPaymentStatusUseCase = checkPaymentStatusUseCase;
        this.processPaymentWebhookUseCase = processPaymentWebhookUseCase;
    }

    @Override
    @GetMapping("/api/payment/{id}")
    public ResponseEntity<PaymentView> checkPaymentStatus(@PathVariable Long id) {
        PaymentView check = checkPaymentStatusUseCase.checkStatus(id);
        return ResponseEntity.ok(check);
    }

    @Override
    @Transactional
    @PostMapping("/api/payment/process/{id}")
    public ResponseEntity<Void> processPayment(Long id, @RequestBody @Valid PaymentWebhookRequest input) {
        processPaymentWebhookUseCase.process(id, input);
        return ResponseEntity.ok().build();
    }
}
