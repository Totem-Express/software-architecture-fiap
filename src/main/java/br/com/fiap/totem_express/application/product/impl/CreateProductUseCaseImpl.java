package br.com.fiap.totem_express.application.product.impl;

import br.com.fiap.totem_express.application.product.CreateProductUseCase;
import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.product.input.NewProductInput;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Product;


public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final ProductGateway gateway;

    public CreateProductUseCaseImpl(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public ProductView create(NewProductInput input) {
        Product domain = input.toDomain();
        Product product = gateway.save(domain);

        return new ProductView.SimpleView(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImagePath(),
                product.getPrice(),
                product.getCategory()
        );
    }

}
