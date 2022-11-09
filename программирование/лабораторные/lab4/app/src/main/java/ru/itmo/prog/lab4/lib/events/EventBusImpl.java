package ru.itmo.prog.lab4.lib.events;

import com.google.inject.Singleton;
import ru.itmo.prog.lab4.lib.events.interfaces.Event;
import ru.itmo.prog.lab4.lib.events.interfaces.EventBus;
import ru.itmo.prog.lab4.lib.events.interfaces.FailureConsumer;

import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Singleton
public class EventBusImpl<E extends Event, H extends EventHandler<?>> implements EventBus<E, H> {
  private final ReferenceQueue<? super H> gcQueue = new ReferenceQueue<>();
  private final AtomicInteger processing = new AtomicInteger();
  private final Set<WeakHandler<H>> handlers = Collections.newSetFromMap(new ConcurrentHashMap<>());

  protected void runHandler(H handler, E event) throws EventException {
    handler.handleEvent(event);
  }

  @Override
  public void subscribe(H subscriber) {
    handlers.add(new WeakHandler<H>(subscriber, gcQueue));
  }

  @Override
  public void unsubscribe(H subscriber) {
    handlers.remove(new WeakHandler<H>(subscriber, gcQueue));
  }

  @Override
  public void publish(E event) {
    publish(event, null, null);
  }

  @Override
  public void publish(E event, BiConsumer<E, H> success, FailureConsumer<E, H> failure) {
    if (event == null) return;

    processing.incrementAndGet();
    try {
      processEvent(new EventWrapper<>(event, success, failure));
    } finally {
      processing.decrementAndGet();
    }
  }

  @Override
  public boolean hasPendingEvents() {
    return processing.get() > 0;
  }

  private void processEvent(EventWrapper<E, H> eventWrapper) {
    WeakHandler weakHandler;
    while ((weakHandler = (WeakHandler)gcQueue.poll()) != null) {
      handlers.remove(weakHandler);
    }
    if (eventWrapper != null) {
      notifySubscribers(eventWrapper);
    }
  }

  private void notifySubscribers(EventWrapper<E, H> eventWrapper) {
    for (WeakHandler<H> wh : handlers) {
      H eh = wh.get();
      if (eh == null) {
        continue;
      }

      try {
        if (eh.getLinkedClass() == null) {
          if (eh.canHandle(eventWrapper.event.getClass())) {
            runHandlerWrapper(eh, eventWrapper);
          }
        } else if (eh.getLinkedClass().equals(eventWrapper.event.getClass())) {
          runHandlerWrapper(eh, eventWrapper);
        }
      } catch (Throwable e) {
        System.err.println("Обработка события провалилась " + eventWrapper.event.getClass().getSimpleName()
          + ". " + e.getMessage());
      }
    }
  }

  private void runHandlerWrapper(H handler, EventWrapper<E, H> eventWrapper) {
    try {
      runHandler(handler, eventWrapper.getEvent());
      if (eventWrapper.success != null) {
        eventWrapper.success.accept(eventWrapper.event, handler);
      }
    } catch (EventException eventException) {
      if (eventWrapper.failure != null) {
        eventWrapper.failure.accept(eventWrapper.event, handler, eventException);
      } else {
        System.err.println("Обработка обработчиком провалилась для " + eventWrapper.event.getClass().getSimpleName() + ". " + eventException.getMessage());
      }
    }
  }
}
