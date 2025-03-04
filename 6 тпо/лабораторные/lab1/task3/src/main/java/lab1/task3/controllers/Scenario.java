package lab1.task3.controllers;

import lab1.task3.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
  private final List<Command> commands;

  public Scenario() {
    this.commands = new ArrayList<>();
  }

  public void addCommand(Command command) {
    commands.add(command);
  }

  public void execute() {
    for (Command command : commands) {
      System.out.print("* ");
      command.execute();
    }
  }
}
