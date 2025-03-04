package lab1.task3.commands;

import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;

import java.util.Objects;

public class PullCommand implements Command {

  private final Person subject;
  private final SceneObject object1;
  private final SceneObject object2;

  public PullCommand(Person subject, SceneObject object1, SceneObject object2) {
    this.subject = Objects.requireNonNull(subject, "Субъект не может быть null");
    this.object1 = Objects.requireNonNull(object1, "Объект не может быть null");
    this.object2 = Objects.requireNonNull(object2, "Объект не может быть null");
  }

  @Override
  public void execute() {
    System.out.println(subject.getName() + " потянул " + object1.getName() + " к " + object2.getName() + ".");
  }
}
