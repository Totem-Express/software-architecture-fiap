package br.com.fiap.totem_express.presentation.product.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.totem_express.application.product.input.UpdateProductInput;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

//TODO: teste
public record UpdateProductRequest(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String imagePath,
        @Positive BigDecimal price,
        @NotNull Category category) implements UpdateProductInput {

    @Override
    public Product toDomain() {
        return new Product(id, name, description, imagePath, price, category, LocalDateTime.now());
    }
}
