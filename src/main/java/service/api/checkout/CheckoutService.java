package service.api.checkout;

import com.braintreegateway.Transaction;
import model.input.TransactionRequest;
import persistency.exception.RepositoryException;
import service.exception.PaymentsException;

public interface CheckoutService {
    Transaction checkout(TransactionRequest transactionData) throws PaymentsException, RepositoryException;
}
