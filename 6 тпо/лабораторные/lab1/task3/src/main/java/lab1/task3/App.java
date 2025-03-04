package lab1.task3;

import lab1.task3.commands.*;
import lab1.task3.controllers.Scenario;
import lab1.task3.models.*;

public class App {

  public static void main(String[] args) {
    var trillian = new Person("Триллиан");
    var ford = new Person("Форд");
    var zaphod = new Person("Зафод");
    var arthur = new Person("Артур");
    var rodents = new Person("Воздухоплавающие грызуны");

    var door = new Door("дверь");
    var hand = new SceneObject("рука");

    var scenario = new Scenario() {{
      addCommand(new GrabCommand(trillian, hand));
      addCommand(new PullCommand(trillian, arthur, door));
      addCommand(new OpenCommand(ford, door));
      addCommand(new OpenCommand(zaphod, door));
      addCommand(new HypnotizeCommand(rodents, arthur));
    }};

    scenario.execute();
  }
}
