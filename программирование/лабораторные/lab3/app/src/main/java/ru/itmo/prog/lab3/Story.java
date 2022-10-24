package ru.itmo.prog.lab3;

import ru.itmo.prog.lab3.models.Action;
import ru.itmo.prog.lab3.models.JumpDistance;
import ru.itmo.prog.lab3.models.Time;
import ru.itmo.prog.lab3.models.people.*;
import ru.itmo.prog.lab3.models.things.*;
import ru.itmo.prog.lab3.models.weather.Weather;
import ru.itmo.prog.lab3.models.weather.Wind;
import ru.itmo.prog.lab3.utils.Direction;
import ru.itmo.prog.lab3.utils.Sentence;

import java.util.ArrayList;

public class Story {
  public static void main(String[] args) {
    var DISTANCE_FROM_SHORTY_TO_GAZOBO = 9;

    var shorties = new PersonGroup(new ArrayList<Person>() {{
      add(new Shorty("Пончик", Sex.MALE, 75.4));
      add(new Shorty("Пилюлькин", Sex.MALE, 45.5));
    }});

    var knowItAll = new Shorty("Знайка", Sex.MALE, 56.99, JumpDistance.BIG.getDistance());
    var smallScrew = new Shorty("Винтик", Sex.MALE, 67.0);

    var gazebo = new Gazebo(new PersonGroup(new ArrayList<Person>() {{
      add(new Shorty("Синеглазка", Sex.FEMALE, 43.0));
    }}));

    new Sentence(
      knowItAll.jumpTo(gazebo, DISTANCE_FROM_SHORTY_TO_GAZOBO)
    ).and(Action.LOOKED_INSIDE.getDescription(knowItAll)).print();

    new Sentence(
      gazebo.findShorty(smallScrew)
    ).print();

    var rope = new Rope();
    rope.bind(knowItAll, shorties);

    new Sentence(
      rope.pull(new Direction(Direction.Type.BACKWARD, Direction.Preposition.TO, new House().dativeCase()).toString())
    ).print();

    var roof = new Roof();
    var weather = new Weather();
    weather.setWind(new Wind("порыв ветра", 100));

    new Sentence(
      knowItAll.climb(new Downpipe(knowItAll.calculateTimeToClimb()), roof) + new Direction(Direction.Type.NONE, Direction.Preposition.ON, roof.dativeCase())
    ).and(
      knowItAll.wantTo(Action.LOOK_AROUND, Time.PAST)
    ).but(
      weather.getWind().swoopInSuddenly(knowItAll)
    ).and(
      weather.getWind().carryAside(knowItAll)
    ).print();

    new Sentence(
      knowItAll.checkFear("Это")
    ).because(
      Action.KNEW.getDescription(knowItAll)
    ).that(
      shorties.can(rope::pullBack)
    ).print();
  }
}
