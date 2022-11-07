package ru.itmo.prog.lab4.lib.events;

import ru.itmo.prog.lab4.interfaces.events.Event;
import ru.itmo.prog.lab4.interfaces.events.FailureConsumer;

import java.util.function.BiConsumer;

public class EventWrapper<E extends Event, H extends EventHandler<?>> {
  protected final E event;
  protected final BiConsumer<E, H> success;
  protected final FailureConsumer<E, H> failure;

  public EventWrapper(E event, BiConsumer<E, H> success, FailureConsumer<E, H> failure) {
    this.event = event;
    this.success = success;
    this.failure = failure;
  }

  public E getEvent() {
    return this.event;
  }
}
