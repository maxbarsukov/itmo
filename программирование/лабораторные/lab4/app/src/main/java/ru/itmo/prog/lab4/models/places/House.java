package ru.itmo.prog.lab4.models.places;

import com.google.inject.Inject;

public class House extends Place {
  public class Roof extends Place {
    public static final String DEFAULT_NAME = "Крыша";

    public Roof(String name) {
      super(name + " дома " + House.this.getName());
    }

    @Override
    public String dativeCase() {
      return "крышу";
    }

    @Override
    public String genitiveCase() {
      return "крыши";
    }

    @Override
    public String toString() {
      return getName();
    }
  }

  public static final String DEFAULT_NAME = "Дом";
  private Roof roof;

  @Inject
  public House(String name) {
    super(name);
  }

  public String distanceDescription(Workshop workshop) {
    return "которая находилась неподалеку от " + genitiveCase();
  }

  public String distanceDescription(Place place) {
    return "которая находилась чрезвычайно далеко от " + genitiveCase();
  }

  @Override
  public String dativeCase() {
    return "дому";
  }

  @Override
  public String genitiveCase() {
    return "дома";
  }

  public Roof getRoof() {
    return roof;
  }

  public void setRoof(Roof roof) {
    this.roof = roof;
  }

  @Override
  public String toString() {
    return "House{" +
      "name='" + getName() + '\'' +
      '}';
  }
}
