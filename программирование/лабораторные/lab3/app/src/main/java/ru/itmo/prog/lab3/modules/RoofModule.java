package ru.itmo.prog.lab3.modules;

import com.google.inject.AbstractModule;
import ru.itmo.prog.lab3.models.places.Roof;

public class RoofModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(String.class).toInstance(Roof.DEFAULT_NAME);
  }
}
