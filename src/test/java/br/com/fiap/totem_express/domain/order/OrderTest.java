package br.com.fiap.totem_express.domain.order;

import br.com.fiap.totem_express.domain.payment.Payment;
import br.com.fiap.totem_express.domain.user.User;
import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTest {

    @Test
    void set_total_should_calculate_total_when_items_are_present() {
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        when(item1.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        when(item2.getTotal()).thenReturn(BigDecimal.valueOf(50.00));
        Set<OrderItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), items, null);

        assertThat(order.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
    }

    @Test
    void set_total_should_update_correctly_when_items_change() {
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        when(item1.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        when(item2.getTotal()).thenReturn(BigDecimal.valueOf(200.00));

        Set<OrderItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), items, null);

        assertThat(order.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(300.00));

        OrderItem item3 = mock(OrderItem.class);
        when(item3.getTotal()).thenReturn(BigDecimal.valueOf(150.00));
        items.add(item3);

        order.setItems(items);

        assertThat(order.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(450.00));
    }

    @Test
    void constructor_with_valid_inputs_should_create_order() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Set<OrderItem> items = new HashSet<>();
        OrderItem item = mock(OrderItem.class);
        when(item.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        items.add(item);
        User user = new User("John", "john@example.com", "235.540.770-38");

        Order order = new Order(createdAt, updatedAt, items, user);

        assertThat(order.getCreatedAt()).isEqualTo(createdAt);
        assertThat(order.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(order.getItems()).containsExactlyElementsOf(items);
        assertThat(order.getPossibleUser()).contains(user);
    }

    @Test
    void constructor_should_throw_when_created_at_is_null() {
        LocalDateTime updatedAt = LocalDateTime.now();
        Set<OrderItem> items = new HashSet<>();

        assertThatThrownBy(() -> new Order(null, updatedAt, items, null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Order created at must be not null");
    }

    @Test
    void constructor_should_throw_when_updated_at_is_null() {
        LocalDateTime createdAt = LocalDateTime.now();
        Set<OrderItem> items = new HashSet<>();
        assertThatThrownBy(() -> new Order(createdAt, null, items, null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Order updated at must be not null");
    }

    @Test
    void constructor_should_throw_when_items_are_empty() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        assertThatThrownBy(() -> new Order(createdAt, updatedAt, Collections.emptySet(), null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Order item must be be not empty");
    }

    @Test
    void constructor_should_throw_when_items_are_null() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        assertThatThrownBy(() -> new Order(createdAt, updatedAt, null, null))
                .isInstanceOf(InvariantException.class);
    }

    @Test
    void constructor_with_null_user_should_create_order() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Set<OrderItem> items = new HashSet<>();
        OrderItem item = mock(OrderItem.class);
        when(item.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        items.add(item);

        Order order = new Order(createdAt, updatedAt, items, null);

        assertThat(order.getItems()).containsExactlyElementsOf(items);
        assertThat(order.getPossibleUser()).isEmpty();
    }

    @Test
    void should_go_to_next_step() {
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        when(item1.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        when(item2.getTotal()).thenReturn(BigDecimal.valueOf(50.00));
        Set<OrderItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), items, null);
        order.goToNextStep();
        assertThat(order.getStatus()).isEqualTo(Status.PREPARING);
    }

    @Test
    void should_add_item() {
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        when(item1.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        when(item2.getTotal()).thenReturn(BigDecimal.valueOf(50.00));
        Set<OrderItem> items = new HashSet<>();
        items.add(item1);
        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), items, null);
        assertThat(order.getItems()).containsExactlyInAnyOrder(item1);


        order.addItem(item2);
        assertThat(order.getItems()).containsExactlyInAnyOrder(item1, item2);
    }

    @Test
    void should_set_Payment() {
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        when(item1.getTotal()).thenReturn(BigDecimal.valueOf(100.00));
        when(item2.getTotal()).thenReturn(BigDecimal.valueOf(50.00));
        Set<OrderItem> items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        Order order = new Order(LocalDateTime.now(), LocalDateTime.now(), items, null);
        assertThat(order.getPayment()).isNull();
        Payment payment = new Payment(BigDecimal.valueOf(100.00));
        order.setPayment(payment);
        assertThat(order.getPayment()).isEqualTo(payment);
    }
}