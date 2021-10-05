package whiteplayground.test.transfer.utils.response;

public class BasicResponse extends Object {
    protected boolean success;
    protected String message;

    public BasicResponse(boolean success, String message) {
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
