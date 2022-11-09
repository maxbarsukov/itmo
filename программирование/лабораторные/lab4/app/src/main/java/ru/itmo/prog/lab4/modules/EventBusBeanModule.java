package ru.itmo.prog.lab4.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

import ru.itmo.prog.lab4.lib.events.*;
import ru.itmo.prog.lab4.lib.events.interfaces.EventBus;
import ru.itmo.prog.lab4.models.events.NoOneExpectedEvent;
import ru.itmo.prog.lab4.models.events.OrderGiven;

import java.util.ArrayList;
import java.util.List;

public class EventBusBeanModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EventBus.class)
      .annotatedWith(Names.named("bus"))
      .to(EventBusImpl.class);

    bind(new TypeLiteral<List<EventHandler<?>>>() {})
      .annotatedWith(Names.named("handlers"))
      .toInstance(new ArrayList<>() {{
        add(new OrderGiven.Handler());
        add(new NoOneExpectedEvent.Handler());
      }});
  }
}
