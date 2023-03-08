package client.commands;

import client.utility.console.Console;

/**
 * Команда 'exit'. Завершает выполнение.
 * @author maxbarsukov
 */
public class Exit extends Command {
  private final Console console;

  public Exit(Console console) {
    super("exit");
    this.console = console;
  }

  /**
   * Выполняет команду
   * @return Успешность выполнения команды.
   */
  @Override
  public boolean apply(String[] arguments) {
    if (!arguments[1].isEmpty()) {
      console.println("Использование: '" + getName() + "'");
      return false;
    }

    console.println("Завершение выполнения...");
    console.println("""
                      \s
               ／＞　 フ\s
      　　　　| 　u　 u|\s
       　　　／`ミ _x 彡    | Пока - пока :3 |
      　 　 /　　　 　 |\s
      　　 /　 ヽ　　 ﾉ\s
      ／￣|　　 |　|　|\s
      | (￣ヽ＿_ヽ_)_)\s
      ＼二つ""".indent(1));
    return true;
  }
}
