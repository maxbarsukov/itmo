package ru.itmo.prog.lab3.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import ru.itmo.prog.lab3.models.weather.Wind;

public class WeatherModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Wind.class)
      .annotatedWith(Names.named("Wind"))
      .toInstance(new Wind("порыв ветра", 100));
  }
}
