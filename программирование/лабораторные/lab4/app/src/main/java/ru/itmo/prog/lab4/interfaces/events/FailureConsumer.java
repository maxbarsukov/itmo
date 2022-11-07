package ru.itmo.prog.lab4.interfaces.events;

import ru.itmo.prog.lab4.lib.events.EventBusHandler;
import ru.itmo.prog.lab4.lib.events.EventException;

@FunctionalInterface
public interface FailureConsumer<E extends Event, H extends EventBusHandler<?>> {
  void accept(E event, H handler, EventException th);
}
