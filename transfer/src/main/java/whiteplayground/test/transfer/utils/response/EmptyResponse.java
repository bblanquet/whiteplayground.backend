package whiteplayground.test.transfer.utils.response;

public class EmptyResponse {
    protected boolean success;
    protected String message;

    public EmptyResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public boolean isSuccess() {
        return success;
    }

}
