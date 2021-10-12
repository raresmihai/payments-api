package service.api.tokenize;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import model.input.CustomerPaymentData;
import persistency.CustomerRepository;
import service.exception.PaymentMethodAlreadyExistsException;
import service.exception.PaymentsException;
import service.processor.resolver.PaymentsProcessorResolver;

import javax.inject.Inject;


public class PersistentTokenizerService implements TokenizerService {
    private final PaymentsProcessorResolver paymentsProcessorResolver;

    private final CustomerRepository repository;

    private final TokenGenerator tokenGenerator;

    private final LambdaLogger logger;

    @Inject
    public PersistentTokenizerService(PaymentsProcessorResolver paymentsProcessorResolver, CustomerRepository repository, TokenGenerator tokenGenerator, LambdaLogger logger) {
        this.paymentsProcessorResolver = paymentsProcessorResolver;
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
        this.logger = logger;
    }

    @Override
    public String tokenize(CustomerPaymentData customerPaymentData) throws PaymentsException {
        this.logger.log("Checking if customer already exists...");
        if (repository.customerPaymentAlreadyExists(customerPaymentData)) {
            this.logger.log("Customer already exists");
            throw new PaymentMethodAlreadyExistsException(customerPaymentData.getPaymentsProcessorType().toString());
        }

        this.logger.log("Customer payment method is new. Adding to the vault...");
        String token = this.tokenGenerator.generate();
        this.logger.log("Generated token");
        this.paymentsProcessorResolver.resolve(customerPaymentData.getPaymentsProcessorType())
                .createCustomerPaymentMethod(token, customerPaymentData.getCreditCardInfo());
        this.repository.save(token, customerPaymentData);
        this.logger.log("Saved");
        return token;
    }
}
