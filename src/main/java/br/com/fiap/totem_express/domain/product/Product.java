package br.com.fiap.totem_express.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.fiap.totem_express.application.product.input.UpdateProductInput;
import br.com.fiap.totem_express.shared.invariant.Invariant;

import static br.com.fiap.totem_express.shared.invariant.Rule.*;

//TODO: teste
public class Product {

    private Long id;
    private String name;
    private String description;
    private String imagePath;
    private BigDecimal price;
    private Category category;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    public Product(String name, String description, String imagePath, BigDecimal price, Category category) {
        Invariant.of(name, notBlank("Product name must be not blank"));
        Invariant.of(description, notBlank("Product description must be not blank"));
        Invariant.of(imagePath, notBlank("Product image path must be not blank"));
        Invariant.of(price, gt(BigDecimal.ZERO, "Product price must be greater than 0"));
        Invariant.of(category, notNull("Product category must be not blank"));

        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.category = category;
    }

    public Product(Long id, String name, String description, String imagePath, BigDecimal price, Category category) {
        this(name, description, imagePath, price, category);
        this.id = id;
    }

    public Product(Long id, String name, String description, String imagePath, BigDecimal price, Category category, LocalDateTime updatedAt) {
        this(name, description, imagePath, price, category);
        this.id = id;
        this.updatedAt = updatedAt;
    }


    public Product(Long id, String name, String description, String imagePath, BigDecimal price, Category category, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(name, description, imagePath, price, category);

        Invariant.of(id, notNull("Product id must be not null"));

        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Category getCategory() {
        return category;
    }
    
    public void update(UpdateProductInput input) {
        this.id = input.id();
        this.name = input.name();
        this.description = input.description();
        this.imagePath = input.imagePath();
        this.price = input.price();
        this.category = input.category();
        this.updatedAt = LocalDateTime.now();
    }
}
