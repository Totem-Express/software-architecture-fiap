package br.com.fiap.totem_express.application.order.input;

import br.com.fiap.totem_express.application.user.*;
import br.com.fiap.totem_express.domain.order.Order;
import br.com.fiap.totem_express.domain.order.OrderItem;

import java.util.Optional;
import java.util.Set;

public interface CreateOrderInput {
    Set<OrderItemInput> orderItems();

    Optional<Long> possibleUserId();

    Order toDomain(Set<OrderItem> orderItemsDomain, UserGateway userGateway);
}
