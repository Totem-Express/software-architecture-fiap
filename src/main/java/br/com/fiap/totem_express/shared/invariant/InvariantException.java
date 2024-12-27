package br.com.fiap.totem_express.shared.invariant;

public class InvariantException extends RuntimeException {
    private static final String INVARIANT_MESSAGE = """
        Invariant Exception: %s
        Actual Value: %s
    """;

    private String message;
    private Object value;

    public InvariantException(String message){
        this.message = message;
    }

    public InvariantException(String message, Object actual){
        this(message);
        this.value = actual;
    }

    @Override
    public String getMessage() {
        return INVARIANT_MESSAGE.formatted(message, value);
    }
}
