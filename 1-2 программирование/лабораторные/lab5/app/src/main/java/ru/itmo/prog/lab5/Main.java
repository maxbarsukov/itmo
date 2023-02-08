package ru.itmo.prog.lab5;

import ru.itmo.prog.lab5.commands.*;

import ru.itmo.prog.lab5.managers.CollectionManager;
import ru.itmo.prog.lab5.managers.CommandManager;
import ru.itmo.prog.lab5.managers.DumpManager;

import ru.itmo.prog.lab5.models.Organization;
import ru.itmo.prog.lab5.models.Product;
import ru.itmo.prog.lab5.utility.*;
import ru.itmo.prog.lab5.utility.console.StandardConsole;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Interrogator.setUserScanner(new Scanner(System.in));
    var console = new StandardConsole();

    if (args.length == 0) {
      console.println("Введите имя загружаемого файла как аргумент командной строки");
      System.exit(1);
    }

    var dumpManager = new DumpManager(args[0], console);
    var collectionManager = new CollectionManager(dumpManager);

    Organization.updateNextId(collectionManager);
    Product.updateNextId(collectionManager);
    collectionManager.validateAll(console);

    var commandManager = new CommandManager() {{
      register("help", new Help(console, this));
      register("info", new Info(console, collectionManager));
      register("show", new Show(console, collectionManager));
      register("add", new Add(console, collectionManager));
      register("update", new Update(console, collectionManager));
      register("remove_by_id", new RemoveById(console, collectionManager));
      register("clear", new Clear(console, collectionManager));
      register("save", new Save(console, collectionManager));
      register("execute_script", new ExecuteScript(console));
      register("exit", new Exit(console));
      register("head", new Head(console, collectionManager));
      register("add_if_max", new AddIfMax(console, collectionManager));
      register("add_if_min", new AddIfMin(console, collectionManager));
      register("sum_of_price", new SumOfPrice(console, collectionManager));
      register("filter_by_price", new FilterByPrice(console, collectionManager));
      register("filter_contains_part_number", new FilterContainsPartNumber(console, collectionManager));
    }};

    new Runner(console, commandManager).interactiveMode();
  }
}
