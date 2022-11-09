package ru.itmo.prog.lab4.models.common;

import ru.itmo.prog.lab4.models.people.Person;

public enum Impression {
  SCARED,
  JOYFUL,
  SURPRISED,
  WTF,
  SAD,
  DECISIVE,
  NONE;

  public String reaction() {
    return switch (this) {
      case SCARED -> "испугало";
      case JOYFUL -> "развеселило";
      case SURPRISED -> "удивило";
      case WTF -> "не успел даже сообразить, что произошло";
      case SAD -> "разочаровало";
      case DECISIVE -> "сделало решимым";
      case NONE -> "ничего не изменило";
    };
  }

  public String react(Person person) {
    return Utils.capitalize(person.possessivePronoun()) + ' ' + reaction();
  }
}
