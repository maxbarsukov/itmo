package ru.itmo.prog.lab4.modules;

import com.google.inject.AbstractModule;
import ru.itmo.prog.lab4.lib.events.interfaces.EventBus;
import ru.itmo.prog.lab4.lib.events.EventBusImpl;

public class SceneModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EventBus.class).to(EventBusImpl.class);
  }
}
