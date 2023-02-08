package ru.itmo.prog.lab5;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.stefanbirkner.systemlambda.SystemLambda;

import java.io.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;

public class MainTest {
  private final InputStream systemIn = System.in;
  private final PrintStream systemOut = System.out;

  private ByteArrayInputStream testIn;
  private ByteArrayOutputStream testOut;


  @Before
  public void setUpOutput() {
    testOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOut));
  }

  private void provideInput(String data) {
    testIn = new ByteArrayInputStream(data.getBytes());
    System.setIn(testIn);
  }

  private String getOutput() {
    return testOut.toString();
  }

  @After
  public void restoreSystemInputOutput() throws IOException {
    System.setIn(systemIn);
    System.setOut(systemOut);
    new PrintWriter("../data/test/test.json").print("[]");
  }

  @Test
  public void noArgument() throws Exception {
    int statusCode = SystemLambda.catchSystemExit(() -> {
      Main.main(new String[]{});
      assertEquals("Введите имя загружаемого файла как аргумент командной строки", getOutput());
    });
    assertEquals(1, statusCode);
  }

  @Test
  public void runScript() {
    provideInput("execute_script data/add.txt\n");

    Main.main(new String[]{"../data/test/test.json"});
    assertThat(getOutput().trim(), containsString("Коллекция успешна загружена!\n! Загруженные продукты валидны.\n$ Выполнение скрипта 'data/add.txt'..."));
    assertThat(getOutput().trim(), containsString("Организация \"NEW_ORG\" №1; Число сотрудников: 15; Вид: PRIVATE_LIMITED_COMPANY; Адрес: ул. Пушкина, 1234567\n"));
  }

  @Test
  public void runRecursion() {
    provideInput("execute_script data/recursion.txt\n");

    Main.main(new String[]{"../data/test/test.json"});
    assertThat(getOutput().trim(), containsString("ошибка: Скрипты не могут вызываться рекурсивно!\n"));
  }

  @Test
  public void runHelp() {
    provideInput("help\n");

    Main.main(new String[]{"../data/test/test.json"});
    assertThat(getOutput().trim(), containsString("execute_script <file_name>         исполнить скрипт из указанного файла"));
    assertThat(getOutput().trim(), containsString("filter_by_price <PRICE>            вывести элементы, значение поля price которых равно заданному"));
  }
}
