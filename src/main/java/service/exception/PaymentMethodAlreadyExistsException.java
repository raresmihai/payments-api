package service.exception;

public class PaymentMethodAlreadyExistsException extends PaymentsException{
    public PaymentMethodAlreadyExistsException(String processorName) {
        super("Payment method already exists for processor " + processorName);
    }

    public PaymentMethodAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
