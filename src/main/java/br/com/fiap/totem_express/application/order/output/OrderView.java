package br.com.fiap.totem_express.application.order.output;

import br.com.fiap.totem_express.application.user.output.*;
import br.com.fiap.totem_express.domain.order.*;

import java.math.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public record OrderView(LocalDateTime createdAt, LocalDateTime updatedAt, Set<OrderItemView> items,
                        BigDecimal total, Status status, Long id, DefaultUserView possibleUserView) {
    public OrderView(Order order) {
        this(
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getItems().stream().map(OrderItemView::new).collect(Collectors.toSet()),
                order.getTotal(),
                order.getStatus(),
                order.getId(),
                order.getPossibleUser().map(user -> new DefaultUserView(user.getId(), user.getName())).orElse(null)
        );
    }
}
