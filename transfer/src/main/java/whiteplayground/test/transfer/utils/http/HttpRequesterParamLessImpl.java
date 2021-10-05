package whiteplayground.test.transfer.utils.http;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import whiteplayground.test.transfer.utils.json.JsonConverterImpl;
import whiteplayground.test.transfer.utils.response.Response;

public class HttpRequesterParamLessImpl<TResult> implements HttpRequesterParamLess<TResult> {
    private JsonConverterImpl<TResult> jsonConverter;
    private HttpResponseReader reader;

    public HttpRequesterParamLessImpl(JsonConverterImpl<TResult> converter, HttpResponseReader reader){
        this.jsonConverter = converter;
        this.reader = reader;
    }

    public Mono<Response<TResult>> get(String url){
        try{
            return HttpClient.create()
                    .baseUrl(url)
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
}
