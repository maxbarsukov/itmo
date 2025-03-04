package lab1.task3.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SceneObjectTest {

  @Test
  void testSceneObjectCreation() {
    SceneObject object = new SceneObject("рука");
    assertEquals("рука", object.getName());
  }
}
