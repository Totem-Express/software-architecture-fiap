package br.com.fiap.totem_express.application.order.output;

import br.com.fiap.totem_express.application.user.output.DefaultUserView;
import br.com.fiap.totem_express.domain.order.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OrderViewTest {

    @Test
    void constructor_should_create_instance_with_correct_values() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Set<OrderItemView> items = Collections.singleton(new OrderItemView("item1", 1L, BigDecimal.TEN));
        BigDecimal total = BigDecimal.TEN;
        Status status = Status.PREPARING;
        Long id = 1L;
        DefaultUserView userView = new DefaultUserView(1L, "John Doe");

        OrderView orderView = new OrderView(createdAt, updatedAt, items, total, status, id, userView);

        assertThat(orderView.createdAt()).isEqualTo(createdAt);
        assertThat(orderView.updatedAt()).isEqualTo(updatedAt);
        assertThat(orderView.items()).isEqualTo(items);
        assertThat(orderView.total()).isEqualTo(total);
        assertThat(orderView.status()).isEqualTo(status);
        assertThat(orderView.id()).isEqualTo(id);
        assertThat(orderView.possibleUserView()).isEqualTo(userView);
    }

    @Test
    void constructor_should_handle_null_user_view() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Set<OrderItemView> items = Collections.singleton(new OrderItemView("item1", 1L, BigDecimal.TEN));
        BigDecimal total = BigDecimal.TEN;
        Status status = Status.PREPARING;
        Long id = 1L;

        OrderView orderView = new OrderView(createdAt, updatedAt, items, total, status, id, null);

        assertThat(orderView.createdAt()).isEqualTo(createdAt);
        assertThat(orderView.updatedAt()).isEqualTo(updatedAt);
        assertThat(orderView.items()).isEqualTo(items);
        assertThat(orderView.total()).isEqualTo(total);
        assertThat(orderView.status()).isEqualTo(status);
        assertThat(orderView.id()).isEqualTo(id);
        assertThat(orderView.possibleUserView()).isNull();
    }
}