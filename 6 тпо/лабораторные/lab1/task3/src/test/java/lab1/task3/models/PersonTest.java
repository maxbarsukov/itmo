package lab1.task3.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

  @Test
  void testPersonCreation() {
    Person person = new Person("Артур");
    assertEquals("Артур", person.getName());
    assertFalse(person.isHypnotized());
  }

  @Test
  void testSetHypnotized() {
    Person person = new Person("Артур");
    person.setHypnotized(true);
    assertTrue(person.isHypnotized());
  }
}
