package br.com.fiap.totem_express.application.order.impl;

import br.com.fiap.totem_express.application.order.ListOrderUseCase;
import br.com.fiap.totem_express.application.order.OrderGateway;
import br.com.fiap.totem_express.application.order.output.OrderView;

import java.util.List;

public class ListOrderUseCaseImpl implements ListOrderUseCase {

    private final OrderGateway orderGateway;

    public ListOrderUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public List<OrderView> execute() {
        return orderGateway.findAll().stream().map(OrderView::new).toList();
    }
}
