package ru.itmo.prog.lab3;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class StoryTest {
  @Test
  public void appHasAGreeting() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    String expectedOutput  = "Одним прыжком Знайка достиг беседки и заглянул внутрь.\n" +
      "Винтика и там не было.\n" +
      "Коротышки принялись тянуть веревку и притянули Знайку обратно к дому.\n" +
      "Знайка мгновенно вскарабкался по водосточной трубе на крышу и уже хотел оглядеться по сторонам, но налетевший неожиданно порыв ветра сдул его с крыши и понес в сторону.\n" +
      "Это не испугало Знайку, так как он знал, что коротышки в любой момент могут притянуть его на веревке обратно.\n";

    Main.main(new String[]{});
    assertEquals(expectedOutput, outContent.toString());
  }
}
