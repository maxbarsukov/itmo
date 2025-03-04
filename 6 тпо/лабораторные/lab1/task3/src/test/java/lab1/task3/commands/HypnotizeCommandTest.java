package lab1.task3.commands;

import lab1.task3.models.Person;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class HypnotizeCommandTest {

  @Test
  void testHypnotizeCommandExecution() {
    Person person = new Person("Воздухоплавающие грызуны");
    Person object = new Person("Артур");

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    HypnotizeCommand hypnotizeCommand = new HypnotizeCommand(person, object);
    hypnotizeCommand.execute();

    String expectedOutput = "Воздухоплавающие грызуны загипнотизировал Артур.\n";
    assertEquals(expectedOutput, outputStream.toString());
    assertTrue(object.isHypnotized());
  }
}
