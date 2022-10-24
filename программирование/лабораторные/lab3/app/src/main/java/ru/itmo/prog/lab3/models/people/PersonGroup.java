package ru.itmo.prog.lab3.models.people;

import ru.itmo.prog.lab3.interfaces.Cannable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class PersonGroup implements Cannable {
  private List<Person> people;

  public PersonGroup(List<Person> people) {
    setPeople(people);
    this.people = people;
  }

  public boolean contains(Person person) {
    return people.contains(person);
  }

  public List<Person> setPeople() {
    return people;
  }

  public void setPeople(List<Person> people) {
    this.people = people;
  }

  @Override
  public String can(Supplier<String> function) {
    return description().toLowerCase() + " в любой момент могут " + function.get();
  }

  public String description() {
    if (people.size() == 1) {
      return people.get(0).getSingularName();
    } else if (people.size() == 0) {
      return "Никто";
    } else {
      return people.get(0).getPluralName();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PersonGroup that = (PersonGroup) o;
    return people.equals(that.people);
  }

  @Override
  public int hashCode() {
    return Objects.hash(people);
  }

  @Override
  public String toString() {
    return "PersonGroup{" +
      "people=" + people +
      '}';
  }
}
