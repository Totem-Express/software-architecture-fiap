package br.com.fiap.totem_express.domain.payment;

import br.com.fiap.totem_express.shared.invariant.Invariant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.fiap.totem_express.domain.payment.Status.FAILED;
import static br.com.fiap.totem_express.domain.payment.Status.PAID;
import static br.com.fiap.totem_express.shared.invariant.Rule.notNull;
//TODO: teste
public class Payment {

    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();;
    private Status status = Status.PENDING;
    private String transactionId;
    private BigDecimal amount;
    private String qrCode;

    public Payment(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Status status, String transactionId, BigDecimal amount, String qrCode) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.transactionId = transactionId;
        this.amount = amount;
        this.qrCode = qrCode;
    }

    public Payment(BigDecimal amount) {
        Invariant.of(amount, notNull("Payment amount must be not null"));
        this.amount = amount;
        this.transactionId = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void processPayment(Status status) {
        Invariant.of(status, notNull("Payment status must be not null"));
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
