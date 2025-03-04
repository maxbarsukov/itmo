package lab1.task3.controllers;

import lab1.task3.commands.GrabCommand;
import lab1.task3.commands.PullCommand;
import lab1.task3.models.Person;
import lab1.task3.models.SceneObject;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class ScenarioTest {

  @Test
  void testScenarioExecution() {
    Person person = new Person("Триллиан");
    SceneObject hand = new SceneObject("рука");
    SceneObject door = new SceneObject("дверь");

    var grabCommand = new GrabCommand(person, hand);
    var pullCommand = new PullCommand(person, hand, door);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    Scenario scenario = new Scenario();
    scenario.addCommand(grabCommand);
    scenario.addCommand(pullCommand);
    scenario.execute();

    String expectedOutput = "* Триллиан схватил за рука.\n" +
      "* Триллиан потянул рука к дверь.\n";
    assertEquals(expectedOutput, outputStream.toString());
  }
}
