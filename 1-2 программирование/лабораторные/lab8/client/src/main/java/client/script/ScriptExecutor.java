package client.script;

import client.commands.*;
import client.controllers.ConsoleController;
import client.network.UDPClient;
import client.utility.console.Console;
import client.utility.console.StandardConsole;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptExecutor {
  public enum ExitCode {
    OK,
    ERROR,
    EXIT,
  }

  private final UDPClient client;
  private final Console console;
  private ConsoleController consoleController;

//  private final Map<String, Command> commands;

  private final List<String> scriptStack = new ArrayList<>();


  public ScriptExecutor(UDPClient client, ConsoleController consoleController) {
    this.console = new StandardConsole(new PrintStream(consoleController.getConsole()));
    this.client = client;
    this.consoleController = consoleController;
  }

  public void run(String path) {
    consoleController.setPrinter(console);
    consoleController.setScriptPath(path);
    consoleController.setScriptExecutor(this);
    consoleController.show();
  }
}
