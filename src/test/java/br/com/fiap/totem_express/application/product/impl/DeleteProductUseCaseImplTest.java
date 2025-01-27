package br.com.fiap.totem_express.application.product.impl;

import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class DeleteProductUseCaseImplTest {

    @Test
    void should_delete_product_successfully_when_product_exists() {
        Long productId = 1L;
        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        ProductGateway productGateway = mock(ProductGateway.class);
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        DeleteProductUseCaseImpl deleteProductUseCase = new DeleteProductUseCaseImpl(productGateway);

        deleteProductUseCase.delete(productId);

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, times(1)).delete(productId);
    }

    @Test
    void should_throw_exception_when_product_does_not_exist() {
        Long productId = 1L;

        ProductGateway productGateway = mock(ProductGateway.class);
        when(productGateway.findById(productId)).thenReturn(Optional.empty());
        DeleteProductUseCaseImpl deleteProductUseCase = new DeleteProductUseCaseImpl(productGateway);

        assertThatThrownBy(() -> deleteProductUseCase.delete(productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product must exists invalid id " + productId);

        verify(productGateway, times(1)).findById(productId);
        verify(productGateway, never()).delete(anyLong());
    }
}