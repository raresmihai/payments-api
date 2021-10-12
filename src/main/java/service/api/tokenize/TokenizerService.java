package service.api.tokenize;

import model.input.CustomerPaymentData;
import service.exception.PaymentsException;

public interface TokenizerService {
    String tokenize(CustomerPaymentData customerPaymentData) throws PaymentsException;
}
