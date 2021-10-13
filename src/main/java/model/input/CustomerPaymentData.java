package model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Input request model for the Tokenizing service.
 */
public final class CustomerPaymentData {
    @JsonProperty("paymentsProcessorType")
    private PaymentsProcessorName paymentsProcessorType;

    @JsonProperty("creditCardInfo")
    private CreditCardInfo creditCardInfo;

    public CustomerPaymentData() {}

    public CustomerPaymentData(PaymentsProcessorName paymentsProcessorType, CreditCardInfo creditCardInfo) {
        this.paymentsProcessorType = paymentsProcessorType;
        this.creditCardInfo = creditCardInfo;
    }

    public PaymentsProcessorName getPaymentsProcessorType() {
        return paymentsProcessorType;
    }

    public void setPaymentsProcessorType(PaymentsProcessorName paymentsProcessorType) {
        this.paymentsProcessorType = paymentsProcessorType;
    }

    public CreditCardInfo getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(CreditCardInfo creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }
}
