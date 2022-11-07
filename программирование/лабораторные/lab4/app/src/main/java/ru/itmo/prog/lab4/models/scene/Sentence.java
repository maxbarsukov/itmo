package ru.itmo.prog.lab4.models.scene;

import java.util.Objects;

public class Sentence {
  private final String content;
  private final String punctuationMark;

  public Sentence(String content) {
    this.content = content;
    this.punctuationMark = ".";
  }

  public Sentence(String content, String punctuationMark) {
    this.content = content;
    this.punctuationMark = punctuationMark;
  }

  public void print() {
    System.out.println(content + punctuationMark);
  }


  public Sentence and(String text) {
    return new Sentence(content + " и " + text);
  }

  public Sentence but(String text) {
    return comma("но " + text);
  }

  public Sentence because(String text) {
    return comma("так как " + text);
  }

  public Sentence that(String text) {
    return comma("что " + text);
  }

  public Sentence which(String text) {
    return comma("который " + text);
  }

  public Sentence like(String text) {
    return comma("словно " + text);
  }

  public Sentence thatTime(String text) {
    return comma("который в это время " + text);
  }

  public Sentence however(String text) {
    return comma("однако, " + text);
  }

  public Sentence comma(String text) {
    return new Sentence(content + ", " + text);
  }

  public Sentence ellipsis() {
    return new Sentence(content + "..");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sentence sentence = (Sentence) o;
    return Objects.equals(content, sentence.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public String toString() {
    return "Sentence{" +
      "content='" + content + '\'' +
      '}';
  }
}
