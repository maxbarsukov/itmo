package ru.itmo.prog.lab3.models.weather;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Objects;

public class Weather {
  @Inject @Named("Wind")
  private Wind wind;

  public Wind getWind() {
    return wind;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Weather weather = (Weather) o;
    return wind.equals(weather.wind);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wind);
  }

  @Override
  public String toString() {
    return "Weather{" +
      "wind=" + wind +
      '}';
  }
}
