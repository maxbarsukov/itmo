package ru.itmo.prog.lab4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.itmo.prog.lab4.lib.events.EventBusImpl;
import ru.itmo.prog.lab4.models.common.Action;
import ru.itmo.prog.lab4.models.common.JumpDistance;
import ru.itmo.prog.lab4.models.common.Time;
import ru.itmo.prog.lab4.models.events.OrderGiven;
import ru.itmo.prog.lab4.models.events.WordsSpokenEvent;
import ru.itmo.prog.lab4.models.people.*;
import ru.itmo.prog.lab4.models.places.*;
import ru.itmo.prog.lab4.models.scene.Scene;
import ru.itmo.prog.lab4.models.scene.Story;
import ru.itmo.prog.lab4.models.things.Rope;
import ru.itmo.prog.lab4.models.weather.Weather;
import ru.itmo.prog.lab4.modules.HouseModule;
import ru.itmo.prog.lab4.modules.SceneModule;
import ru.itmo.prog.lab4.modules.StoryModule;
import ru.itmo.prog.lab4.modules.WeatherModule;
import ru.itmo.prog.lab4.utils.Direction;
import ru.itmo.prog.lab4.models.scene.Sentence;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
  public static void main(String[] args) {
    var injector = Guice.createInjector(new StoryModule());
    var story = injector.getInstance(Story.class);

    var sceneInjector = Guice.createInjector(new SceneModule());
    var scene = sceneInjector.getInstance(Scene.class);
    sceneInjector.injectMembers(new EventBusImpl());

    setupCharacters(scene);
    setupLocations(scene);

    var house = (House)scene.getLocation(House.DEFAULT_NAME);

    var bus = scene.getEventBus();
    bus.subscribe(new WordsSpokenEvent.Handler(scene.getMainCharacter()));
    bus.subscribe(new OrderGiven.Handler());

    var rope = new Rope();
    try {
      rope.pull("куда-нибудь");
    } catch (Rope.NothingIsBindedException e) {
      rope.bind(scene.getMainCharacter(), scene.getCharactersGroup("коротышки"));
    }

    Injector weatherInjector = Guice.createInjector(new WeatherModule());
    var weather = weatherInjector.getInstance(Weather.class);

    AtomicReference<String> hearedWords = new AtomicReference<>("услыхал слова ");
    bus.publish(
      new WordsSpokenEvent(
        scene.getCharacter("Шпунтик"),
        new WordsSpokenEvent.WordsSpoken("бла-бла-бла", 90)
      ),
      (event, handler) -> {
        var e = (WordsSpokenEvent)event;
        hearedWords.set(hearedWords.get() + e.getWhoSaid().genitiveCase() + ": \"" + e.getWordsSpoken().getContent() + '"');
      },
      (event, handler, exception) -> {
        hearedWords.set(hearedWords.get() + ((WordsSpokenEvent)event).getWhoSaid().genitiveCase());
      }
    );

    story.addSentence(
      new Sentence(scene.getCharacter("Незнайка").getName())
        .thatTime(
          new Direction(
            Direction.Type.LOOKED,
            Direction.Preposition.IN,
            new Place("коридор") {
              @Override public String dativeCase() { return "коридору"; }
              @Override public String genitiveCase() { return "коридора"; }
            }.getName()
          ).toString()
        ).comma(hearedWords.get())
    );

    story.addSentence(
      new Sentence("Все всполошились и бросились к выходу")
    );

    bus.publish(
      new OrderGiven(OrderGiven.TimeToExecute.LOW),
      (event, handler) -> {
        story.addSentence(new Sentence(((OrderGiven.Handler) handler).description((OrderGiven) event)));
      },
      (event, handler, exception) -> {
        story.addSentence(new Sentence(exception.getMessage()));
      }
    );

    story.addSentence(
      new Sentence("Знайка обвязал один конец веревки вокруг пояса, а другой конец привязал к дверной ручке и строго сказал", ":")
    );

    story.addSentence(
      new Sentence("Придав своему телу наклонное положение").comma("Знайка с силой оттолкнулся ногами от порога и полетел в направлении мастерской, которая находилась неподалеку от дома")
    );

    story.addSentence(
      new Sentence("Он немного не рассчитал толчка и поднялся выше, чем было надо")
    );

    story.addSentence(
      new Sentence("Пролетая над мастерской").comma("он ухватился рукой за флюгер, который показывал направление ветра")
    );

    story.addSentence(
      new Sentence("Это задержало полет")
    );

    story.addSentence(
      new Sentence("Спустившись по водосточной трубе").comma("Знайка отворил дверь и проник в мастерскую")
    );

    story.addSentence(
      new Sentence("Коротышки с напряжением следили за его действиями")
    );

    story.addSentence(
      new Sentence("Через минуту Знайка выглянул из мастерской")
    );

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
          (Downpipe) scene.getLocation(Downpipe.DEFAULT_NAME), scene.getLocation(house.getRoof().getName())
        ) + new Direction(
          Direction.Type.NONE, Direction.Preposition.ON, scene.getLocation(house.getRoof().getName()).dativeCase()
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

    story.addSentence(
      new Sentence("Ему, однако, не удалось ничего разглядеть, так как в следующий момент произошло то, чего никто не ожидал")
    );

    story.addSentence(
      new Sentence("Не долетев до забора, Знайка вдруг начал стремительно падать, словно какая-то сила неожиданно потянула его вниз")
    );

    story.addSentence(
      new Sentence("Шлепнувшись с размаху о землю, он растянулся во весь рост и не успел даже сообразить, что произошло")
    );

    story.addSentence(
      new Sentence("Ощущая во всем теле страшную тяжесть, он с трудом поднялся на ноги и огляделся по сторонам")
    );

    story.addSentence(
      new Sentence("Его удивило, что он снова твердо держится на ногах")
    );

    story.addSentence(
      new Sentence("Он попробовал поднять руку, потом другую, попробовал сделать шаг, другой", "...")
    );

    story.addSentence(
      new Sentence("Руки и ноги повиновались с трудом, словно были свинцом налиты")
    );

    scene.setStory(story);
    scene.play();
  }

  private static void setupCharacters(Scene scene) {
    scene.setMainCharacter(new Shorty("Знайка", Sex.MALE, 56.99, JumpDistance.BIG.getDistance()));
    scene.addCharacter(new Shorty("Незнайка", Sex.MALE, 58.9, JumpDistance.BIG.getDistance()));
    scene.addCharacter(new Shorty("Винтик", Sex.MALE, 67.0));
    scene.addCharacter(new Shorty("Шпунтик", Sex.MALE, 56.0));

    scene.addCharacterGroup("коротышки", new Group<>(new ArrayList<>() {{
      add(new Shorty("Пончик", Sex.MALE, 75.4));
      add(new Shorty("Пилюлькин", Sex.MALE, 45.5));
    }}));

    scene.addCharacterGroup("все", scene.getCharactersGroup("коротышки"));
  }

  private static void setupLocations(Scene scene) {
    var house = Guice.createInjector(new HouseModule()).getInstance(House.class);
    house.setRoof(house.new Roof(House.Roof.DEFAULT_NAME));

    scene.addLocation(house.getRoof());
    scene.addLocation(house);
    scene.addLocation(new Downpipe(scene.getMainCharacter().calculateTimeToClimb()));
    scene.addLocation(new Gazebo(new Group<>(new ArrayList<>() {{
      add(new Shorty("Синеглазка", Sex.FEMALE, 43.0));
    }})));
  }
}
