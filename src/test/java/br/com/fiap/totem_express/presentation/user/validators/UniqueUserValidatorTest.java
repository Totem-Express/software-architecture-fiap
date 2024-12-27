package br.com.fiap.totem_express.presentation.user.validators;

import br.com.fiap.totem_express.infrastructure.user.UserRepository;
import br.com.fiap.totem_express.presentation.user.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UniqueUserValidatorTest {

    private UniqueUserValidator validator;
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        validator = new UniqueUserValidator(repository);
    }

    @Test
    void should_support_CreateUserRequest_class() {
        assertThat(validator.supports(CreateUserRequest.class)).isTrue();
    }

    @Test
    void should_not_have_errors_when_user_does_not_exist() {
        CreateUserRequest request = new CreateUserRequest("name", "email@example.com", "123.456.789-00");
        Errors errors = new BeanPropertyBindingResult(request, "createUserRequest");

        when(repository.existsByEmailOrCpf(request.email(), request.cpf())).thenReturn(false);

        validator.validate(request, errors);

        assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    void should_have_errors_when_user_already_exists() {
        CreateUserRequest request = new CreateUserRequest("name", "email@example.com", "123.456.789-00");
        Errors errors = new BeanPropertyBindingResult(request, "createUserRequest");

        when(repository.existsByEmailOrCpf(request.email(), request.cpf())).thenReturn(true);

        validator.validate(request, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getAllErrors()).hasSize(1);
        assertThat(errors.getAllErrors().get(0).getCode()).isEqualTo("user.already.exists");
        assertThat(errors.getAllErrors().get(0).getDefaultMessage()).isEqualTo("Já existe um usuário cadastrado com este email ou cpf!");
    }
}