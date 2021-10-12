package service.processor.type;

import com.braintreegateway.Transaction;
import model.input.CreditCardInfo;
import service.exception.PaymentsException;

import java.math.BigDecimal;

public interface PaymentsProcessor {
    void createCustomerPaymentMethod(String token, CreditCardInfo creditCardInfo) throws PaymentsException;

    Transaction checkout(String paymentMethodToken, BigDecimal amount) throws PaymentsException;
}
