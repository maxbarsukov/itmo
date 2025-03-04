package lab1.task3.commands;

import lab1.task3.interfaces.Openable;
import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;

import java.util.Objects;

public class OpenCommand implements Command {

  private final Person subject;
  private final SceneObject object;

  public OpenCommand(Person subject, SceneObject object) {
    this.subject = Objects.requireNonNull(subject, "Субъект не может быть null");
    this.object = Objects.requireNonNull(object, "Объект не может быть null");
  }

  @Override
  public void execute() {
    if (object instanceof Openable) {
      ((Openable) object).open(subject);
    } else {
      System.out.println(object.getName() + " нельзя открыть.");
    }
  }
}
