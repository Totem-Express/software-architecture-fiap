package br.com.fiap.totem_express.application.user.impl;

import br.com.fiap.totem_express.application.user.CreateUserUseCase;
import br.com.fiap.totem_express.application.user.UserGateway;
import br.com.fiap.totem_express.application.user.input.NewUserInput;
import br.com.fiap.totem_express.application.user.output.DefaultUserView;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserGateway gateway;

    public CreateUserUseCaseImpl(UserGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public DefaultUserView execute(NewUserInput input) {

        if (gateway.existsByEmailOrCPF(input.email(), input.cpf())) {
            throw new RuntimeException("User already exists");
        }

        final var domain = input.toDomain();

        final var created = gateway.create(domain);

        return new DefaultUserView(
                created.getId(),
                created.getName()
        );
    }
}
