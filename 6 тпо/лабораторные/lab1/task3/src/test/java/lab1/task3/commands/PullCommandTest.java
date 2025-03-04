package lab1.task3.commands;

import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class PullCommandTest {

  @Test
  void testPullCommandExecution() {
    Person person1 = new Person("Триллиан");
    Person person2 = new Person("Артур");
    SceneObject object = new SceneObject("дверь");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    PullCommand pullCommand = new PullCommand(person1, person2, object);
    pullCommand.execute();

    String expectedOutput = "Триллиан потянул Артур к дверь.\n";
    assertEquals(expectedOutput, outputStream.toString());
  }
}
