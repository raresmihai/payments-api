package model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Part of the Tokenizing service input request.
 */
public final class CreditCardInfo {
    @JsonProperty("number")
    private String number;

    @JsonProperty("expirationDate")
    private String expirationDate;

    public CreditCardInfo() {}

    public CreditCardInfo(String number, String expirationDate) {
        this.number = number;
        this.expirationDate = expirationDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
