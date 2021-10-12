package ioc;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import dagger.Module;
import dagger.Provides;
import model.input.PaymentsProcessorName;
import persistency.CustomerRepository;
import persistency.DynamoDBCustomerRepository;
import service.api.checkout.CheckoutService;
import service.api.checkout.CheckoutServiceImpl;
import service.api.tokenize.PersistentTokenizerService;
import service.api.tokenize.TokenizerService;
import service.processor.type.PaymentsProcessor;
import service.processor.resolver.PaymentsProcessorResolver;
import service.processor.resolver.PaymentsProcessorResolverImpl;
import service.processor.type.braintree.BraintreeProcessor;
import service.api.tokenize.RandomTokenGenerator;
import service.api.tokenize.TokenGenerator;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Module
public class MainModule {
    private final LambdaLogger logger;

    public MainModule(LambdaLogger logger) {
        // dependencies injected at Runtime
        this.logger = logger;
    }

    @Provides
    @Singleton
    public TokenizerService tokenizerService(PaymentsProcessorResolver paymentsProcessorResolver, CustomerRepository repository, TokenGenerator tokenGenerator) {
        return new PersistentTokenizerService(paymentsProcessorResolver, repository, tokenGenerator, this.logger);
    }

    @Provides
    @Singleton
    public CheckoutService checkoutService(PaymentsProcessorResolver paymentsProcessorResolver, CustomerRepository repository) {
        return new CheckoutServiceImpl(paymentsProcessorResolver, repository, this.logger);
    }

    @Provides
    @Singleton
    public PaymentsProcessorResolver paymentsProcessorResolver(BraintreeProcessor braintreeProcessor) {
        Map<PaymentsProcessorName, PaymentsProcessor> map = new HashMap<>();
        map.put(PaymentsProcessorName.Braintree, braintreeProcessor);
        return new PaymentsProcessorResolverImpl(map);
    }

    @Provides
    @Singleton
    public BraintreeProcessor braintreeProcessor(BraintreeGateway braintreeGateway) {
        return new BraintreeProcessor(braintreeGateway);
    }

    @Provides
    @Singleton
    public BraintreeGateway braintreeGateway() {
        return new BraintreeGateway(
                Environment.SANDBOX,
                System.getenv("BRAINTREE_MERCHANT_ID"),
                System.getenv("BRAINTREE_PUBLIC_KEY"),
                System.getenv("BRAINTREE_PRIVATE_KEY")
        );
    }

    @Provides
    @Singleton
    public CustomerRepository customerRepository(DynamoDB dynamoDB) {
        return new DynamoDBCustomerRepository(
                dynamoDB,
                System.getenv("DYNAMO_CUSTOMERS_TABLE_NAME"),
                System.getenv("DYNAMO_CUSTOMERS_INDEX_NAME")
        );
    }

    @Provides
    @Singleton
    public DynamoDB dynamoDB() {
        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(Regions.fromName(System.getenv("AWS_REGION"))));
        return new DynamoDB(client);
    }

    @Provides
    @Singleton
    public TokenGenerator tokenGenerator() {
        return new RandomTokenGenerator();
    }
}
