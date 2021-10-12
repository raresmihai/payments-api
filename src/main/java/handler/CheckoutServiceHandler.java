package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.braintreegateway.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ioc.ApplicationGraph;
import ioc.DaggerApplicationGraph;
import ioc.MainModule;
import model.input.CustomerPaymentData;
import model.input.TransactionRequest;
import model.output.CheckoutResponse;
import model.output.TokenResponse;
import persistency.exception.RepositoryException;
import service.exception.PaymentsException;

public class CheckoutServiceHandler implements RequestHandler<TransactionRequest, CheckoutResponse> {
    public CheckoutResponse handleRequest(TransactionRequest transactionRequest, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log(transactionRequest.getToken());
        final ApplicationGraph dependencyGraph = DaggerApplicationGraph.builder().mainModule(new MainModule(logger)).build();
        CheckoutResponse response = new CheckoutResponse();
        response.setRequestId(context.getAwsRequestId());
        try {
            Transaction transaction = dependencyGraph.checkoutService().checkout(transactionRequest);

            ObjectMapper mapper = new ObjectMapper();
            String transactionResult = mapper.writeValueAsString(transaction);
            response.setResult(transactionResult);
            response.setSuccess(true);
        } catch (PaymentsException | RepositoryException | JsonProcessingException e) {
            response.setErrorMessage(e.getMessage());
            response.setSuccess(false);
            logger.log(e.getMessage());
        }
        return response;
    }
}
