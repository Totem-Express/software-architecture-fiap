package br.com.fiap.totem_express.infrastructure.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.SoftDelete;

//TODO: teste
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Column(name = "image_path")
    @NotBlank
    private String imagePath;

    @Positive
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @SoftDelete
    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Deprecated
    public ProductEntity() {
    }

    public ProductEntity(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.imagePath = product.getImagePath();
        this.price = product.getPrice();
        this.category = product.getCategory();
    }

    public void updateFromDomain(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.imagePath = product.getImagePath();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.updatedAt = LocalDateTime.now();
    }

    public Product toDomain() {
        return new Product(id, name, description, imagePath, price, category, createdAt, updatedAt);
    }
}
