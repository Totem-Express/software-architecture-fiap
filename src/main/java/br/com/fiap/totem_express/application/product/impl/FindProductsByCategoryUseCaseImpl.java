package br.com.fiap.totem_express.application.product.impl;

import java.util.List;

import static java.util.stream.Collectors.toList;

import br.com.fiap.totem_express.application.product.FindProductsByCategoryUseCase;
import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.domain.product.Product;

//TODO: teste
public class FindProductsByCategoryUseCaseImpl implements FindProductsByCategoryUseCase {

    private final ProductGateway gateway;

    public FindProductsByCategoryUseCaseImpl(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public List<ProductView> findAllByCategory(Category category) {
        List<Product> products = gateway.findAllByCategory(category);

        return products.stream()
                .map(product -> new ProductView.SimpleView(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getImagePath(),
                        product.getPrice(),
                        product.getCategory())
                ).collect(toList());
    }

}
