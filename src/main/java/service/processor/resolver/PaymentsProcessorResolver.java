package service.processor.resolver;

import model.input.PaymentsProcessorName;
import service.processor.type.PaymentsProcessor;

/**
 * Returns the right PaymentProcessor (e.g. Braintree, Stripe) based on what was requested for the current payment method.
 */
public interface PaymentsProcessorResolver {
    PaymentsProcessor resolve(PaymentsProcessorName paymentsProcessorName);
}
