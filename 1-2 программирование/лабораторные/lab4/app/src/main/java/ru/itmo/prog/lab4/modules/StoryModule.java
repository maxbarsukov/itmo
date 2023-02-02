package ru.itmo.prog.lab4.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class StoryModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(String.class)
      .annotatedWith(Names.named("Title"))
      .toInstance("Приключения Незнайки. Отрывок");
  }
}
