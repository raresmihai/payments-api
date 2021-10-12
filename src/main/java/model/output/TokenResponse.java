package model.output;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TokenResponse extends Response {
    @JsonProperty("token")
    private String token;

    public TokenResponse() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}