package lab1.task3.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoorTest {

  private Person person;

  @BeforeEach
  public void setUp() {
    person = new Person("Открыватель");
  }

  @Test
  void testDoorCreation() {
    Door door = new Door("дверь");
    assertEquals("дверь", door.getName());
    assertFalse(door.isOpen());
  }

  @Test
  void testOpenDoor() {
    Door door = new Door("дверь");
    door.open(person);
    assertTrue(door.isOpen());
  }

  @Test
  void testOpenAlreadyOpenDoor() {
    Door door = new Door("дверь");
    door.open(person);
    door.open(person);
    assertTrue(door.isOpen());
  }
}
