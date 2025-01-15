package br.com.fiap.totem_express.infrastructure.product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;

public class ProductGatewayImpl implements ProductGateway {

    private final ProductRepository repository;

    public ProductGatewayImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity savedEntity = repository.save(new ProductEntity(product));
        return savedEntity.toDomain();
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product with id: " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Product update(Product product) {
        Optional<ProductEntity> possibleProduct = repository.findById(product.getId());

        if (possibleProduct.isEmpty()) {
            throw new IllegalArgumentException("Product not found with id: " + product.getId());
        }

        ProductEntity productEntity = possibleProduct.get();
        productEntity.updateFromDomain(product);
        ProductEntity updatedEntity = repository.save(productEntity);

        return updatedEntity.toDomain();
    }

    @Override
    public List<Product> findAllByIds(Set<Long> order) {
        return repository.findAllById(order).stream().map(ProductEntity::toDomain).toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(ProductEntity::toDomain);
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        return repository.findAllByCategory(category).stream().map(ProductEntity::toDomain).toList();
    }

}
