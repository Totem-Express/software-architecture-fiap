package br.com.fiap.totem_express.infrastructure.user;

import br.com.fiap.totem_express.application.user.UserGateway;
import br.com.fiap.totem_express.domain.user.User;

import java.util.*;

//TODO: teste
public class UserGatewayImpl implements UserGateway {
    private final UserRepository repository;

    public UserGatewayImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        final var created = repository.save(new UserEntity(user));
        return created.toDomain();
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(UserEntity::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmailOrCPF(String email, String cpf) {
        return repository.existsByEmailOrCpf(email, cpf);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByCPF(String cpf) {
        return repository.findByCpf(cpf).map(UserEntity::toDomain);
    }
}
