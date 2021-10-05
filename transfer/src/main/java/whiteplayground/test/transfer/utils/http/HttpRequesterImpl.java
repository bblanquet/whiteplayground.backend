package whiteplayground.test.transfer.utils.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import whiteplayground.test.transfer.utils.json.JsonConverterImpl;
import whiteplayground.test.transfer.utils.response.Response;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HttpRequesterImpl<TParam, TResult> implements HttpRequester<TParam,TResult> {
    private JsonConverterImpl<TResult> jsonConverter;
    private HttpResponseReader reader;

    public HttpRequesterImpl(JsonConverterImpl<TResult> converter, HttpResponseReader reader){
        this.jsonConverter = converter;
        this.reader = reader;
    }

    public Mono<Response<TResult>> get(String url, TParam param){
        try{
            var params = this.toNameValuePairList(param);
            var uri = new URIBuilder(url)
                    .addParameters(params)
                    .build();

            System.out.println("request " + uri.toString());
            return HttpClient.create()
                    .baseUrl(uri.toString())
                    .get()
                    .responseSingle((response,bytes)->{
                        return bytes.asString().map(content->{
                            try {
                                return new Response<TResult>(jsonConverter.deserialize(content),true,"");
                            } catch (JsonProcessingException e) {
                                return new Response<TResult>(null,false,e.getMessage());
                            }
                        });
                    });
        }catch (Exception e){
            return Mono.just(new Response<TResult>(null,false,e.getMessage()));
        }
    }

    private ArrayList<NameValuePair> toNameValuePairList(Object obj) throws IllegalArgumentException, IllegalAccessException {
        var list = new ArrayList<NameValuePair>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            list.add(new BasicNameValuePair(field.getName(), (String) field.get(obj)));
        }
        return list;
    }

}
