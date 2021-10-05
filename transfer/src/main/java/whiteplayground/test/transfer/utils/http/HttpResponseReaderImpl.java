package whiteplayground.test.transfer.utils.http;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpResponseReaderImpl implements HttpResponseReader {
    public String read(HttpResponse response) throws IOException {
        var br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        var result = "";
        String line = null;
        while ((line = br.readLine()) != null) {
            result += line;
        }
        return result;
    }
}
