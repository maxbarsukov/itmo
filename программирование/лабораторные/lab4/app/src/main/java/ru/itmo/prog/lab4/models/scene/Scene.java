package ru.itmo.prog.lab4.models.scene;

import com.google.inject.Inject;
import ru.itmo.prog.lab4.interfaces.Tellable;
import ru.itmo.prog.lab4.interfaces.events.EventBus;
import ru.itmo.prog.lab4.models.people.Group;
import ru.itmo.prog.lab4.models.people.Person;
import ru.itmo.prog.lab4.models.places.Place;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scene {
  private Person mainCharacter;
  private Tellable story;
  private EventBus eventBus;

  private Map<String, Place> locations;
  private Map<String, Person> characters;
  private Map<String, Group<Person>> characterGroups;

  @Inject
  public Scene() {
    this.locations = new HashMap<>();
    this.characters = new HashMap<>();
    this.characterGroups = new HashMap<>();
  }

  public Scene(
    Person mainCharacter,
    Tellable story,
    Map<String, Place> locations,
    Map<String, Person> characters,
    Map<String, Group<Person>> characterGroups
  ) {
    this.mainCharacter = mainCharacter;
    this.story = story;
    this.locations = locations;
    this.characters = characters;
    this.characterGroups = characterGroups;
  }

  public void play() {
    story.tell();
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  @Inject
  public void setEventBus(EventBus eventBus){
    this.eventBus = eventBus;
  }

  public Person getCharacter(String name) {
    return characters.get(name);
  }

  public Place getLocation(String name) {
    return locations.get(name);
  }

  public Group<Person> getCharactersGroup(String name) {
    return characterGroups.get(name);
  }

  public void addCharacter(Person person) {
    characters.put(person.getName(), person);
  }

  public void addCharacterGroup(String groupName, Group<Person> group) {
    characterGroups.put(groupName, group);
  }

  public void addLocation(Place place) {
    locations.put(place.getName(), place);
  }

  public Person getMainCharacter() {
    return mainCharacter;
  }

  public void setMainCharacter(Person mainCharacter) {
    this.mainCharacter = mainCharacter;
  }

  public Tellable getStory() {
    return story;
  }

  public void setStory(Tellable story) {
    this.story = story;
  }

  public Map<String, Place> getLocations() {
    return locations;
  }

  public void setLocations(Map<String, Place> locations) {
    this.locations = locations;
  }

  public Map<String, Person> getCharacters() {
    return characters;
  }

  public void setCharacters(Map<String, Person> characters) {
    this.characters = characters;
  }

  public Map<String, Group<Person>> getCharacterGroups() {
    return characterGroups;
  }

  public void setCharacterGroups(Map<String, Group<Person>> characterGroups) {
    this.characterGroups = characterGroups;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Scene scene = (Scene) o;
    return Objects.equals(mainCharacter, scene.mainCharacter) && Objects.equals(story, scene.story) && Objects.equals(locations, scene.locations) && Objects.equals(characters, scene.characters) && Objects.equals(characterGroups, scene.characterGroups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mainCharacter, story, locations, characters, characterGroups);
  }

  @Override
  public String toString() {
    return "Scene{" +
      "mainCharacter=" + mainCharacter +
      ", story=" + story +
      ", locations=" + locations +
      ", characters=" + characters +
      ", characterGroups=" + characterGroups +
      '}';
  }
}
