package br.com.fiap.totem_express.application.product.impl;

import br.com.fiap.totem_express.TestcontainersConfiguration;
import br.com.fiap.totem_express.application.product.CreateProductUseCase;
import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.product.input.NewProductInput;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CreateProductUseCaseImplTest {

    @Autowired
    private ProductGateway gateway;

    @Autowired
    private CreateProductUseCase createProductUseCase;

    @Test
    void should_create_product_successfully() {
        NewProductInput input = new NewProductInput() {
            @Override
            public String name() {
                return "Product Name";
            }

            @Override
            public String description() {
                return "Product Description";
            }

            @Override
            public String imagePath() {
                return "image/path.jpg";
            }

            @Override
            public BigDecimal price() {
                return BigDecimal.valueOf(19.99);
            }

            @Override
            public Category category() {
                return Category.DISH;
            }

            @Override
            public Product toDomain() {
                return new Product(
                        null,
                        name(),
                        description(),
                        imagePath(),
                        price(),
                        category()
                );
            }
        };

        ProductView result = createProductUseCase.create(input);

        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo(input.name());
        assertThat(result.description()).isEqualTo(input.description());
        assertThat(result.imagePath()).isEqualTo(input.imagePath());
        assertThat(result.price()).isEqualTo(input.price());
        assertThat(result.category()).isEqualTo(input.category());
    }
}