package service.exception;

public class PaymentsException extends Exception {
    public PaymentsException(String message) {
        super(message);
    }

    public PaymentsException(Throwable cause) {
        super(cause);
    }
}
