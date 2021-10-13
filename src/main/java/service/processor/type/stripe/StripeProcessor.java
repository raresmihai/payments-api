package service.processor.type.stripe;

import com.braintreegateway.Transaction;
import model.input.CreditCardInfo;
import service.exception.PaymentsException;
import service.processor.type.PaymentsProcessor;

import java.math.BigDecimal;

public class StripeProcessor implements PaymentsProcessor {
    @Override
    public void createCustomerPaymentMethod(String token, CreditCardInfo creditCardInfo) throws PaymentsException {
        
    }

    @Override
    public Transaction checkout(String paymentMethodToken, BigDecimal amount) throws PaymentsException {
        return null;
    }
}
