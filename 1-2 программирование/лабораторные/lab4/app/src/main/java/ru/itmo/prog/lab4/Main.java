package ru.itmo.prog.lab4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.itmo.prog.lab4.models.common.*;
import ru.itmo.prog.lab4.models.events.*;
import ru.itmo.prog.lab4.models.people.*;
import ru.itmo.prog.lab4.models.places.*;
import ru.itmo.prog.lab4.models.scene.*;
import ru.itmo.prog.lab4.models.things.*;
import ru.itmo.prog.lab4.models.weather.Weather;
import ru.itmo.prog.lab4.modules.*;
import ru.itmo.prog.lab4.utils.Direction;
import ru.itmo.prog.lab4.models.scene.Sentence;
import ru.itmo.prog.lab4.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
  public static void main(String[] args) {
    var injector = Guice.createInjector(new StoryModule());
    var story = injector.getInstance(Story.class);

    var sceneInjector = Guice.createInjector(new SceneModule());
    var scene = sceneInjector.getInstance(Scene.class);

    setupCharacters(scene);
    setupLocations(scene);

    var house = (House)scene.getLocation(House.DEFAULT_NAME);
    var mainCharacter = (Shorty) scene.getMainCharacter();

    var ebb =  Guice.createInjector(new EventBusBeanModule()).getInstance(EventBusBean.class);
    ebb.bus().subscribe(new WordsSpokenEvent.Handler(mainCharacter));

    Injector weatherInjector = Guice.createInjector(new WeatherModule());
    var weather = weatherInjector.getInstance(Weather.class);

    AtomicReference<String> hearedWords = new AtomicReference<>("услыхал слова ");
    ebb.bus().publish(
      new WordsSpokenEvent(
        scene.getCharacter("Шпунтик"),
        new WordsSpokenEvent.WordsSpoken("бла-бла-бла", 90)
      ),
      (event, handler) -> {
        var e = (WordsSpokenEvent)event;
        hearedWords.set(hearedWords.get() + e.getWhoSaid().genitiveCase() + ": \"" + e.getWordsSpoken().getContent() + '"');
      },
      (event, handler, exception) -> hearedWords.set(hearedWords.get() + ((WordsSpokenEvent)event).getWhoSaid().genitiveCase())
    );

    class Hallway extends Place {
      public Hallway() { super("коридор"); }
      @Override public String dativeCase() { return "коридору"; }
      @Override public String genitiveCase() { return "коридора"; }
    }

    story.addSentence(
      new Sentence(scene.getCharacter("Незнайка").getName())
        .thatTime(
          new Direction(
            Direction.Type.LOOKED,
            Direction.Preposition.IN,
            (new Hallway()).getName()
          ).toString()
        ).comma(hearedWords.get())
    );

    story.addSentence(
      new Sentence(scene.getCharactersGroup("все").takeAlarm())
        .and(Action.GO_TO_EXIT.getDescription(mainCharacter))
    );

    ebb.bus().publish(
      new OrderGiven(OrderGiven.TimeToExecute.LOW),
      (event, handler) -> story.addSentence(new Sentence(((OrderGiven.Handler) handler).description((OrderGiven) event))),
      (event, handler, exception) -> story.addSentence(new Sentence(exception.getMessage()))
    );

    var rope = new Rope();
    var workshop = (Workshop) scene.getLocation(Workshop.DEFAULT_NAME);
    var doorToWorkshop = new Door(workshop);

    try {
      rope.pull("куда-нибудь");
    } catch (Rope.NothingIsBindedException e) {
      story.addSentence(
        new Sentence(
          rope.bindWith(
            mainCharacter,
            scene.getCharactersGroup("коротышки"),
            doorToWorkshop.new Doorhandle()
          ) + " и " + Action.STRICTLY_SAY.getDescription(mainCharacter),
          ":"
        )
      );
    }

    story.addSentence(
      new Sentence(mainCharacter.jumpTo(workshop))
        .comma(((House) scene.getLocation(House.DEFAULT_NAME)).distanceDescription(workshop))
    );

    story.addSentence(
      new Sentence(mainCharacter.jumpResult())
    );

    var vane = new Vane();
    story.addSentence(
      new Sentence(mainCharacter.locationDescription())
        .comma(mainCharacter.grubHoldOf(vane))
        .which(vane.description())
    );

    story.addSentence(new Sentence("Это задержало полет"));
    story.addSentence(
      new Sentence(mainCharacter.goTo(scene.getLocation(Downpipe.DEFAULT_NAME), UnknownEntity.Direction.DOWN))
        .comma(mainCharacter.goTo(doorToWorkshop))
    );

    story.addSentence(
      new Sentence(scene.getCharactersGroup("коротышки").track(mainCharacter))
    );

    story.addSentence(
      new Sentence(mainCharacter.peekOut(59))
    );

    story.addSentence(
      new Sentence(
        mainCharacter.jumpTo(
          scene.getLocation(Gazebo.DEFAULT_NAME),
          mainCharacter.distanceTo(scene.getLocation(Gazebo.DEFAULT_NAME))
        )
      ).and(Action.LOOKED_INSIDE.getDescription(mainCharacter))
    );

    story.addSentence(
      new Sentence(
        ((Gazebo) scene.getLocation(Gazebo.DEFAULT_NAME)).findShorty((Shorty) scene.getCharacter("Винтик"))
      )
    );

    story.addSentence(
      new Sentence(
        rope.pull(new Direction(Direction.Type.BACKWARD, Direction.Preposition.TO, house.dativeCase()).toString())
      )
    );

    story.addSentence(
      new Sentence(mainCharacter.climb(
          (Downpipe) scene.getLocation(Downpipe.DEFAULT_NAME), scene.getLocation(house.getRoof().getName())
        ) + new Direction(
          Direction.Type.NONE, Direction.Preposition.ON, scene.getLocation(house.getRoof().getName()).dativeCase()
        ))
        .and(mainCharacter.wantTo(Action.LOOK_AROUND, Time.PAST))
        .but(weather.getWind().swoopInSuddenly(mainCharacter))
        .and(weather.getWind().carryAside(mainCharacter)
      )
    );

    story.addSentence(
      new Sentence(mainCharacter.checkFear("Это"))
        .because(Action.KNEW.getDescription(mainCharacter))
        .that(scene.getCharactersGroup("коротышки").can(rope::pullBack))
    );

    AtomicReference<String> spanishInquisitionMessage = new AtomicReference<>("");
    ebb.bus().publish(
      new NoOneExpectedEvent(),
      (event, handler) -> {
        var e = (NoOneExpectedEvent) event;
        spanishInquisitionMessage.set(e.execute() + NoOneExpectedEvent.GOOD_END);
      },
      (event, handler, exception) -> {
        var e = (NoOneExpectedEvent) event;
        spanishInquisitionMessage.set(e.execute() + exception.getMessage());
      }
    );

    story.addSentence(
      new Sentence(Utils.capitalize(mainCharacter.dativePronoun()))
        .however(mainCharacter.tryTo(Action.DISCERN::getDefault))
        .because(spanishInquisitionMessage.get())
    );

    story.addSentence(
      new Sentence(mainCharacter.flyTo(new Place("забор") {
          @Override public String dativeCase() { return getName() + "у"; }
          @Override public String genitiveCase() { return getName() + "а"; }
        },
        () -> String.join(
          " ",
          mainCharacter.getName(),
          Action.SUDDENLY_STARTED.getDescription(mainCharacter),
          Action.PLUMMET.getDescription(mainCharacter)
        )
      )
      ).like((new UnknownEntity("какая-то сила") {}).pull(mainCharacter, UnknownEntity.Direction.DOWN))
    );

    story.addSentence(
      new Sentence(mainCharacter.flopAbout(Earth.getInstance()))
        .comma(mainCharacter.checkStretched())
        .and(mainCharacter.getCurrentImpression().reaction())
    );

    story.addSentence(
      new Sentence(mainCharacter.bodyStatus())
        .comma(mainCharacter.riseToYourFeet())
        .and(Action.LOOKED_AROUND.getDescription(mainCharacter))
    );

    story.addSentence(
      new Sentence(mainCharacter.react())
        .that(mainCharacter.checkStandingAbility("снова"))
    );

    story.addSentence(
      new Sentence(Utils.capitalize(mainCharacter.pronoun()) + ' ' + mainCharacter.tryTo(mainCharacter::warmUpArms))
        .comma(mainCharacter.tryTo(mainCharacter::warmUpLegs))
        .ellipsis()
    );

    story.addSentence(
      new Sentence(mainCharacter.healthStatus())
    );

    scene.setStory(story);
    scene.play();
  }

  private static void setupCharacters(Scene scene) {
    var allKnow = new Shorty("Знайка", Sex.MALE, 56.99, JumpDistance.BIG.getDistance());
    // Знайка много читает
    allKnow.readALot();

    scene.setMainCharacter(allKnow);
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
    scene.addLocation(Guice.createInjector(new WorkshopModule()).getInstance(Workshop.class));
    scene.addLocation(house);
    scene.addLocation(new Downpipe(scene.getMainCharacter().calculateTimeToClimb()));
    scene.addLocation(new Gazebo(new Group<>(new ArrayList<>() {{
      add(new Shorty("Синеглазка", Sex.FEMALE, 43.0));
    }})));
  }
}
