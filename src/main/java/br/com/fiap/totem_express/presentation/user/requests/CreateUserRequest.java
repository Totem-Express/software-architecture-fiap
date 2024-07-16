package br.com.fiap.totem_express.presentation.user.requests;

import br.com.fiap.totem_express.application.user.input.NewUserInput;
import br.com.fiap.totem_express.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotNull @NotBlank String name,
        @NotNull @Email String email,
        @NotNull @NotBlank String cpf
) implements NewUserInput {
    @Override
    public User toDomain() {
        return new User(name, email, cpf);
    }
}