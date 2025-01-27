package br.com.fiap.totem_express.domain.product;

import br.com.fiap.totem_express.application.product.input.UpdateProductInput;
import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    void update_should_update_all_product_fields() {
        Long existingId = 1L;
        String initialName = "Original Name";
        String initialDescription = "Original Description";
        String initialImagePath = "/images/old.png";
        BigDecimal initialPrice = new BigDecimal("100.00");
        Category initialCategory = Category.DISH;
        LocalDateTime initialUpdatedAt = LocalDateTime.now().minusDays(1);

        String updatedName = "Updated Name";
        String updatedDescription = "Updated Description";
        String updatedImagePath = "/images/new.png";
        BigDecimal updatedPrice = new BigDecimal("150.00");
        Category updatedCategory = Category.SIDE_DISH;

        UpdateProductInput mockInput = Mockito.mock(UpdateProductInput.class);
        Mockito.when(mockInput.id()).thenReturn(existingId);
        Mockito.when(mockInput.name()).thenReturn(updatedName);
        Mockito.when(mockInput.description()).thenReturn(updatedDescription);
        Mockito.when(mockInput.imagePath()).thenReturn(updatedImagePath);
        Mockito.when(mockInput.price()).thenReturn(updatedPrice);
        Mockito.when(mockInput.category()).thenReturn(updatedCategory);

        Product product = new Product(existingId, initialName, initialDescription, initialImagePath, initialPrice, initialCategory, initialUpdatedAt);

        product.update(mockInput);

        assertThat(product.getId()).isEqualTo(existingId);
        assertThat(product.getName()).isEqualTo(updatedName);
        assertThat(product.getDescription()).isEqualTo(updatedDescription);
        assertThat(product.getImagePath()).isEqualTo(updatedImagePath);
        assertThat(product.getPrice()).isEqualTo(updatedPrice);
        assertThat(product.getCategory()).isEqualTo(updatedCategory);
        assertThat(product.getUpdatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isAfter(initialUpdatedAt);
    }

    @Test
    void update_should_not_change_updatedAt_if_no_fields_changed() {
        Long existingId = 1L;
        String initialName = "Original Name";
        String initialDescription = "Original Description";
        String initialImagePath = "/images/old.png";
        BigDecimal initialPrice = new BigDecimal("100.00");
        Category initialCategory = Category.DISH;
        LocalDateTime initialUpdatedAt = LocalDateTime.now().minusDays(1);

        UpdateProductInput mockInput = Mockito.mock(UpdateProductInput.class);
        Mockito.when(mockInput.id()).thenReturn(existingId);
        Mockito.when(mockInput.name()).thenReturn(initialName);
        Mockito.when(mockInput.description()).thenReturn(initialDescription);
        Mockito.when(mockInput.imagePath()).thenReturn(initialImagePath);
        Mockito.when(mockInput.price()).thenReturn(initialPrice);
        Mockito.when(mockInput.category()).thenReturn(initialCategory);

        Product product = new Product(existingId, initialName, initialDescription, initialImagePath, initialPrice, initialCategory, initialUpdatedAt);

        product.update(mockInput);

        assertThat(product.getId()).isEqualTo(existingId);
        assertThat(product.getName()).isEqualTo(initialName);
        assertThat(product.getDescription()).isEqualTo(initialDescription);
        assertThat(product.getImagePath()).isEqualTo(initialImagePath);
        assertThat(product.getPrice()).isEqualTo(initialPrice);
        assertThat(product.getCategory()).isEqualTo(initialCategory);
        assertThat(product.getUpdatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isAfter(initialUpdatedAt);
    }

    @Test
    void update_should_throw_exception_if_input_is_null() {

        Product product = new Product(1L, "Product Name", "Description", "/images/image.png", new BigDecimal("100.00"), Category.SIDE_DISH);

        assertThatThrownBy(() -> product.update(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void constructor_should_throw_exception_when_name_is_null_or_blank() {
        assertThatThrownBy(() -> new Product(null, "Description", "/path/image.png", new BigDecimal("100.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product name must be not blank");

        assertThatThrownBy(() -> new Product("", "Description", "/path/image.png", new BigDecimal("100.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product name must be not blank");
    }

    @Test
    void constructor_should_throw_exception_when_description_is_null_or_blank() {
        assertThatThrownBy(() -> new Product("Name", null, "/path/image.png", new BigDecimal("100.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product description must be not blank");

        assertThatThrownBy(() -> new Product("Name", "", "/path/image.png", new BigDecimal("100.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product description must be not blank");
    }

    @Test
    void constructor_should_throw_exception_when_image_path_is_null_or_blank() {
        assertThatThrownBy(() -> new Product("Name", "Description", null, new BigDecimal("100.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product image path must be not blank");

        assertThatThrownBy(() -> new Product("Name", "Description", "", new BigDecimal("100.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product image path must be not blank");
    }

    @Test
    void constructor_should_throw_exception_when_price_is_null_or_not_greater_than_zero() {
        assertThatThrownBy(() -> new Product("Name", "Description", "/path/image.png", null, Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product price must be greater than 0");

        assertThatThrownBy(() -> new Product("Name", "Description", "/path/image.png", BigDecimal.ZERO, Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product price must be greater than 0");

        assertThatThrownBy(() -> new Product("Name", "Description", "/path/image.png", new BigDecimal("-10.00"), Category.DISH))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product price must be greater than 0");
    }

    @Test
    void constructor_should_throw_exception_when_category_is_null() {
        assertThatThrownBy(() -> new Product("Name", "Description", "/path/image.png", new BigDecimal("100.00"), null))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Product category must be not blank");
    }

    @Test
    void constructor_should_create_product_when_all_fields_are_valid() {
        Product product = new Product("Name", "Description", "/path/image.png", new BigDecimal("100.00"), Category.DISH);

        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("Name");
        assertThat(product.getDescription()).isEqualTo("Description");
        assertThat(product.getImagePath()).isEqualTo("/path/image.png");
        assertThat(product.getPrice()).isEqualTo(new BigDecimal("100.00"));
        assertThat(product.getCategory()).isEqualTo(Category.DISH);
    }
}