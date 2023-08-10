package broadcaster.filters;

import io.micronaut.http.HttpRequest;
import org.reactivestreams.Publisher;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TraceService {
  private static final Logger logger = LoggerFactory.getLogger(TraceService.class);

  public Publisher<Boolean> trace(HttpRequest<?> request) {
    return Mono.fromCallable(() -> {
      logger.debug("Tracing request: {}", request.getUri());
      return true;
    }).subscribeOn(Schedulers.boundedElastic()).flux();
  }
}
