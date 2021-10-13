package handler;

import ioc.ApplicationGraph;
import ioc.DaggerApplicationGraph;
import ioc.MainModule;
import model.input.CustomerPaymentData;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import model.output.TokenResponse;
import service.exception.PaymentsException;

/**
 * Main AWS Lambda function handler for the Tokenizer service.
 */
public class TokenizingServiceHandler implements RequestHandler<CustomerPaymentData, TokenResponse> {
    public TokenResponse handleRequest(CustomerPaymentData customerPaymentData, Context context) {
        LambdaLogger logger = context.getLogger();
        TokenResponse response = new TokenResponse();
        response.setRequestId(context.getAwsRequestId());

        if (customerPaymentData.getPaymentsProcessorType() == null || customerPaymentData.getCreditCardInfo() == null) {
            response.setSuccess(false);
            String errorMessage = "The CustomerPaymentData event did not meet the required format.\n";
            logger.log(errorMessage);
            response.setErrorMessage(errorMessage);
            return response;
        }

        final ApplicationGraph dependencyGraph = DaggerApplicationGraph.builder().mainModule(new MainModule(logger)).build();
        try {
            logger.log("Tokenizing new payment method for processor " + customerPaymentData.getPaymentsProcessorType() + "\n");
            String token = dependencyGraph.tokenizerService().tokenize(customerPaymentData);
            response.setToken(token);
            response.setSuccess(true);
            logger.log("Tokenizing service has completed successfully.\n");
        } catch (PaymentsException e) {
            response.setErrorMessage(e.getMessage());
            response.setSuccess(false);
            logger.log(e.getMessage());
        }
        return response;
    }
}
