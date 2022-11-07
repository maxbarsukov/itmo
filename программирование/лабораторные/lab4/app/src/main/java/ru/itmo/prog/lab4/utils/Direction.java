package ru.itmo.prog.lab4.utils;

public class Direction {
  public enum Preposition {
    IN,
    TO,
    ON,
  }

  public enum Type {
    BACKWARD,
    FORWARD,
    SIDEWARD,
    NONE,
  }

  private final Type type;
  private final Preposition preposition;
  private final String text;

  public Direction(Type type, Preposition preposition, String text) {
    this.type = type;
    this.preposition = preposition;
    this.text = text;
  }

  @Override
  public String toString() {
    return decodeType() + " " + decodePreposition() + " " + this.text;
  }

  private String decodePreposition() {
    return switch (preposition) {
      case IN -> "в";
      case TO -> "к";
      case ON -> "на";
    };
  }

  private String decodeType() {
    return switch (type) {
      case BACKWARD -> "обратно";
      case FORWARD -> "вперед";
      case SIDEWARD -> "в сторону";
      default -> "";
    };
  }
}
