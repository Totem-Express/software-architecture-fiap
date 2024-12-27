package br.com.fiap.totem_express.presentation.user.requests;

import br.com.fiap.totem_express.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRequestTest {

    @Test
    void should_create_model_correctly() {
        CreateUserRequest createUserRequest = new CreateUserRequest("Name Name", "email@email.com", "114.974.750-15");
        assertThat(createUserRequest.toDomain()).extracting(
                User::getName, User::getEmail, User::getCpf
        ).containsExactly("Name Name", "email@email.com", "114.974.750-15");
    }
}