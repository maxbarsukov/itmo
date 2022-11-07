package ru.itmo.prog.lab4.models.common;

import ru.itmo.prog.lab4.models.people.Person;

public enum Impression {
  SCARED,
  JOYFUL,
  SURPRISED,
  SAD,
  DECISIVE,
  NONE;

  public String reaction(Person person) {
    var verb = switch (this) {
      case SCARED -> "испугало";
      case JOYFUL -> "развеселило";
      case SURPRISED -> "удивило";
      case SAD -> "разочаровало";
      case DECISIVE -> "сделало решимым";
      case NONE -> "ничего не изменило";
    };

    return Utils.capitalize(person.possessivePronoun()) + ' ' + verb;
  }
}
