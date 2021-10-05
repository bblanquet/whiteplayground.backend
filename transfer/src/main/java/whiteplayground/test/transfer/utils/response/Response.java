package whiteplayground.test.transfer.utils.response;

public class Response<T> extends BasicResponse{
    private T value;

    public Response(T value, boolean success, String message) {
        super(success,message);
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    public static <T> Response<T> success(T value){
        return new Response<T>(value,true,"");
    }
    public static <T> Response<T> error(String message){
        return new Response<T>(null,false,message);
    }
}
