package br.com.fiap.totem_express.infrastructure.order;

import br.com.fiap.totem_express.application.order.*;
import br.com.fiap.totem_express.domain.order.*;
import br.com.fiap.totem_express.infrastructure.payment.PaymentEntity;
import br.com.fiap.totem_express.infrastructure.payment.PaymentRepository;

import java.util.*;

public class OrderGatewayImpl implements OrderGateway {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderGatewayImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void changeStatus(Order current) {
        orderRepository.updateStatus(current);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id).map(OrderEntity::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllWithDeletedProducts().stream().map(orderEntity -> {
            Order orderDomain = orderEntity.toDomain();
            return orderDomain;
        }).toList();
    }

    @Override
    public Order create(Order domain) {
        if (domain.getPayment() != null) {
            PaymentEntity paymentEntity = new PaymentEntity(domain.getPayment());
            paymentRepository.save(paymentEntity);
            domain.setPayment(paymentEntity.toDomain());
        }

        OrderEntity savedOrderEntity = orderRepository.save(new OrderEntity(domain));
        Order savedOrderDomain = savedOrderEntity.toDomain();
        savedOrderEntity.getItems().forEach(orderItemEntity -> {
            savedOrderDomain.addItem(orderItemEntity.toDomain());
        });
        return savedOrderDomain;
    }

}
