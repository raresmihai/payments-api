package persistency;

import model.input.CustomerPaymentData;
import persistency.exception.RepositoryException;

public interface CustomerRepository {
    boolean customerPaymentAlreadyExists(CustomerPaymentData customerPaymentData);

    void save(String token, CustomerPaymentData customerPaymentData);

    CustomerPaymentData getCustomerPaymentDataByToken(String token) throws RepositoryException;
}
