package service.processor.resolver;


import model.input.PaymentsProcessorName;
import service.processor.type.PaymentsProcessor;

import java.util.Map;

public class PaymentsProcessorResolverImpl implements PaymentsProcessorResolver {
    private final Map<PaymentsProcessorName, PaymentsProcessor> paymentsProcessorMap;

    public PaymentsProcessorResolverImpl(Map<PaymentsProcessorName, PaymentsProcessor> paymentsProcessorMap) {
        this.paymentsProcessorMap = paymentsProcessorMap;
    }

    @Override
    public PaymentsProcessor resolve(PaymentsProcessorName paymentsProcessorName) {
        return this.paymentsProcessorMap.get(paymentsProcessorName);
    }
}
