package ru.itmo.prog.lab4.modules;

import com.google.inject.AbstractModule;
import ru.itmo.prog.lab4.models.places.Workshop;

public class WorkshopModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(String.class).toInstance(Workshop.DEFAULT_NAME);
  }
}
