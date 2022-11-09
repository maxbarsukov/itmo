package ru.itmo.prog.lab4.models.events;

import com.google.inject.Inject;

import ru.itmo.prog.lab4.interfaces.events.Event;
import ru.itmo.prog.lab4.interfaces.events.EventBus;
import ru.itmo.prog.lab4.lib.events.EventBusImpl;
import ru.itmo.prog.lab4.lib.events.EventHandler;

import java.util.List;

public class EventBusBean {
  private final EventBus<Event, EventHandler<?>> bus;

  @Inject
  EventBusBean(List<EventHandler<?>> handlers) {
    this.bus = new EventBusImpl<>();
    handlers.stream().forEach(bus::subscribe);
  }

  public EventBus<Event, EventHandler<?>> bus() {
    return this.bus;
  }
}
