package ru.itmo.prog.lab3;

import ru.itmo.prog.lab3.models.Action;
import ru.itmo.prog.lab3.models.JumpDistance;
import ru.itmo.prog.lab3.models.Time;
import ru.itmo.prog.lab3.models.people.*;
import ru.itmo.prog.lab3.models.places.*;
import ru.itmo.prog.lab3.models.scene.Scene;
import ru.itmo.prog.lab3.models.scene.Story;
import ru.itmo.prog.lab3.models.things.Rope;
import ru.itmo.prog.lab3.models.weather.Weather;
import ru.itmo.prog.lab3.models.weather.Wind;
import ru.itmo.prog.lab3.utils.Direction;
import ru.itmo.prog.lab3.models.scene.Sentence;

import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {
    var scene = new Scene();

    scene.setMainCharacter(new Shorty("Знайка", Sex.MALE, 56.99, JumpDistance.BIG.getDistance()));
    scene.addCharacter(new Shorty("Винтик", Sex.MALE, 67.0));

    scene.addCharacterGroup("коротышки", new Group<>(new ArrayList<>() {{
      add(new Shorty("Пончик", Sex.MALE, 75.4));
      add(new Shorty("Пилюлькин", Sex.MALE, 45.5));
    }}));

    scene.addLocation(new Gazebo(new Group<>(new ArrayList<>() {{
      add(new Shorty("Синеглазка", Sex.FEMALE, 43.0));
    }})));

    final var DISTANCE_FROM_SHORTY_TO_GAZOBO = 9;

    var rope = new Rope();
    var roof = new Roof();
    var weather = new Weather();

    rope.bind(scene.getMainCharacter(), scene.getCharactersGroup("коротышки"));
    weather.setWind(new Wind("порыв ветра", 100));

    var story = new Story("Приключения Незнайки. Отрывок");

    story.addSentence(
      new Sentence(
        ((Shorty) scene.getMainCharacter()).jumpTo(scene.getLocation(Gazebo.DEFAULT_NAME), DISTANCE_FROM_SHORTY_TO_GAZOBO)
      ).and(Action.LOOKED_INSIDE.getDescription(scene.getMainCharacter()))
    );

    story.addSentence(
      new Sentence(
        ((Gazebo) scene.getLocation(Gazebo.DEFAULT_NAME)).findShorty(
          (Shorty) scene.getCharacter("Винтик")
        )
      )
    );

    story.addSentence(
      new Sentence(
        rope.pull(
          new Direction(
            Direction.Type.BACKWARD, Direction.Preposition.TO,
            new House().dativeCase()
          ).toString()
        )
      )
    );

    story.addSentence(
      new Sentence(
        scene.getMainCharacter().climb(
          new Downpipe(scene.getMainCharacter().calculateTimeToClimb()), roof)
          + new Direction(Direction.Type.NONE, Direction.Preposition.ON, roof.dativeCase())
      ).and(
        scene.getMainCharacter().wantTo(Action.LOOK_AROUND, Time.PAST)
      ).but(
        weather.getWind().swoopInSuddenly(scene.getMainCharacter())
      ).and(
        weather.getWind().carryAside(scene.getMainCharacter())
      )
    );

    story.addSentence(
      new Sentence(
        scene.getMainCharacter().checkFear("Это")
      ).because(
        Action.KNEW.getDescription(scene.getMainCharacter())
      ).that(
        scene.getCharactersGroup("коротышки").can(rope::pullBack)
      )
    );

    scene.setStory(story);
    scene.play();
  }
}
