package br.com.fiap.totem_express.application.user.impl;

import br.com.fiap.totem_express.application.user.RetrieveUserUseCase;
import br.com.fiap.totem_express.application.user.UserGateway;
import br.com.fiap.totem_express.application.user.output.DefaultUserView;

import java.util.Optional;

public class RetrieveUserUseCaseImpl implements RetrieveUserUseCase {

    private final UserGateway gateway;

    public RetrieveUserUseCaseImpl(UserGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Optional<DefaultUserView> execute(String cpf) {
        return gateway
                .findByCPF(cpf)
                .map(user -> new DefaultUserView(user.getId(), user.getName()));
    }
}
