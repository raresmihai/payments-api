package service.api.checkout;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.braintreegateway.Transaction;
import model.input.CustomerPaymentData;
import model.input.PaymentsProcessorName;
import model.input.TransactionRequest;
import persistency.CustomerRepository;
import persistency.exception.RepositoryException;
import service.exception.PaymentsException;
import service.processor.resolver.PaymentsProcessorResolver;

import javax.inject.Inject;

public class CheckoutServiceImpl implements CheckoutService {
    private final PaymentsProcessorResolver paymentsProcessorResolver;

    private final CustomerRepository repository;

    private final LambdaLogger logger;

    @Inject
    public CheckoutServiceImpl(PaymentsProcessorResolver paymentsProcessorResolver, CustomerRepository repository, LambdaLogger logger) {
        this.paymentsProcessorResolver = paymentsProcessorResolver;
        this.repository = repository;
        this.logger = logger;
    }

    @Override
    public Transaction checkout(TransactionRequest transactionData) throws PaymentsException, RepositoryException {
        this.logger.log(transactionData.getToken());
        CustomerPaymentData paymentData = this.repository.getCustomerPaymentDataByToken(transactionData.getToken());
        PaymentsProcessorName processorName = paymentData.getPaymentsProcessorType();
        return this.paymentsProcessorResolver.resolve(processorName).checkout(transactionData.getToken(), transactionData.getAmount());
    }
}
