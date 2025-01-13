package br.com.fiap.totem_express.application.product.impl;

import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.product.input.UpdateProductInput;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Product;
import br.com.fiap.totem_express.domain.product.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static br.com.fiap.totem_express.domain.product.Category.DISH;
import static org.assertj.core.api.Assertions.assertThat;

class UpdateProductUseCaseImplTest {

    @Test
    void update_should_return_empty_optional_when_product_does_not_exist() {
        ProductGateway gateway = new InMemoryProductGateway();
        UpdateProductUseCaseImpl useCase = new UpdateProductUseCaseImpl(gateway);

        UpdateProductInput input = new UpdateProductInput() {
            @Override
            public Long id() {
                return 1L;
            }

            @Override
            public String name() {
                return "Non-existent Product";
            }

            @Override
            public String description() {
                return "Product does not exist";
            }

            @Override
            public String imagePath() {
                return "image/path.jpg";
            }

            @Override
            public BigDecimal price() {
                return BigDecimal.TEN;
            }

            @Override
            public Category category() {
                return DISH;
            }

            @Override
            public Product toDomain() {
                return new Product(id(), name(), description(), imagePath(), price(), category());
            }
        };

        Optional<ProductView> result = useCase.update(input);

        assertThat(result).isEmpty();
    }

    @Test
    void update_should_update_product_when_product_exists() {
        ProductGateway gateway = new InMemoryProductGateway();
        Product existingProduct = new Product(1L, "Existing Product", "Description", "image.jpg", BigDecimal.valueOf(50.00), Category.SIDE_DISH);
        gateway.update(existingProduct);

        UpdateProductUseCaseImpl useCase = new UpdateProductUseCaseImpl(gateway);

        UpdateProductInput input = new UpdateProductInput() {
            @Override
            public Long id() {
                return 1L;
            }

            @Override
            public String name() {
                return "Updated Product";
            }

            @Override
            public String description() {
                return "Updated Description";
            }

            @Override
            public String imagePath() {
                return "Updated Image";
            }

            @Override
            public BigDecimal price() {
                return BigDecimal.valueOf(100.00);
            }

            @Override
            public Category category() {
                return DISH;
            }

            @Override
            public Product toDomain() {
                return new Product(id(), name(), description(), imagePath(), price(), category());
            }
        };

        Optional<ProductView> result = useCase.update(input);

        assertThat(result).isPresent();
        ProductView updatedProduct = result.get();
        assertThat(updatedProduct.id()).isEqualTo(1L);
        assertThat(updatedProduct.name()).isEqualTo("Updated Product");
        assertThat(updatedProduct.description()).isEqualTo("Updated Description");
        assertThat(updatedProduct.imagePath()).isEqualTo("Updated Image");
        assertThat(updatedProduct.price()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(updatedProduct.category()).isEqualTo(DISH);
    }

    static class InMemoryProductGateway implements ProductGateway {
        private Product product;

        @Override
        public Optional<Product> findById(Long id) {
            return Optional.ofNullable(product != null && product.getId().equals(id) ? product : null);
        }

        @Override
        public List<Product> findAllByCategory(Category category) {
            return List.of();
        }

        @Override
        public List<Product> findAllByIds(Set<Long> order) {
            return List.of();
        }

        @Override
        public Product save(Product product) {
            return null;
        }

        @Override
        public void delete(Long id) {

        }

        @Override
        public Product update(Product product) {
            this.product = product;
            return product;
        }
    }
}