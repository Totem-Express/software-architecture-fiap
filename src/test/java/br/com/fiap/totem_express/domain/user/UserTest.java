package br.com.fiap.totem_express.domain.user;

import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserTest {

    @Test
    void should_create_user_with_default_role() {
        User user = new User("Name Name", "email@email.com", "114.974.750-15");

        assertThat(user.getName()).isEqualTo("Name Name");
        assertThat(user.getEmail()).isEqualTo("email@email.com");
        assertThat(user.getCpf()).isEqualTo("114.974.750-15");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void should_create_user_with_specified_role() {
        User user = new User("Name Name", "email@email.com", "114.974.750-15", Role.ADMIN);

        assertThat(user.getName()).isEqualTo("Name Name");
        assertThat(user.getEmail()).isEqualTo("email@email.com");
        assertThat(user.getCpf()).isEqualTo("114.974.750-15");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void should_create_user_with_all_fields() {
        LocalDateTime createdAt = LocalDateTime.now();
        User user = new User(1L, "Name Name", "email@email.com", "114.974.750-15", createdAt, Role.USER);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Name Name");
        assertThat(user.getEmail()).isEqualTo("email@email.com");
        assertThat(user.getCpf()).isEqualTo("114.974.750-15");
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Nested
    class Validations{

        @NullAndEmptySource
        @ParameterizedTest
        void should_not_create_user_with_invalid_name(String name) {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(name, "email@email.com", "114.974.750-15"))
                    .withMessageContaining("User name must be not blank");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(1L, name, "email@email.com", "114.974.750-15", LocalDateTime.now(), Role.USER))
                    .withMessageContaining("User name must be not blank");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(name, "email@email.com", "114.974.750-15", Role.USER))
                    .withMessageContaining("User name must be not blank");
        }

        @NullAndEmptySource
        @ParameterizedTest
        void should_not_create_user_with_invalid_email(String email) {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", email, "114.974.750-15"))
                    .withMessageContaining("User email must be not blank");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(1L, "name", email, "114.974.750-15", LocalDateTime.now(), Role.USER))
                    .withMessageContaining("User email must be not blank");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", email, "114.974.750-15", Role.USER))
                    .withMessageContaining("User email must be not blank");
        }

        @NullAndEmptySource
        @ParameterizedTest
        void should_not_create_user_with_invalid_blank_cpf(String cpf) {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", "email@email.com", cpf))
                    .withMessageContaining("User cpf must be not blank");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(1L, "name", "email@email.com", cpf, LocalDateTime.now(), Role.USER))
                    .withMessageContaining("User cpf must be not blank");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", "email@email.com", cpf, Role.USER))
                    .withMessageContaining("User cpf must be not blank");
        }


        @ValueSource(
                strings = {
                        "00000000000",
                        "notavalidcpf",
                        "111-111-111-11",
                }
        )
        @ParameterizedTest
        void should_not_create_user_with_invalid_cpf(String cpf) {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", "email@email.com", cpf))
                    .withMessageContaining("User cpf must be a valid document");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(1L, "name", "email@email.com", cpf, LocalDateTime.now(), Role.USER))
                    .withMessageContaining("User cpf must be a valid document");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", "email@email.com", cpf, Role.USER))
                    .withMessageContaining("User cpf must be a valid document");
        }

        @Test
        void should_not_create_user_without_role() {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(1L, "name", "email@email.com", "114.974.750-15",  LocalDateTime.now(), null))
                    .withMessageContaining("User role must be not null");

            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User("name", "email@email.com", "114.974.750-15", null))
                    .withMessageContaining("User role must be not null");
        }

        @Test
        void should_not_create_user_without_id() {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(null, "name", "email@email.com", "114.974.750-15", null, Role.USER))
                    .withMessageContaining("User id must be not null");
        }

        @Test
        void should_not_create_user_without_created_at() {
            assertThatExceptionOfType(InvariantException.class)
                    .isThrownBy(() -> new User(42L, "name", "email@email.com", "114.974.750-15", null, Role.USER))
                    .withMessageContaining("User created at must be not null");
        }

    }
}