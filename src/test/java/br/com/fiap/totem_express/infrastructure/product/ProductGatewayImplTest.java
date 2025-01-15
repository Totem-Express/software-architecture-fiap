package br.com.fiap.totem_express.infrastructure.product;

import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.fiap.totem_express.domain.product.Category.DISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductGatewayImplTest {

    private ProductRepository repository;

    private ProductGatewayImpl productGateway;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        productGateway = new ProductGatewayImpl(repository);
    }

    @Test
    void should_save_product() {
        Product product = new Product(1L, "Product 1", "Description 1", "image.png", BigDecimal.valueOf(10.0), DISH);
        ProductEntity productEntity = new ProductEntity(product);

        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);

        Product savedProduct = productGateway.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        verify(repository).save(any(ProductEntity.class));
    }

    @Test
    void should_delete_product() {
        Long productId = 1L;
        when(repository.existsById(productId)).thenReturn(true);
        productGateway.delete(productId);
        verify(repository).deleteById(productId);
    }

    @Test
    void should_throw_exception_when_deleting_non_existent_product() {
        Long productId = 1L;

        when(repository.existsById(productId)).thenReturn(false);

        var exception = assertThrows(IllegalArgumentException.class, () -> productGateway.delete(productId));
        assertThat(exception.getMessage()).isEqualTo("Product with id: %s not found".formatted(productId));
    }

    @Test
    void should_update_product() {
        Product product = new Product(1L, "Product 1", "Description 1", "image.png", BigDecimal.valueOf(10.0), DISH);
        ProductEntity productEntity = new ProductEntity(product);

        when(repository.findById(product.getId())).thenReturn(Optional.of(productEntity));
        when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);

        Product updatedProduct = productGateway.update(product);

        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getId()).isEqualTo(product.getId());
        verify(repository).findById(product.getId());
        verify(repository).save(any(ProductEntity.class));
    }

    @Test
    void should_throw_exception_when_updating_non_existent_product() {
        Product product = new Product(1L, "Product 1", "Description 1", "image.png", BigDecimal.valueOf(10.0), DISH);

        when(repository.findById(product.getId())).thenReturn(Optional.empty());

        var exception = assertThrows(IllegalArgumentException.class, () -> productGateway.update(product));
        assertThat(exception.getMessage()).isEqualTo("Product not found with id: " + product.getId());
    }

    @Test
    void should_find_all_products_by_ids() {
        Set<Long> ids = Set.of(1L, 2L);
        ProductEntity productEntity1 = new ProductEntity(new Product(1L, "Product 1", "Description 1", "image.png", BigDecimal.valueOf(10.0), DISH));
        ProductEntity productEntity2 = new ProductEntity(new Product(2L, "Product 2", "Description 2", "image.png", BigDecimal.valueOf(20.0), Category.DRINK));

        when(repository.findAllById(ids)).thenReturn(List.of(productEntity1, productEntity2));

        List<Product> products = productGateway.findAllByIds(ids);

        assertThat(products).hasSize(2);
        verify(repository).findAllById(ids);
    }

    @Test
    void should_find_product_by_id() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity(new Product(productId, "Product 1", "Description 1", "image.png", BigDecimal.valueOf(10.0), DISH));

        when(repository.findById(productId)).thenReturn(Optional.of(productEntity));

        Optional<Product> product = productGateway.findById(productId);

        assertThat(product).isPresent();
        assertThat(product.get().getId()).isEqualTo(productId);
        verify(repository).findById(productId);
    }

    @Test
    void should_find_all_products_by_category() {
        ProductEntity productEntity1 = new ProductEntity(new Product(1L, "Product 1", "Description 1", "image.png", new BigDecimal("19.90"), DISH));
        ProductEntity productEntity2 = new ProductEntity(new Product(2L, "Product 2", "Description 2", "image.png", new BigDecimal("19.90"), DISH));

        when(repository.findAllByCategory(DISH)).thenReturn(List.of(productEntity1, productEntity2));

        List<Product> products = productGateway.findAllByCategory(DISH);

        assertThat(products).hasSize(2);
        verify(repository).findAllByCategory(DISH);
    }
}