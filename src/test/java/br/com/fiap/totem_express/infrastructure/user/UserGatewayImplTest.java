package br.com.fiap.totem_express.infrastructure.user;

import br.com.fiap.totem_express.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.totem_express.domain.user.Role.ADMIN;
import static br.com.fiap.totem_express.domain.user.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserGatewayImplTest {

    private UserRepository repository;
    private UserGatewayImpl userGateway;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        userGateway = new UserGatewayImpl(repository);
    }

    @Test
    void should_create_user() {
        User user = new User(1L, "Brad Pitt", "bradpitt@email.com", "114.974.750-15", LocalDateTime.now(), ADMIN);
        UserEntity userEntity = new UserEntity(user);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);

        User createdUser = userGateway.create(user);

        assertThat(createdUser.getName()).isEqualTo(user.getName());
    }

    @Test
    void should_find_all_users() {
        UserEntity userEntity1 = new UserEntity(new User(1L, "Brad Pitt", "bradpitt@email.com", "114.974.750-15", LocalDateTime.now(), USER));
        UserEntity userEntity2 = new UserEntity(new User(2L, "Angelina Jolie", "angelina@email.com", "936.038.500-09", LocalDateTime.now(), ADMIN));
        when(repository.findAll()).thenReturn(List.of(userEntity1, userEntity2));

        List<User> users = userGateway.findAll();
        assertThat(users).size().isEqualTo(2);
    }

    @Test
    void should_check_if_user_exists_by_id() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean exists = userGateway.existsById(1L);

        assertThat(exists).isTrue();
    }

    @Test
    void should_check_if_user_exists_by_email_or_cpf() {
        when(repository.existsByEmailOrCpf("bradpitt@email.com", "114.974.750-15")).thenReturn(true);
        boolean exists = userGateway.existsByEmailOrCPF("bradpitt@email.com", "114.974.750-15");

        assertThat(exists).isTrue();
    }

    @Test
    void should_find_user_by_id() {
        UserEntity userEntity = new UserEntity(new User(1L, "Brad Pitt", "bradpitt@email.com", "114.974.750-15", LocalDateTime.now(), USER));
        when(repository.findById(1L)).thenReturn(Optional.of(userEntity));

        Optional<User> user = userGateway.findById(1L);

        assertThat(user.get().getName()).isEqualTo("Brad Pitt");
    }

    @Test
    void should_find_user_by_cpf() {
        UserEntity userEntity = new UserEntity(new User(1L, "Brad Pitt", "bradpitt@email.com", "114.974.750-15", LocalDateTime.now(), USER));
        when(repository.findByCpf("114.974.750-15")).thenReturn(Optional.of(userEntity));

        Optional<User> user = userGateway.findByCPF("114.974.750-15");

        assertThat(user.get().getName()).isEqualTo("Brad Pitt");
    }
}