package br.com.fiap.totem_express.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.totem_express.domain.product.Product;
import br.com.fiap.totem_express.infrastructure.product.*;

//TODO: teste
public class OrderItem {
    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Order order;
    private Product product;
    private Long quantity;
    private BigDecimal total;

    public OrderItem(LocalDateTime createdAt, ProductEntity product, Order order, Long quantity) {
        this.createdAt = createdAt;
        this.product = product.toDomain();
        this.order = order;
        this.quantity = quantity;
        this.total = this.calculateTotal();
    }

    public OrderItem(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
        this.total = this.calculateTotal();
    }

    public OrderItem(Long id, LocalDateTime createdAt, ProductEntity product, Long quantity, BigDecimal total) {
        this.id = id;
        this.createdAt = createdAt;
        this.product = product.toDomain();
        this.quantity = quantity;
        this.total = this.calculateTotal();
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return getProduct().getName();
    }

    public BigDecimal getProductPrice() {
        return getProduct().getPrice();
    }

    public String getProductDescription() {
        return getProduct().getDescription();
    }

    public void setOrder(Order order) {
        if (order == null) throw new IllegalArgumentException("Order must not be null");
        this.order = order;
    }

    protected BigDecimal calculateTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
