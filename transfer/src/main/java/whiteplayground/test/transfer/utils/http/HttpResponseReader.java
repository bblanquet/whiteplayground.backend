package whiteplayground.test.transfer.utils.http;



import org.apache.http.HttpResponse;

import java.io.IOException;

public interface HttpResponseReader {
    public String read(HttpResponse response) throws IOException;
}
