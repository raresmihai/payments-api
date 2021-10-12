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

public class TokenizingServiceHandler implements RequestHandler<CustomerPaymentData, TokenResponse> {
    public TokenResponse handleRequest(CustomerPaymentData customerPaymentData, Context context) {
        LambdaLogger logger = context.getLogger();
        TokenResponse response = new TokenResponse();
        response.setRequestId(context.getAwsRequestId());

        if (customerPaymentData.getPaymentsProcessorType() == null || customerPaymentData.getCreditCardInfo() == null) {
            response.setSuccess(false);
            response.setErrorMessage("The CustomerPaymentData event did not meet the required format.");
            return response;
        }

        final ApplicationGraph dependencyGraph = DaggerApplicationGraph.builder().mainModule(new MainModule(logger)).build();
        try {
            String token = dependencyGraph.tokenizerService().tokenize(customerPaymentData);
            response.setToken(token);
            response.setSuccess(true);
        } catch (PaymentsException e) {
            response.setErrorMessage(e.getMessage());
            response.setSuccess(false);
            logger.log(e.getMessage());
        }
        return response;
    }
}
