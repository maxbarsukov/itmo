package ru.itmo.prog.lab4.lib.events.interfaces;

import ru.itmo.prog.lab4.lib.events.EventHandler;
import ru.itmo.prog.lab4.lib.events.EventException;

@FunctionalInterface
public interface FailureConsumer<E extends Event, H extends EventHandler<?>> {
  void accept(E event, H handler, EventException th);
}
