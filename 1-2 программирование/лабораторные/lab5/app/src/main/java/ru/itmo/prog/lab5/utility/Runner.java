package ru.itmo.prog.lab5.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ru.itmo.prog.lab5.exceptions.ScriptRecursionException;
import ru.itmo.prog.lab5.managers.CommandManager;
import ru.itmo.prog.lab5.utility.console.Console;

public class Runner {
  public enum ExitCode {
    OK,
    ERROR,
    EXIT,
  }

  private final Console console;
  private final CommandManager commandManager;
  private final List<String> scriptStack = new ArrayList<>();

  public Runner(Console console, CommandManager commandManager) {
    this.console = console;
    this.commandManager = commandManager;
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

        commandManager.addToHistory(userCommand[0]);
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
   * Launchs the command.
   * @param userCommand Команда для запуска
   * @return Код завершения.
   */
  private ExitCode launchCommand(String[] userCommand) {
    if (userCommand[0].equals("")) return ExitCode.OK;
    var command = commandManager.getCommands().get(userCommand[0]);

    if (command == null) {
      console.printError("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
      return ExitCode.ERROR;
    }

    switch (userCommand[0]) {
      case "exit" -> {
        if (!commandManager.getCommands().get("exit").apply(userCommand)) return ExitCode.ERROR;
        else return ExitCode.EXIT;
      }
      case "execute_script" -> {
        if (!commandManager.getCommands().get("execute_script").apply(userCommand)) return ExitCode.ERROR;
        else return scriptMode(userCommand[1]);
      }
      default -> { if (!command.apply(userCommand)) return ExitCode.ERROR; }
    };

    return ExitCode.OK;
  }
}
