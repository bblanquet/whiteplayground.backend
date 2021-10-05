package whiteplayground.test.transfer.utils.http;

import reactor.core.publisher.Mono;
import whiteplayground.test.transfer.utils.response.Response;

public interface HttpRequesterParamLess<TResult> {
    public Mono<Response<TResult>> get(String url);
}
