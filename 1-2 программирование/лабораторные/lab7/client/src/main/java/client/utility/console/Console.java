package client.utility.console;

/**
 * Консоль для ввода команд и вывода результата
 * @author maxbarsukov
 */
public interface Console {
  void print(Object obj);
  void println(Object obj);
  void printError(Object obj);
  void printTable(Object obj1, Object obj2);
  void ps1();
  void ps2();
  String getPS1();
  String getPS2();
}
