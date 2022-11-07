package ru.itmo.prog.lab3.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class WindModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(String.class).toInstance("порыв ветра");
    bind(Double.class).annotatedWith(Names.named("power")).toInstance(100.0);
  }
}
