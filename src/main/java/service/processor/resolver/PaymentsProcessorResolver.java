package service.processor.resolver;

import model.input.PaymentsProcessorName;
import service.processor.type.PaymentsProcessor;

public interface PaymentsProcessorResolver {
    PaymentsProcessor resolve(PaymentsProcessorName paymentsProcessorName);
}
