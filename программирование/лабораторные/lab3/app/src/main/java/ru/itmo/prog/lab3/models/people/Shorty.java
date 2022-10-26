package ru.itmo.prog.lab3.models.people;

import ru.itmo.prog.lab3.interfaces.Climbable;
import ru.itmo.prog.lab3.interfaces.Jumpable;
import ru.itmo.prog.lab3.models.Action;
import ru.itmo.prog.lab3.models.Impression;
import ru.itmo.prog.lab3.models.JumpDistance;
import ru.itmo.prog.lab3.models.places.Place;

import java.util.Objects;

public class Shorty extends Person implements Jumpable {
  private int jumpDistance;

  public Shorty(String name, Sex sex, double mass) {
    super(name, sex, mass);
    this.jumpDistance = JumpDistance.DEFAULT.getDistance();
  }

  public Shorty(String name, Sex sex, double mass, int jumpDistance) {
    super(name, sex, mass);
    this.jumpDistance = jumpDistance;
  }

  public String getSingularName() {
    return "Коротышка";
  }

  public String getPluralName() {
    return "Коротышки";
  }

  public String jumpTo(Place place, double distance) {
    int numberOfJumps = (int) Math.ceil(distance / jumpDistance);
    String jumpDescription = switch (numberOfJumps) {
      case 0, 1 -> "Одним прыжком ";
      default -> "Несколькими прыжками ";
    };

    return jumpDescription + getName() + " " + Action.REACHED.getDescription(this) + " " + place.genitiveCase();
  }

  @Override
  public String climb(Climbable climbable) {
    return climbable.beClimbedBy(this);
  }

  @Override
  public String climb(Climbable climbable, Place place) {
    setCurrentImpression(Impression.JOYFUL);
    setLocation(place);
    return climb(climbable);
  }

  @Override
  public int calculateTimeToClimb() {
    return this.getMass() < 60 ? 5 : 10;
  }

  @Override
  public String dativeCase() {
    if (getName().matches(".*(?i)[аеёоуиэя]")) {
      return getName().substring(0, getName().length() - 1) + "е";
    }

    return getName() + "у";
  }

  @Override
  public String genitiveCase() {
    if (getName().matches(".*(?i)[аеёоуиэя]")) {
      return getName().substring(0, getName().length() - 1) + "у";
    }

    return getName() + "а";
  }

  public int getJumpDistance() {
    return jumpDistance;
  }

  public void setJumpDistance(int jumpDistance) {
    this.jumpDistance = jumpDistance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Shorty shorty = (Shorty) o;
    return jumpDistance == shorty.jumpDistance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), jumpDistance);
  }

  @Override
  public String toString() {
    return "Shorty{" +
      "name=" + getName() +
      "mass=" + getMass() +
      "currentImpression=" + getCurrentImpression() +
      "location=" + getLocation() +
      "jumpDistance=" + jumpDistance +
      '}';
  }
}
