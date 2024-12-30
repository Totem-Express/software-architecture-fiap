package br.com.fiap.totem_express.presentation.errors;

import br.com.fiap.totem_express.shared.invariant.InvariantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InvariantException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handleInvariantException(){
        return ResponseEntity.internalServerError().body(Map.of(
                "error", "Unexpected Error",
                "message", "Our team was notified"
        ));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BadRequestError handleValidationError(MethodArgumentNotValidException exception){
        return Stream
                .concat(
                    exception.getFieldErrors().stream().map(it -> new FieldError(it.getField(), it.getDefaultMessage())),
                    exception.getGlobalErrors().stream().map(it -> new FieldError(it.getCode(), it.getDefaultMessage()))
                )
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    BadRequestError::new
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundError handleIllegalArgumentException(IllegalArgumentException exception) {
        return new NotFoundError("Not Found", exception.getMessage());
    }

}
