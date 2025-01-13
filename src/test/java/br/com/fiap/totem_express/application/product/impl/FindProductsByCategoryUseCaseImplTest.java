package br.com.fiap.totem_express.application.product.impl;

import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FindProductsByCategoryUseCaseImplTest {

    @Test
    void find_all_by_category_should_return_correct_product_views() {
        Category category = Category.DRINK;

        Product product1 = new Product(1L, "Coca-Cola", "A refreshing drink", "/images/coca-cola.jpg", BigDecimal.valueOf(5.99), category);
        Product product2 = new Product(2L, "Pepsi", "An alternative drink", "/images/pepsi.jpg", BigDecimal.valueOf(5.49), category);

        ProductGateway gateway = mock(ProductGateway.class);
        when(gateway.findAllByCategory(category)).thenReturn(List.of(product1, product2));

        FindProductsByCategoryUseCaseImpl useCase = new FindProductsByCategoryUseCaseImpl(gateway);

        List<ProductView> actualProductViews = useCase.findAllByCategory(category);

        assertThat(actualProductViews).hasSize(2);

        assertThat(actualProductViews.get(0))
                .extracting("id", "name", "description", "imagePath", "price", "category")
                .containsExactly(1L, "Coca-Cola", "A refreshing drink", "/images/coca-cola.jpg", BigDecimal.valueOf(5.99), category);

        assertThat(actualProductViews.get(1))
                .extracting("id", "name", "description", "imagePath", "price", "category")
                .containsExactly(2L, "Pepsi", "An alternative drink", "/images/pepsi.jpg", BigDecimal.valueOf(5.49), category);

        verify(gateway, times(1)).findAllByCategory(category);
    }

    @Test
    void find_all_by_category_should_return_empty_list_when_no_products_found() {
        Category category = Category.DESSERT;

        ProductGateway gateway = mock(ProductGateway.class);
        when(gateway.findAllByCategory(category)).thenReturn(List.of());

        FindProductsByCategoryUseCaseImpl useCase = new FindProductsByCategoryUseCaseImpl(gateway);

        List<ProductView> actualProductViews = useCase.findAllByCategory(category);

        assertThat(actualProductViews).isEmpty();

        verify(gateway, times(1)).findAllByCategory(category);
    }

    @Test
    void find_all_by_category_should_throw_exception_when_category_is_null() {
        ProductGateway gateway = mock(ProductGateway.class);

        FindProductsByCategoryUseCaseImpl useCase = new FindProductsByCategoryUseCaseImpl(gateway);

        assertThatThrownBy(() -> useCase.findAllByCategory(null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Category must not be null");

        verifyNoInteractions(gateway);
    }
}