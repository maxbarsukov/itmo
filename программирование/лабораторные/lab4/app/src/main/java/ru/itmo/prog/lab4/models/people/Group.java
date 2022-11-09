package ru.itmo.prog.lab4.models.people;

import ru.itmo.prog.lab4.interfaces.Cannable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class Group <M extends Person> implements Cannable {
  private List<M> members;

  public Group(List<M> members) {
    setMembers(members);
    this.members = members;
  }

  public boolean contains(M member) {
    return members.contains(member);
  }

  public List<M> getMembers() {
    return members;
  }

  public void setMembers(List<M> members) {
    this.members = members;
  }

  public void addMember(M member) {
    this.members.add(member);
  }

  public String takeAlarm() {
    return "Все всполошились";
  }

  @Override
  public String can(Supplier<String> action) {
    return description().toLowerCase() + " в любой момент могут " + action.get();
  }

  public String track(M somebody) {
    return description() + " с напряжением следили за " + somebody.possessivePronoun() + " действиями";
  }

  public String description() {
    if (members.size() == 1) {
      return members.get(0).getSingularName();
    } else if (members.size() == 0) {
      return "Никто";
    } else {
      return members.get(0).getPluralName();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Group<?> group = (Group<?>) o;
    return Objects.equals(members, group.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(members);
  }


}
