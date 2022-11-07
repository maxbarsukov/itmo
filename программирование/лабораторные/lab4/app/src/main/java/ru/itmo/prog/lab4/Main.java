package ru.itmo.prog.lab4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.itmo.prog.lab4.models.common.Action;
import ru.itmo.prog.lab4.models.common.JumpDistance;
import ru.itmo.prog.lab4.models.common.Time;
import ru.itmo.prog.lab4.models.people.*;
import ru.itmo.prog.lab4.models.places.*;
import ru.itmo.prog.lab4.models.scene.Scene;
import ru.itmo.prog.lab4.models.scene.Story;
import ru.itmo.prog.lab4.models.things.Rope;
import ru.itmo.prog.lab4.models.weather.Weather;
import ru.itmo.prog.lab4.modules.HouseModule;
import ru.itmo.prog.lab4.modules.RoofModule;
import ru.itmo.prog.lab4.modules.StoryModule;
import ru.itmo.prog.lab4.modules.WeatherModule;
import ru.itmo.prog.lab4.utils.Direction;
import ru.itmo.prog.lab4.models.scene.Sentence;

import java.util.ArrayList;

public class Main {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new StoryModule());
    var story = injector.getInstance(Story.class);

    var scene = new Scene();

    scene.setMainCharacter(new Shorty("Знайка", Sex.MALE, 56.99, JumpDistance.BIG.getDistance()));
    scene.addCharacter(new Shorty("Винтик", Sex.MALE, 67.0));

    scene.addCharacterGroup("коротышки", new Group<>(new ArrayList<>() {{
      add(new Shorty("Пончик", Sex.MALE, 75.4));
      add(new Shorty("Пилюлькин", Sex.MALE, 45.5));
    }}));

    var house = Guice.createInjector(new HouseModule()).getInstance(House.class);
    var roof = Guice.createInjector(new RoofModule()).getInstance(Roof.class);

    scene.addLocation(roof);
    scene.addLocation(house);
    scene.addLocation(new Downpipe(scene.getMainCharacter().calculateTimeToClimb()));
    scene.addLocation(new Gazebo(new Group<>(new ArrayList<>() {{
      add(new Shorty("Синеглазка", Sex.FEMALE, 43.0));
    }})));

    var rope = new Rope();
    rope.bind(scene.getMainCharacter(), scene.getCharactersGroup("коротышки"));

    Injector weatherInjector = Guice.createInjector(new WeatherModule());
    var weather = weatherInjector.getInstance(Weather.class);

    story.addSentence(
      new Sentence(
        ((Shorty) scene.getMainCharacter()).jumpTo(
          scene.getLocation(Gazebo.DEFAULT_NAME),
          scene.getMainCharacter().distanceTo(scene.getLocation(Gazebo.DEFAULT_NAME))
        )
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
            house.dativeCase()
          ).toString()
        )
      )
    );

    story.addSentence(
      new Sentence(
        scene.getMainCharacter().climb(
          (Downpipe) scene.getLocation(Downpipe.DEFAULT_NAME), scene.getLocation(Roof.DEFAULT_NAME)
        ) + new Direction(
          Direction.Type.NONE, Direction.Preposition.ON, scene.getLocation(Roof.DEFAULT_NAME).dativeCase()
        )
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
