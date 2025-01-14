package br.com.fiap.totem_express.domain.order;

import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import br.com.fiap.totem_express.infrastructure.product.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OrderItemTest {

    @Test
    void calculate_total_should_return_correct_total_for_given_quantity_and_price() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(10.50), Category.DISH);
        Long quantity = 3L;

        OrderItem orderItem = new OrderItem(product, quantity);

        assertThat(orderItem.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(31.50));
    }

    @Test
    void calculate_total_should_handle_large_quantities_correctly() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(15.75), Category.DISH);
        Long quantity = 1_000L;

        OrderItem orderItem = new OrderItem(product, quantity);

        assertThat(orderItem.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(15750.00));
    }

    @Test
    void calculate_total_should_handle_large_prices_correctly() {
        Product product = new Product(1L, "Expensive Product", "Expensive Description","image.png", BigDecimal.valueOf(10_000.00), Category.DISH);
        Long quantity = 2L;

        OrderItem orderItem = new OrderItem(product, quantity);

        assertThat(orderItem.getTotal()).isEqualByComparingTo(BigDecimal.valueOf(20_000.00));
    }

    @Test
    void should_set_order() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(10.50), Category.DISH);
        Long quantity = 3L;

        OrderItem orderItem = new OrderItem(product, quantity);
        assertThat(orderItem.getOrder()).isNull();
        Order order = mock(Order.class);
        orderItem.setOrder(order);
        assertThat(orderItem.getOrder()).isEqualTo(order);
    }

    @Test
    void should_throw_exception_if_order_is_null() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(10.50), Category.DISH);
        Long quantity = 3L;

        OrderItem orderItem = new OrderItem(product, quantity);
        assertThat(orderItem.getOrder()).isNull();

        assertThatIllegalArgumentException().isThrownBy(() -> orderItem.setOrder(null))
                .withMessage("Order must not be null");
    }

    @Test
    void should_create_order_item_with_all_fields_via_first_constructor() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(50), Category.DISH);
        LocalDateTime createdAt = LocalDateTime.now();
        ProductEntity productEntity = new ProductEntity(product);
        Order order = mock(Order.class);
        Long quantity = 2L;

        OrderItem orderItem = new OrderItem(createdAt, productEntity, order, quantity);

        assertThat(orderItem.getCreatedAt()).isEqualTo(createdAt);
        assertThat(orderItem.getProductName()).isEqualTo(product.getName());
        assertThat(orderItem.getProductDescription()).isEqualTo(product.getDescription());
        assertThat(orderItem.getProductPrice()).isEqualTo(product.getPrice());
        assertThat(orderItem.getOrder()).isEqualTo(order);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getTotal()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void should_create_order_item_with_product_and_quantity_via_second_constructor() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(100), Category.DISH);
        Long quantity = 3L;

        OrderItem orderItem = new OrderItem(product, quantity);

        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getTotal()).isEqualTo(BigDecimal.valueOf(300));
    }

    @Test
    void should_create_order_item_with_all_fields_including_id_via_third_constructor() {
        Product product = new Product(1L, "Test Product", "Description", "image.png", BigDecimal.valueOf(25), Category.DISH);
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        ProductEntity productEntity = new ProductEntity(product);
        Long quantity = 4L;

        OrderItem orderItem = new OrderItem(id, createdAt, productEntity, quantity);

        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getCreatedAt()).isEqualTo(createdAt);
        assertThat(orderItem.getProductName()).isEqualTo(product.getName());
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getTotal()).isEqualTo(BigDecimal.valueOf(100));
    }
}