package br.com.fiap.totem_express.application.user.impl;

import br.com.fiap.totem_express.application.user.UserGateway;
import br.com.fiap.totem_express.domain.user.User;
import br.com.fiap.totem_express.infrastructure.user.UserEntity;
import br.com.fiap.totem_express.infrastructure.user.UserRepository;
import br.com.fiap.totem_express.presentation.user.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static br.com.fiap.totem_express.domain.user.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateUserUseCaseImplTest {

    private UserGateway gateway;

    private CreateUserUseCaseImpl createUserUseCase;

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        gateway = mock(UserGateway.class);
        repository = mock(UserRepository.class);
        createUserUseCase = new CreateUserUseCaseImpl(gateway);
    }

    @Test
    void should_create_user_when_user_does_not_exist() {
        var name = "Rose DeWitt";
        var email = "rosewitt@email.com";
        var cpf = "114.974.750-15";

        var input = new CreateUserRequest(name, email, cpf);
        var createdUser = new User(1L, name, email, cpf, LocalDateTime.now(), USER);

        when(gateway.existsByEmailOrCPF(input.email(), input.cpf())).thenReturn(false);
        when(gateway.create(any(User.class))).thenReturn(new UserEntity(createdUser).toDomain());

        var result = createUserUseCase.execute(input);

        assertThat(result.id()).isEqualTo(createdUser.getId());
        assertThat(result.name()).isEqualTo(createdUser.getName());
    }

    @Test
    void should_throw_exception_when_user_already_exists() {
        var input = new CreateUserRequest("Rose DeWitt", "rosewitt@email.com", "114.974.750-15");

        when(gateway.existsByEmailOrCPF(input.email(), input.cpf())).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User already exists");
    }
}