package ru.itmo.prog.lab3.models.weather;

import java.util.Objects;

public class Weather {
  private Wind wind;

  public Wind getWind() {
    return wind;
  }

  public void setWind(Wind wind) {
    this.wind = wind;
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
}
