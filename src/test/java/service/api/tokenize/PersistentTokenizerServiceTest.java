package service.api.tokenize;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import junit.framework.TestCase;
import model.input.CreditCardInfo;
import model.input.CustomerPaymentData;
import model.input.PaymentsProcessorName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import persistency.DynamoDBCustomerRepository;
import service.exception.PaymentsException;
import service.processor.resolver.PaymentsProcessorResolver;
import service.processor.type.PaymentsProcessor;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersistentTokenizerServiceTest extends TestCase {

    @Mock
    private PaymentsProcessorResolver resolver;

    @Mock
    private PaymentsProcessor processor;

    @Mock
    private DynamoDBCustomerRepository repository;

    @Mock
    private TokenGenerator generator;

    private LambdaLoggerMock logger;

    private PersistentTokenizerService service;

    @Before
    public void setUp() {
        logger = new LambdaLoggerMock();
        service = new PersistentTokenizerService(resolver, repository, generator, logger);
    }

    @Test
    public void testTokenize_CustomerExists_expectFails() {
        // Arrange
        CustomerPaymentData paymentData = new CustomerPaymentData(PaymentsProcessorName.Braintree, new CreditCardInfo("number", "expiration"));
        when(repository.customerPaymentAlreadyExists(paymentData)).thenReturn(true);

        // Act
        PaymentsException exception = assertThrows(PaymentsException.class, () -> this.service.tokenize(paymentData));

        // Assert
        assertEquals("Payment method already exists for processor Braintree", exception.getMessage());
        assertTrue(this.logger.wasMessageLogged("Checking if customer already exists..."));
    }

    @Test
    public void testTokenize_CustomerIsNew_tokenGenerated() throws PaymentsException {
        // Arrange
        CustomerPaymentData paymentData = new CustomerPaymentData(PaymentsProcessorName.Braintree, new CreditCardInfo("number", "expiration"));
        when(repository.customerPaymentAlreadyExists(paymentData)).thenReturn(false);

        String token = "TestToken123";
        when(generator.generate()).thenReturn(token);

        when(resolver.resolve(PaymentsProcessorName.Braintree)).thenReturn(processor);

        // Act
        String generatedToken = this.service.tokenize(paymentData);

        // Assert
        assertEquals(token, generatedToken);
        assertTrue(this.logger.wasMessageLogged("Saved"));
    }

    private class LambdaLoggerMock implements LambdaLogger {
        private Set<String> messagesLogged;

        LambdaLoggerMock() {
            this.messagesLogged = new HashSet<>();
        }

        @Override
        public void log(String s) {
            this.messagesLogged.add(s);
        }

        @Override
        public void log(byte[] bytes) {
        }

        public boolean wasMessageLogged(String s) {
            return messagesLogged.contains(s);
        }
    }
}