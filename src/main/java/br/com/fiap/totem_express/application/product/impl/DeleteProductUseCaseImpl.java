package br.com.fiap.totem_express.application.product.impl;

import br.com.fiap.totem_express.application.product.DeleteProductUseCase;
import br.com.fiap.totem_express.application.product.ProductGateway;

//TODO: teste
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private final ProductGateway gateway;

    public DeleteProductUseCaseImpl(ProductGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void delete(Long id) {
        gateway.delete(id);
    }

}
