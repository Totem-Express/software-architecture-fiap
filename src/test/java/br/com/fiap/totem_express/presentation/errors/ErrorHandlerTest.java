package br.com.fiap.totem_express.presentation.errors;

import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class ErrorHandlerTest {

    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    public void should_return_generic_error_message_for_invariant_exception() {
        ResponseEntity response = errorHandler.handleInvariantException();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat((Map) response.getBody()).containsEntry("error", "Unexpected Error")
                                             .containsEntry("message", "Our team was notified");
    }

    @Test
    public void should_return_field_errors_and_bad_request() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getFieldErrors()).thenReturn(List.of(new org.springframework.validation.FieldError("fieldError", "field", "defaultMessage for fieldError")));
        when(exception.getGlobalErrors()).thenReturn(List.of(new org.springframework.validation.ObjectError("globalError", "defaultMessage for globalError")));

        BadRequestError response = errorHandler.handleValidationError(exception);
        assertThat(response.errors())
        .hasSize(2)
        .contains(
            new FieldError("field", "defaultMessage for fieldError"),
            new FieldError(null, "defaultMessage for globalError")
        );
    
    }

    @Test
    public void should_return_not_found_error_for_illegal_argument_exception() {
        IllegalArgumentException exception = new IllegalArgumentException("Product with ID 1 not found");

        NotFoundError response = errorHandler.handleIllegalArgumentException(exception);

        assertThat(response.error()).isEqualTo("Not Found");
        assertThat(response.message()).isEqualTo(exception.getMessage());
    }
}
