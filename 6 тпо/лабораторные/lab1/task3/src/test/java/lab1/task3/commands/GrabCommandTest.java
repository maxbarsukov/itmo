package lab1.task3.commands;

import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class GrabCommandTest {

  @Test
  void testGrabCommandExecution() {
    Person person = new Person("Триллиан");
    SceneObject object = new SceneObject("рука");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    GrabCommand grabCommand = new GrabCommand(person, object);
    grabCommand.execute();

    String expectedOutput = "Триллиан схватил за рука.\n";
    assertEquals(expectedOutput, outputStream.toString());
  }
}
