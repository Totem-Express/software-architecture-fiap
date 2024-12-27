package br.com.fiap.totem_express.infrastructure.order;

import br.com.fiap.totem_express.domain.order.*;
import br.com.fiap.totem_express.infrastructure.product.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.*;
import java.time.*;

//TODO: teste
@Entity(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime createdAt =  LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    private ProductEntity product;

    @Min(1)
    private Long quantity;

    @NotNull
    private BigDecimal price;

    @Deprecated
    public OrderItemEntity() {
    }

    public OrderItemEntity(OrderItem item) {
        this.id = item.getId();
        this.createdAt = item.getCreatedAt();
        this.product = new ProductEntity(item.getProduct());
        this.quantity = item.getQuantity();
        this.price = item.getTotal();
    }

    public OrderItemEntity(OrderItem item, OrderEntity orderEntity) {
        this(item);
        order = orderEntity;
    }

    public Long getId() {
        return id;
    }

    public OrderItem toDomain() {
        Order orderDomain = order.toDomain();
        OrderItem orderItem = new OrderItem(id, createdAt, product, quantity, price);
        orderDomain.addItem(orderItem);
        orderItem.setOrder(orderDomain);
        return orderItem;
    }

    public OrderItem toDomain(Order order) {
        final var item = new OrderItem(id, createdAt, product, quantity, price);
        item.setOrder(order);
        return item;
    }
}
