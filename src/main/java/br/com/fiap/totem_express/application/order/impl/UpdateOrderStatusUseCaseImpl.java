package br.com.fiap.totem_express.application.order.impl;

import br.com.fiap.totem_express.application.order.OrderGateway;
import br.com.fiap.totem_express.application.order.UpdateOrderStatusUseCase;
import br.com.fiap.totem_express.application.order.output.OrderView;
import br.com.fiap.totem_express.shared.invariant.InvariantException;

public class UpdateOrderStatusUseCaseImpl implements UpdateOrderStatusUseCase {

    private final OrderGateway gateway;

    public UpdateOrderStatusUseCaseImpl(OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public OrderView changeStatus(Long id) {
        final var order = gateway
                .findById(id)
                .orElseThrow(() ->  new InvariantException("Order<%s> must exists to update status".formatted(id)));

        order.goToNextStep();
        gateway.changeStatus(order);

        return new OrderView(order);
    }
}
