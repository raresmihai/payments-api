package model.output;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    @JsonProperty("requestId")
    String requestId;

    @JsonProperty("success")
    boolean success;

    @JsonProperty("errorMessage")
    String errorMessage;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
