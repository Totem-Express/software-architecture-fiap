package br.com.fiap.totem_express.application.product.impl;

import java.util.Optional;

import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.product.UpdateProductUseCase;
import br.com.fiap.totem_express.application.product.input.UpdateProductInput;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Product;

public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

    private final ProductGateway gateway;

    public UpdateProductUseCaseImpl(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Optional<ProductView> update(UpdateProductInput input) {
        Optional<Product> possibleProduct = gateway.findById(input.id());
        if (possibleProduct.isEmpty()) return Optional.empty();

        Product product = possibleProduct.get();
        product.update(input);
        Product save = gateway.save(product);

        return Optional.of(new ProductView.SimpleView(
                save.getId(),
                save.getName(),
                save.getDescription(),
                save.getImagePath(),
                save.getPrice(),
                save.getCategory()));
    }
}