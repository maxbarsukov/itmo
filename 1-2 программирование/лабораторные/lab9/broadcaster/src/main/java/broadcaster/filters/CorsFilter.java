package broadcaster.filters;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Filter("/**")
public class CorsFilter implements HttpServerFilter {
  private final TraceService traceService;

  public CorsFilter(TraceService traceService) {
    this.traceService = traceService;
  }

  @Override
  public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request,
                                                    ServerFilterChain chain) {
    return Flux.from(traceService
      .trace(request))
      .switchMap(aBoolean -> chain.proceed(request))
      .doOnNext(res -> {
        res.getHeaders().add("Access-Control-Allow-Credentials", "true");
        res.getHeaders().add("Access-Control-Allow-Methods", "*");
        res.getHeaders().add("Access-Control-Allow-Headers", "*");
        res
          .getHeaders()
          .add(
            "Access-Control-Allow-Origin",
            (request.getHeaders().getOrigin().isPresent() ? request.getHeaders().getOrigin().get() : "http://localhost:3000")
          );
        res.getHeaders().add("Vary", "origin");
      });
  }
}
