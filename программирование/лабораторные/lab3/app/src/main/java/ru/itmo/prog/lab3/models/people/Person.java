package ru.itmo.prog.lab3.models.people;

import ru.itmo.prog.lab3.interfaces.*;
import ru.itmo.prog.lab3.models.Action;
import ru.itmo.prog.lab3.models.Impression;
import ru.itmo.prog.lab3.models.Time;
import ru.itmo.prog.lab3.models.places.Place;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class Person implements Climber, HasCases, Cannable, Pluralable {
  private String name;
  private Sex sex;
  private double mass;
  private Place location;
  private Impression currentImpression = Impression.NONE;

  public Person(String name, Sex sex, double mass) {
    this.name = name;
    this.sex = sex;
    this.mass = mass;
  }

  public abstract String climb(Climbable climbable);

  public String wantTo(Action action, Time time) {
    String word = switch (time) {
      case PRESENT -> "хочу";
      case FUTURE -> "захочу";
      case PAST -> "хотел";
    };
    return "уже " + word +  " " + action.getDescription(this);
  }

  @Override
  public String can(Supplier<String> function) {
    return getName() + "в любой момент " + Action.CAN.getDescription(this) + " " + function.get();
  }

  public boolean isMale() {
    return sex == Sex.MALE;
  }

  public String pronunciation() {
    return isMale() ? "его" : "её";
  }

  public String checkFear(String reason) {
    String impression =  (currentImpression == Impression.SCARED ? " испугало " : " не испугало ");
    return reason + impression + genitiveCase();
  }

  public String getSingularName() {
    return "Человек";
  }

  public String getPluralName() {
    return "Люди";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Sex getSex() {
    return sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public double getMass() {
    return mass;
  }

  public void setMass(double mass) {
    this.mass = mass;
  }

  public Place getLocation() {
    return location;
  }

  public void setLocation(Place location) {
    this.location = location;
  }

  public Impression getCurrentImpression() {
    return currentImpression;
  }

  public void setCurrentImpression(Impression currentImpression) {
    this.currentImpression = currentImpression;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
