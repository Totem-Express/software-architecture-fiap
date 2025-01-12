package br.com.fiap.totem_express.application.order.impl;

import br.com.fiap.totem_express.application.order.OrderGateway;
import br.com.fiap.totem_express.application.order.output.OrderView;
import br.com.fiap.totem_express.domain.order.*;
import br.com.fiap.totem_express.domain.payment.Payment;
import br.com.fiap.totem_express.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.fiap.totem_express.domain.order.Status.PREPARING;
import static br.com.fiap.totem_express.domain.order.Status.RECEIVED;
import static br.com.fiap.totem_express.domain.payment.Status.PENDING;
import static br.com.fiap.totem_express.domain.user.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListOrderUseCaseImplTest {

    private OrderGateway gateway;

    private ListOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(OrderGateway.class);
        useCase = new ListOrderUseCaseImpl(gateway);
    }

    @Test
    void should_return_list_of_order_views() {
        Order order1 = createOrder(1L, RECEIVED);
        Order order2 = createOrder(2L, PREPARING);

        when(gateway.findAll()).thenReturn(List.of(order1, order2));

        List<OrderView> result = useCase.execute();

        OrderView orderView1 = result.get(0);
        assertThat(orderView1.id()).isEqualTo(order1.getId());
        assertThat(orderView1.status()).isEqualTo(order1.getStatus());
        assertThat(orderView1.total()).isEqualTo(order1.getTotal());

        OrderView orderView2 = result.get(1);
        assertThat(orderView2.id()).isEqualTo(order2.getId());
        assertThat(orderView2.status()).isEqualTo(order2.getStatus());
        assertThat(orderView2.total()).isEqualTo(order2.getTotal());
    }

    private Order createOrder(Long id, Status status) {
        var user = new User("Jack Dawson", "jackdawson@outlook.com", "114.974.750-15", USER);

        var payment = new Payment(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                PENDING,
                "TXN123",
                new BigDecimal("99.99"),
                "QRCode123"
        );

        return new Order(
                id,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new BigDecimal("20.00"),
                user,
                status,
                payment
        );
    }
}