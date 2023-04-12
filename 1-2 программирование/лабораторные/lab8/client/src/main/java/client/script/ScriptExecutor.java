package client.script;

import client.controllers.MainController;
import client.ui.DialogManager;
import client.utility.Localizator;
import common.exceptions.ScriptRecursionException;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScriptExecutor {
  public enum ExitCode {
    OK,
    ERROR
  }

  private final List<String> scriptStack = new ArrayList<>();
  private Localizator localizator;
  private MainController mainController;

  public ScriptExecutor(MainController mainController, Localizator localizator) {
    this.localizator = localizator;
    this.mainController = mainController;
  }

  public ExitCode run(String path) {
    String userCommand;
    ExitCode commandStatus;
    scriptStack.add(path);
    try (Scanner scriptScanner = new Scanner(new File(path))) {
      if (!scriptScanner.hasNext()) throw new NoSuchElementException();

      do {
        userCommand = scriptScanner.nextLine().trim();
        while (scriptScanner.hasNextLine() && userCommand.isEmpty()) {
          userCommand = scriptScanner.nextLine().trim();
        }

        if (userCommand.startsWith("execute_script")) {
          for (String scriptPath : scriptStack) {
            if (new File(path).getAbsolutePath().equals(scriptPath)) {
              throw new ScriptRecursionException();
            }
          }
        }
        commandStatus = launchCommand((userCommand + " ").split(" ", 2));
      } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());


      if (commandStatus == ExitCode.ERROR && !(userCommand.startsWith("execute_script") && !userCommand.split(" ", 2)[1].trim().isEmpty())) {
        DialogManager.alert("CheckScriptErr", localizator);
      }

      return commandStatus;

    } catch (FileNotFoundException exception) {
      DialogManager.alert("FileNotFoundException", localizator);
    } catch (NoSuchElementException exception) {
      DialogManager.alert("EmptyFileErr", localizator);
    } catch (ScriptRecursionException exception) {
      DialogManager.alert("ScriptRecursionException", localizator);
    } catch (IllegalStateException exception) {
      DialogManager.alert("UnexpectedErr", localizator);
      System.exit(0);
    } finally {
      scriptStack.remove(scriptStack.size() - 1);
    }

    return ExitCode.ERROR;
  }

  private ExitCode launchCommand(String[] userCommand) {
    userCommand[0] = userCommand[0].trim();
    userCommand[1] = userCommand[1].trim();

    if (userCommand[0].equals("")) return ExitCode.OK;

    var noSuchCommand = false;
    switch (userCommand[0]) {
      case "help" -> mainController.help();
      case "info" -> mainController.info();
      case "add" -> mainController.add();
      case "update" -> mainController.update();
      case "remove_by_id" -> mainController.removeById();
      case "clear" -> mainController.clear();
      case "execute_script" -> mainController.executeScript();
      case "head" -> mainController.head();
      case "add_if_max" -> mainController.addIfMax();
      case "add_if_min" -> mainController.addIfMin();
      case "sum_of_price" -> mainController.sumOfPrice();
      case "filter_by_price" -> mainController.filterByPrice();
      case "filter_contains_part_number" -> mainController.filterContainsPartNumber();
      case "exit" -> mainController.exit();
      default -> {
        noSuchCommand = true;
        var formatted = MessageFormat.format(localizator.getKeyString("CommandNotFound"), userCommand[0]);
        DialogManager.createAlert(localizator.getKeyString("Error"), formatted, Alert.AlertType.ERROR, true);
      }
    };

    if (noSuchCommand) return ExitCode.ERROR;
    return ExitCode.OK;
  }
}
