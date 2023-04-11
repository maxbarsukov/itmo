package client.utility.console;

import java.io.PrintStream;

/**
 * Для ввода команд и вывода результата
 * @author maxbarsukov
 */
public class StandardConsole implements Console {
  private static final String PS1 = "$ ";
  private static final String PS2 = "> ";

  private final PrintStream ps;

  public StandardConsole(PrintStream printStream) {
    this.ps = printStream;
  }

  /**
   * Выводит obj.toString() в консоль
   * @param obj Объект для печати
   */
  public void print(Object obj) {
    ps.print(obj);
  }

  /**
   * Выводит obj.toString() + \n в консоль
   * @param obj Объект для печати
   */
  public void println(Object obj) {
    ps.println(obj);
  }

  /**
   * Выводит ошибка: obj.toString() в консоль
   * @param obj Ошибка для печати
   */
  public void printError(Object obj) {
    ps.println("ошибка: " + obj);
  }

  /**
   * Выводит таблицу из 2 колонок
   * @param elementLeft Левый элемент колонки.
   * @param elementRight Правый элемент колонки.
   */
  public void printTable(Object elementLeft, Object elementRight) {
    ps.printf(" %-35s%-1s%n", elementLeft, elementRight);
  }

  /**
   * Выводит PS1 текущей консоли
   */
  public void ps1() {
    print(PS1);
  }

  /**
   * Выводит PS2 текущей консоли
   */
  public void ps2() {
    print(PS2);
  }

  /**
   * @return PS1 текущей консоли
   */
  public String getPS1() {
    return PS1;
  }

  /**
   * @return PS2 текущей консоли
   */
  public String getPS2() {
    return PS1;
  }
}
