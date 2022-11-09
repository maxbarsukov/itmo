package ru.itmo.prog.lab4.models.events;

import ru.itmo.prog.lab4.interfaces.Hearable;
import ru.itmo.prog.lab4.lib.events.interfaces.Event;
import ru.itmo.prog.lab4.lib.events.EventException;
import ru.itmo.prog.lab4.lib.events.EventHandler;
import ru.itmo.prog.lab4.models.people.Person;

import java.util.Objects;

public class WordsSpokenEvent implements Event {
  public static class Handler extends EventHandler<WordsSpokenEvent> {
    private final Person whoHeared;

    public Handler(Person whoHeared) {
      this.whoHeared = whoHeared;
    }

    @Override
    public void handle(WordsSpokenEvent event) throws EventException {
      if (!whoHeared.hear(event.getWordsSpoken())) {
        throw new EventException("Кто-то что-то бормочет");
      }
    }
  }

  public static class WordsSpoken implements Hearable {
    private final String content;
    private final int volume;

    public WordsSpoken(String content, int volume) {
      this.content = content;
      this.volume = volume;
    }

    @Override
    public int getVolume() {
      return volume;
    }

    @Override
    public String getContent() {
      return content;
    }
  }

  private final Person whoSaid;
  private final WordsSpoken wordsSpoken;

  public WordsSpokenEvent(Person whoSaid, WordsSpoken wordsSpoken) {
    this.whoSaid = whoSaid;
    this.wordsSpoken = wordsSpoken;
  }

  public Person getWhoSaid() {
    return whoSaid;
  }

  public WordsSpoken getWordsSpoken() {
    return wordsSpoken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WordsSpokenEvent that = (WordsSpokenEvent) o;
    return Objects.equals(whoSaid, that.whoSaid) && Objects.equals(wordsSpoken, that.wordsSpoken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(whoSaid, wordsSpoken);
  }

  @Override
  public String toString() {
    return whoSaid.getName() + " говорит: \"" + wordsSpoken.getContent() + '"';
  }
}
