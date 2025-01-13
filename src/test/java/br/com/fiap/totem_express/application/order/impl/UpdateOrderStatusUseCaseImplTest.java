package br.com.fiap.totem_express.application.order.impl;

import br.com.fiap.totem_express.application.order.OrderGateway;
import br.com.fiap.totem_express.application.order.output.OrderView;
import br.com.fiap.totem_express.domain.order.Order;
import br.com.fiap.totem_express.domain.order.OrderItem;
import br.com.fiap.totem_express.domain.order.Status;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import br.com.fiap.totem_express.infrastructure.product.ProductEntity;
import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UpdateOrderStatusUseCaseImplTest {

    @Test
    void change_status_should_update_order_status_successfully() {
        Long orderId = 1L;

        Product product = new Product(1L, "Product 1", "Description 1", "image.png", BigDecimal.valueOf(10.0), Category.DISH);
        Order order = spy(new Order(Set.of(new OrderItem(1L, LocalDateTime.now(), new ProductEntity(product), 1L, BigDecimal.TEN)), empty()));
        ReflectionTestUtils.setField(order, "id", orderId);
        OrderGateway gateway = mock(OrderGateway.class);
        when(gateway.findById(orderId)).thenReturn(Optional.of(order));

        UpdateOrderStatusUseCaseImpl useCase = new UpdateOrderStatusUseCaseImpl(gateway);

        OrderView result = useCase.changeStatus(orderId);

        verify(order).goToNextStep();
        verify(gateway).changeStatus(order);

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(Status.PREPARING);
        assertThat(result.id()).isEqualTo(orderId);
    }

    @Test
    void change_status_should_throw_invariant_exception_when_order_not_found() {
        Long orderId = 2L;
        OrderGateway gateway = mock(OrderGateway.class);
        when(gateway.findById(orderId)).thenReturn(Optional.empty());

        UpdateOrderStatusUseCaseImpl useCase = new UpdateOrderStatusUseCaseImpl(gateway);

        assertThatThrownBy(() -> useCase.changeStatus(orderId))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Order<2> must exists to update status");

        verify(gateway, never()).changeStatus(any(Order.class));
    }
}