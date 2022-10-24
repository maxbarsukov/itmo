package ru.itmo.prog.lab3.models.weather;

import ru.itmo.prog.lab3.models.people.Person;

import java.util.Objects;

public class Wind {
  private final String name;

  /**
   * mass that wind can carry aside
   */
  private double power;

  public Wind(String name, double power) {
    this.name = name;
    this.power = power;
  }

  public String swoopInSuddenly(Person person) {
    String prefix = "налетевший неожиданно " + getName() + " ";
    if (person.getMass() >= getPower()) return prefix + "ничего не сделал";

    String result = prefix + "сдул " + person.pronunciation();
    if (person.getLocation() == null) return result;

    return result + " с " + person.getLocation().genitiveCase();
  }

  public String carryAside(Person person) {
    return person.getMass() < getPower() ? "понес в сторону" : "не смог никуда унести";
  }

  public String getName() {
    return name;
  }

  public double getPower() {
    return power;
  }

  public void setPower(double power) {
    this.power = power;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Wind wind = (Wind) o;
    return Double.compare(wind.power, power) == 0 && Objects.equals(name, wind.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, power);
  }

  @Override
  public String toString() {
    return "Wind{" +
      "name='" + name + '\'' +
      ", power=" + power +
      '}';
  }
}
