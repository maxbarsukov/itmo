package client.cli;

import client.App;
import client.commands.*;
import client.network.UDPClient;
import client.utility.console.Console;
import client.utility.Interrogator;
import common.exceptions.ScriptRecursionException;
import common.utility.Commands;

import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Класс, запускающий введенные пользователем команды.
 * @author maxbarsukov
 */
public class Runner {
  public enum ExitCode {
    OK,
    ERROR,
    EXIT,
  }

  private final Console console;
  private final UDPClient client;
  private final Map<String, Command> commands;

  private final Logger logger = App.logger;
  private final List<String> scriptStack = new ArrayList<>();

  public Runner(UDPClient client, Console console) {
    Interrogator.setUserScanner(new Scanner(System.in));
    this.client = client;
    this.console = console;
    this.commands = new HashMap<>() {{
      put(Commands.HELP, new Help(console, client));
      put(Commands.INFO, new Info(console, client));
      put(Commands.SHOW, new Show(console, client));
      put(Commands.ADD, new Add(console, client));
      put(Commands.UPDATE, new Update(console, client));
      put(Commands.REMOVE_BY_ID, new RemoveById(console, client));
      put(Commands.CLEAR, new Clear(console, client));
      put(Commands.HEAD, new Head(console, client));
      put(Commands.EXECUTE_SCRIPT, new ExecuteScript(console));
      put(Commands.EXIT, new Exit(console));
      put(Commands.ADD_IF_MAX, new AddIfMax(console, client));
      put(Commands.ADD_IF_MIN, new AddIfMin(console, client));
      put(Commands.SUM_OF_PRICE, new SumOfPrice(console, client));
      put(Commands.FILTER_BY_PRICE, new FilterByPrice(console, client));
      put(Commands.FILTER_CONTAINS_PART_NUMBER, new FilterContainsPartNumber(console, client));
    }};
  }

  /**
   * Интерактивный режим
   */
  public void interactiveMode() {
    var userScanner = Interrogator.getUserScanner();
    try {
      ExitCode commandStatus;
      String[] userCommand = {"", ""};

      do {
        console.ps1();
        userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
        userCommand[1] = userCommand[1].trim();
        commandStatus = launchCommand(userCommand);
      } while (commandStatus != ExitCode.EXIT);

    } catch (NoSuchElementException exception) {
      console.printError("Пользовательский ввод не обнаружен!");
    } catch (IllegalStateException exception) {
      console.printError("Непредвиденная ошибка!");
    }
  }

  /**
   * Режим для запуска скрипта.
   * @param argument Аргумент скрипта
   * @return Код завершения.
   */
  public ExitCode scriptMode(String argument) {
    String[] userCommand = {"", ""};
    ExitCode commandStatus;
    scriptStack.add(argument);
    if (!new File(argument).exists()) {
      argument = "../" + argument;
    }
    try (Scanner scriptScanner = new Scanner(new File(argument))) {
      if (!scriptScanner.hasNext()) throw new NoSuchElementException();
      Scanner tmpScanner = Interrogator.getUserScanner();
      Interrogator.setUserScanner(scriptScanner);
      Interrogator.setFileMode();

      do {
        userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
        userCommand[1] = userCommand[1].trim();
        while (scriptScanner.hasNextLine() && userCommand[0].isEmpty()) {
          userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
          userCommand[1] = userCommand[1].trim();
        }
        console.println(console.getPS1() + String.join(" ", userCommand));
        if (userCommand[0].equals("execute_script")) {
          for (String script : scriptStack) {
            if (userCommand[1].equals(script)) throw new ScriptRecursionException();
          }
        }
        commandStatus = launchCommand(userCommand);
      } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());

      Interrogator.setUserScanner(tmpScanner);
      Interrogator.setUserMode();

      if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
        console.println("Проверьте скрипт на корректность введенных данных!");
      }

      return commandStatus;

    } catch (FileNotFoundException exception) {
      console.printError("Файл со скриптом не найден!");
    } catch (NoSuchElementException exception) {
      console.printError("Файл со скриптом пуст!");
    } catch (ScriptRecursionException exception) {
      console.printError("Скрипты не могут вызываться рекурсивно!");
    } catch (IllegalStateException exception) {
      console.printError("Непредвиденная ошибка!");
      System.exit(0);
    } finally {
      scriptStack.remove(scriptStack.size() - 1);
    }
    return ExitCode.ERROR;
  }

  /**
   * Запускает команду.
   * @param userCommand Команда для запуска
   * @return Код завершения.
   */
  private ExitCode launchCommand(String[] userCommand) {
    if (userCommand[0].equals("")) return ExitCode.OK;
    var command = commands.get(userCommand[0]);

    if (command == null) {
      console.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
      return ExitCode.ERROR;
    }

    switch (userCommand[0]) {
      case "exit" -> {
        if (!commands.get("exit").apply(userCommand)) return ExitCode.ERROR;
        else return ExitCode.EXIT;
      }
      case "execute_script" -> {
        if (!commands.get("execute_script").apply(userCommand)) return ExitCode.ERROR;
        else return scriptMode(userCommand[1]);
      }
      default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
    };

    return ExitCode.OK;
  }
}
