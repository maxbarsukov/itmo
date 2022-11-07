package ru.itmo.prog.lab4.models.people;

import ru.itmo.prog.lab4.interfaces.*;
import ru.itmo.prog.lab4.interfaces.speech.HasCases;
import ru.itmo.prog.lab4.interfaces.speech.Pluralable;
import ru.itmo.prog.lab4.models.common.Action;
import ru.itmo.prog.lab4.models.common.Impression;
import ru.itmo.prog.lab4.models.common.Time;
import ru.itmo.prog.lab4.models.common.Utils;
import ru.itmo.prog.lab4.models.places.Place;

import java.util.Objects;
import java.util.function.Supplier;

abstract class Limb implements Pluralable {
  public enum HealthStatus {
    HEALTHY,
    SICK,
    MISSING,
  }

  private String name;
  private HealthStatus status = HealthStatus.HEALTHY;

  public Limb(String name) {
    this.name = name;
  }

  public boolean isHealth() {
    return status == HealthStatus.HEALTHY;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public HealthStatus getStatus() {
    return status;
  }

  public void setStatus(HealthStatus status) {
    this.status = status;
  }
}

public abstract class Person implements Cannable, Climber, HasCases, Hearer, Pluralable, Tryable {
  public class Body {
    public class Arm extends Limb {
      public Arm() {
        super("Рука");
      }

      @Override
      public String getSingularName() {
        return "рука";
      }

      @Override
      public String getPluralName() {
        return "руки";
      }
    }

    public class Leg extends Limb {
      public Leg() {
        super("Нога");
      }

      @Override
      public String getSingularName() {
        return "нога";
      }

      @Override
      public String getPluralName() {
        return "ноги";
      }
    }

    private final Arm leftArm;
    private final Arm rightArm;
    private final Leg leftLeg;
    private final Leg rightLeg;

    public Body() {
      this.leftArm = new Arm();
      this.rightArm = new Arm();
      this.leftLeg = new Leg();
      this.rightLeg = new Leg();
    }

    public void breakDown() {
      if (Utils.randomBoolean()) {
        leftArm.setStatus(Limb.HealthStatus.SICK);
      } else {
        rightArm.setStatus(Limb.HealthStatus.SICK);
      }

      if (Utils.randomBoolean()) {
        leftLeg.setStatus(Limb.HealthStatus.SICK);
      } else {
        rightLeg.setStatus(Limb.HealthStatus.SICK);
      }
    }

    public boolean areArmsHealthy() {
      return leftArm.isHealth() && rightArm.isHealth();
    }

    public boolean areLegsHealthy() {
      return leftLeg.isHealth() && rightLeg.isHealth();
    }

    public boolean canStand() {
      return leftLeg.isHealth() || rightLeg.isHealth();
    }
  }

  private final Body body = new Body();
  private String name;
  private Sex sex;
  private double mass;
  private Place location;
  private Impression currentImpression = Impression.NONE;
  private String currentActivity;

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

  public String healthStatus() {
    if (body.areLegsHealthy() && body.areArmsHealthy()) {
      return "Руки и ноги легко повиновались и были здоровы";
    }
    if (body.areArmsHealthy() || body.areLegsHealthy()) {
      return "То ли руки не повиновались, то ли ноги. Неплохо";
    }
    return "Руки и ноги повиновались с трудом, словно были свинцом налиты";
  }

  @Override
  public String can(Supplier<String> function) {
    return getName() + "в любой момент " + Action.CAN.getDescription(this) + " " + function.get();
  }

  @Override
  public String tryTo(Supplier<String> function) {
    return Action.TRIED.getDescription(this) + ' ' + function.get();
  }

  @Override
  public boolean canHear(Hearable hearable) {
    return hearable.getVolume() > 70;
  }

  @Override
  public boolean hear(Hearable hearable) {
    if (!canHear(hearable)) return false;

    if (hearable.getContent().equals("Медведь сел в машину и сгорел")) {
      setCurrentImpression(Impression.JOYFUL);
      return true;
    }
    return !hearable.getContent().equals("бла-бла-бла");
  }

  public String warmUpArms() {
    String result = "";
    if (body.leftArm.getStatus() != Limb.HealthStatus.MISSING && body.rightArm.getStatus() != Limb.HealthStatus.MISSING) {
      result += "поднять руку, потом другую";
    } else if (body.leftArm.getStatus() != Limb.HealthStatus.MISSING || body.rightArm.getStatus() != Limb.HealthStatus.MISSING) {
      result += "поднять руку";
    }
    return result;
  }

  public String warmUpLegs() {
    String result = "";
    if (body.leftLeg.getStatus() != Limb.HealthStatus.MISSING && body.rightLeg.getStatus() != Limb.HealthStatus.MISSING) {
      result += "сделать шаг, другой";
    } else if (body.leftLeg.getStatus() != Limb.HealthStatus.MISSING || body.rightLeg.getStatus() != Limb.HealthStatus.MISSING) {
      result += "сделать шаг";
    }
    return result;
  }

  public int distanceTo(Place place) {
    return place.getName().length();
  }

  public boolean isMale() {
    return sex == Sex.MALE;
  }

  public String pronoun() {
    return isMale() ? "он" : "она";
  }

  public String possessivePronoun() {
    return isMale() ? "его" : "её";
  }

  public String checkFear(String reason) {
    String impression =  (currentImpression == Impression.SCARED ? " испугало " : " не испугало ");
    return reason + impression + genitiveCase();
  }

  public String checkStandingAbility(String adverb) {
    if (body.canStand()) {
      return pronoun() + ' ' + adverb + " твердо держится на ногах";
    }
    return pronoun() + "не может стоять и падает";
  }

  public String getSingularName() {
    return "Человек";
  }

  public String getPluralName() {
    return "Люди";
  }

  public Body getBody() {
    return body;
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

  public String getCurrentActivity() {
    return currentActivity;
  }

  public void setCurrentActivity(String currentActivity) {
    this.currentActivity = currentActivity;
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
