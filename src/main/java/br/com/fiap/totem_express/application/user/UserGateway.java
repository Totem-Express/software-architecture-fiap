package br.com.fiap.totem_express.application.user;

import br.com.fiap.totem_express.domain.user.User;

import java.util.*;

public interface UserGateway {

    User create(User user);

    List<User> findAll();

    boolean existsByEmailOrCPF(String email, String cpf);

    Optional<User> findById(Long id);
}
