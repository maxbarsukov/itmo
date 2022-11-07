package ru.itmo.prog.lab4.models.things;

import ru.itmo.prog.lab4.interfaces.Binadle;
import ru.itmo.prog.lab4.interfaces.HasCases;
import ru.itmo.prog.lab4.models.people.Person;
import ru.itmo.prog.lab4.models.people.Group;

import java.util.Objects;

public class Rope implements Binadle<Person, Group<? extends Person>>, HasCases {
  public static class NothingIsBindedException extends RuntimeException {
    public NothingIsBindedException(String message) {
      super(message);
    }
  }

  private boolean binded = false;
  private Person person;
  private Group<? extends Person> group;

  @Override
  public void bind(Person person, Group<? extends Person> group) {
    this.person = person;
    this.group = group;
    this.binded = true;
  }

  @Override
  public void unbind() {
    this.person = null;
    this.group = null;
    this.binded = false;
  }

  public String pull() {
    checkBinded();
    return group.description() + " принялись тянуть " + genitiveCase() + " и притянули " + person.genitiveCase();
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
    return binded == rope.binded && Objects.equals(person, rope.person) && Objects.equals(group, rope.group);
  }

  @Override
  public int hashCode() {
    return Objects.hash(binded, person, group);
  }

  @Override
  public String toString() {
    return "Rope{" +
      "binded=" + binded +
      ", person=" + person +
      ", personGroup=" + group +
      '}';
  }
}
