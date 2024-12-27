package br.com.fiap.totem_express.infrastructure.order;

import br.com.fiap.totem_express.domain.order.*;
import br.com.fiap.totem_express.domain.user.*;
import br.com.fiap.totem_express.infrastructure.payment.PaymentEntity;
import br.com.fiap.totem_express.infrastructure.user.*;
import jakarta.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

//TODO: teste
@Entity(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    @NotEmpty
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderItemEntity> items = new HashSet<>();

    @NotNull
    private BigDecimal total;

    @ManyToOne
    @Nullable
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Status status = Status.RECEIVED;

    @OneToOne
    private PaymentEntity payment;

    @Deprecated
    public OrderEntity() {
    }

    public OrderEntity(Order order) {
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.total = order.getTotal();
        this.user = order.getPossibleUser().map(UserEntity::new).orElse(null);
        this.items = order.getItems().stream().map(item -> new OrderItemEntity(item, this)).collect(Collectors.toSet());
        this.payment = order.getPayment() != null ? new PaymentEntity(order.getPayment()) : null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<OrderItemEntity> getItems() {
        return items;
    }

    public Order toDomain() {
        var order = new Order(
                id,
                createdAt,
                updatedAt,
                total,
                Optional.ofNullable(user).map(UserEntity::toDomain).orElse(null),
                status,
                payment.toDomain()
        );

        final var orderItems = items.stream().map(item -> item.toDomain(order)).collect(Collectors.toSet());

        order.setItems(orderItems);

        return order;
    }
}
