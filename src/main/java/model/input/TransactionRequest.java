package model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public final class TransactionRequest {
    @JsonProperty("token")
    private String token;

    @JsonProperty("amount")
    private BigDecimal amount;

    public TransactionRequest() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
