package ru.itmo.prog.lab4.models.people;

import ru.itmo.prog.lab4.interfaces.Climbable;
import ru.itmo.prog.lab4.interfaces.Hearable;
import ru.itmo.prog.lab4.interfaces.Jumpable;
import ru.itmo.prog.lab4.models.common.Action;
import ru.itmo.prog.lab4.models.common.Impression;
import ru.itmo.prog.lab4.models.common.JumpDistance;
import ru.itmo.prog.lab4.models.common.Utils;
import ru.itmo.prog.lab4.models.places.Place;

import java.util.Objects;
import java.util.function.Supplier;

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

  public String jumpResult() {
    var result = Utils.capitalize(pronoun()) + ' ';
    if (jumpDistance == JumpDistance.BIG.getDistance()) {
      return result + "немного не рассчитал толчка и поднялся выше, чем было надо";
    }
    return result + "рассчитал прыжок и поднялся точно туда, куда надо было";
  }

  @Override
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

  public boolean canHear(Hearable hearable) {
    // У коротышек острый слух
    return hearable.getVolume() > 20;
  }

  @Override
  public String tryTo(Supplier<String> function) {
    // Коротышки с плохим зрением ничего не могут разглядеть
    if (!this.hasGoodVision() && function.get().equals(Action.DISCERN.getDefault())) {
      return "не удалось ничего " + function.get();
    }
    return Action.TRIED.getDescription(this) + ' ' + function.get();
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
