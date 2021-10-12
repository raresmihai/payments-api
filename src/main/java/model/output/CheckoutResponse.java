package model.output;

public class CheckoutResponse extends Response {
    private String result;

    public CheckoutResponse() {}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
