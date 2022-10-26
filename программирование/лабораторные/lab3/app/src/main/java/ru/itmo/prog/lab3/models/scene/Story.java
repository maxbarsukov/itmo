package ru.itmo.prog.lab3.models.scene;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ru.itmo.prog.lab3.interfaces.Tellable;

import java.util.*;

public class Story implements Tellable {
  private List<Sentence> sentences;

  @Inject @Named("Title")
  private String title;

  public Story() {
    this.sentences = new ArrayList<>(Collections.emptyList());
  }

  @Override
  public void tell() {
    for (var sentence : sentences) {
      sentence.print();
    }
  }

  public void addSentence(Sentence sentence) {
    sentences.add(sentence);
  }

  public List<Sentence> getSentences() {
    return sentences;
  }

  public void setSentences(List<Sentence> sentences) {
    this.sentences = sentences;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Story story = (Story) o;
    return Objects.equals(sentences, story.sentences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sentences);
  }

  @Override
  public String toString() {
    return "Story{" +
      "sentences=" + sentences +
      '}';
  }
}
