package ru.itmo.prog.lab4.models.events;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ru.itmo.prog.lab4.lib.events.interfaces.Event;
import ru.itmo.prog.lab4.lib.events.interfaces.EventBus;
import ru.itmo.prog.lab4.lib.events.EventHandler;

import java.util.List;

public class EventBusBean {
  private final EventBus<Event, EventHandler<?>> bus;

  @Inject
  EventBusBean(
    @Named("bus") EventBus bus,
    @Named("handlers") List<EventHandler<?>> handlers
  ) {
    this.bus = bus;
    handlers.forEach(bus::subscribe);
  }

  public EventBus<Event, EventHandler<?>> bus() {
    return this.bus;
  }
}
