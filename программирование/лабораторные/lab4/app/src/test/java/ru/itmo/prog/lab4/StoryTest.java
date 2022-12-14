package ru.itmo.prog.lab4;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class StoryTest {
  @Test
  public void appHasAGreeting() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    String expectedOutput  = """
      Незнайка, который в это время выглянул в коридор, услыхал слова Шпунтика.
      Все всполошились и бросились к выходу.
      Приказ моментально исполнили.
      Знайка обвязал один конец веревки вокруг пояса, а другой конец привязал к дверной ручке и строго сказал:
      Придав своему телу наклонное положение, Знайка с силой оттолкнулся ногами от порога и полетел в направлении мастерской, которая находилась неподалеку от дома.
      Он немного не рассчитал толчка и поднялся выше, чем было надо.
      Пролетая над мастерской, он ухватился рукой за флюгер, который показывал направление ветра.
      Это задержало полет.
      Спустившись по водосточной трубе, Знайка отворил дверь и проник в мастерскую.
      Коротышки с напряжением следили за его действиями.
      Через минуту Знайка выглянул из мастерской.
      Одним прыжком Знайка достиг беседки и заглянул внутрь.
      Винтика и там не было.
      Коротышки принялись тянуть веревку и притянули Знайку обратно к дому.
      Знайка мгновенно вскарабкался по водосточной трубе на крышу и уже хотел оглядеться по сторонам, но налетевший неожиданно порыв ветра сдул его с крыши и понес в сторону.
      Это не испугало Знайку, так как он знал, что коротышки в любой момент могут притянуть его на веревке обратно.
      Ему, однако, не удалось ничего разглядеть, так как в следующий момент произошло то, чего никто не ожидал.
      Не долетев до забора, Знайка вдруг начал стремительно падать, словно какая-то сила неожиданно потянула его вниз.
      Шлепнувшись с размаху о землю, он растянулся во весь рост и не успел даже сообразить, что произошло.
      Ощущая во всем теле страшную тяжесть, он с трудом поднялся на ноги и огляделся по сторонам.
      Его удивило, что он снова твердо держится на ногах.
      Он попробовал поднять руку, потом другую, попробовал сделать шаг, другой...
      Руки и ноги повиновались с трудом, словно были свинцом налиты.
      """;

    Main.main(new String[]{});
    assertEquals(expectedOutput, outContent.toString());
  }
}
