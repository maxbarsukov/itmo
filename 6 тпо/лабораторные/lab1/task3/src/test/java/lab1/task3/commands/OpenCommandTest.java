package lab1.task3.commands;

import lab1.task3.models.Door;
import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class OpenCommandTest {

  @Test
  void testOpenCommandExecution() {
    Person person = new Person("Форд");
    Door door = new Door("дверь");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    OpenCommand openCommand = new OpenCommand(person, door);
    openCommand.execute();

    String expectedOutput = "Форд открыл дверь.\n";
    assertEquals(expectedOutput, outputStream.toString());
    assertTrue(door.isOpen());
  }

  @Test
  void testOpenNonOpenableObject() {
    Person person = new Person("Форд");
    SceneObject object = new SceneObject("рука");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    OpenCommand openCommand = new OpenCommand(person, object);
    openCommand.execute();

    String expectedOutput = "рука нельзя открыть.\n";
    assertEquals(expectedOutput, outputStream.toString());
  }
}
