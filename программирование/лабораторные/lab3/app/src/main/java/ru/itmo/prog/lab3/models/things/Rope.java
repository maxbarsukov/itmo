package ru.itmo.prog.lab3.models.things;

import ru.itmo.prog.lab3.interfaces.Binadle;
import ru.itmo.prog.lab3.interfaces.HasCases;
import ru.itmo.prog.lab3.models.people.Person;
import ru.itmo.prog.lab3.models.people.PersonGroup;

import java.util.Objects;

public class Rope implements Binadle<Person, PersonGroup>, HasCases {
  public static class NothingIsBindedException extends RuntimeException {
    public NothingIsBindedException(String message) {
      super(message);
    }
  }

  private boolean binded = false;
  private Person person;
  private PersonGroup personGroup;

  @Override
  public void bind(Person person, PersonGroup personGroup) {
    this.person = person;
    this.personGroup = personGroup;
    this.binded = true;
  }

  @Override
  public void unbind() {
    this.person = null;
    this.personGroup = null;
    this.binded = false;
  }

  public String pull() {
    checkBinded();
    return personGroup.description() + " принялись тянуть " + genitiveCase() + " и притянули " + person.genitiveCase();
  }

  public String pull(String whereToPull) {
    checkBinded();
    return pull() + " " + whereToPull;
  }

  public String pullBack() {
    checkBinded();
    return "притянуть " + person.pronunciation() + " на веревке обратно";
  }

  public void checkBinded() {
    if (!binded) throw new NothingIsBindedException("Эта веревка ни к чему не привязана, некому ее тянуть");
  }

  @Override
  public String dativeCase() {
    return "веревке";
  }

  @Override
  public String genitiveCase() {
    return "веревку";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rope rope = (Rope) o;
    return binded == rope.binded && Objects.equals(person, rope.person) && Objects.equals(personGroup, rope.personGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(binded, person, personGroup);
  }

  @Override
  public String toString() {
    return "Rope{" +
      "binded=" + binded +
      ", person=" + person +
      ", personGroup=" + personGroup +
      '}';
  }
}
