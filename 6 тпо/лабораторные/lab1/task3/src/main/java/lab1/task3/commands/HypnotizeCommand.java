package lab1.task3.commands;

import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;

import java.util.Objects;

public class HypnotizeCommand implements Command {

  private final SceneObject subject;
  private final Person object;

  public HypnotizeCommand(SceneObject subject, Person object) {
    this.subject = Objects.requireNonNull(subject, "Субъект не может быть null");
    this.object = Objects.requireNonNull(object, "Объект не может быть null");
  }

  @Override
  public void execute() {
    object.setHypnotized(true);
    System.out.println(subject.getName() + " загипнотизировал " + object.getName() + ".");
  }
}
