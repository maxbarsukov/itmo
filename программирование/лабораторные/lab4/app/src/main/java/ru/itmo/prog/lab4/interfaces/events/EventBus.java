package ru.itmo.prog.lab4.interfaces.events;

import ru.itmo.prog.lab4.lib.events.EventHandler;

import java.util.function.BiConsumer;

public interface EventBus<E extends Event, H extends EventHandler<?>> {
  void subscribe(H subscriber);

  void unsubscribe(H subscriber);

  void publish(E event);

  void publish(E event, BiConsumer<E, H> success, FailureConsumer<E, H> failure);

  boolean hasPendingEvents();
}
