package service.processor.type.braintree;

import com.braintreegateway.*;
import model.input.CreditCardInfo;
import service.exception.PaymentsException;
import service.processor.type.PaymentsProcessor;

import java.math.BigDecimal;

/**
 * Implementation of a PaymentProcessor using the Braintree gateway.
 */
public class BraintreeProcessor implements PaymentsProcessor {
    private BraintreeGateway braintreeGateway;

    public BraintreeProcessor(BraintreeGateway braintreeGateway) {
        this.braintreeGateway = braintreeGateway;
    }

    @Override
    public void createCustomerPaymentMethod(String token, CreditCardInfo creditCardInfo) throws PaymentsException {
        CustomerRequest request = new CustomerRequest()
                .creditCard()
                .number(creditCardInfo.getNumber())
                .expirationDate(creditCardInfo.getExpirationDate())
                .token(token)
                .done();
        Result<Customer> result = this.braintreeGateway.customer().create(request);
        throwIfResultUnsuccessful(result.isSuccess(), result.getMessage());
    }

    @Override
    public Transaction checkout(String paymentMethodToken, BigDecimal amount) throws PaymentsException {
        TransactionRequest request = new TransactionRequest()
                .paymentMethodToken(paymentMethodToken)
                .amount(amount);

        Result<Transaction> result = this.braintreeGateway.transaction().sale(request);
        throwIfResultUnsuccessful(result.isSuccess(), result.getMessage());
        return result.getTarget();
    }

    private void throwIfResultUnsuccessful(boolean success, String failureMessage) throws PaymentsException {
        if (!success) {
            throw new PaymentsException(failureMessage);
        }
    }
}
