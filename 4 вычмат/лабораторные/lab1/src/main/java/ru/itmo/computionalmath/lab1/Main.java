package ru.itmo.computionalmath.lab1;

import java.util.ArrayList;
import java.util.Scanner;
import ru.itmo.computionalmath.lab1.commands.*;
import static ru.itmo.computionalmath.lab1.utils.Color.*;

public class Main {
  private static boolean isNumeric(String str) {
    try {
      Double.parseDouble(str);
    } catch(NumberFormatException e) {
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    System.out.println(CYAN_BOLD + "Вычислительная математика. Барсуков Максим, P3215\n" + RESET);

    Scanner scanner = new Scanner(System.in);
    var commands = new ArrayList<Command>() {{
      add(new ConsoleCommand());
      add(new FileCommand());
      add(new RandomCommand());
    }};

    System.out.println(BLUE_BOLD + "Введите:" + RESET);

    for (int i = 0; i < commands.size(); i++) {
      System.out.println(WHITE + "\t" + (i + 1) + " - " + commands.get(i).getMessage() + RESET);
    }

    String result = scanner.nextLine();
    if (isNumeric(result) && Integer.parseInt(result) > 0 && Integer.parseInt(result) <= 3) {
      int input = Integer.parseInt(result);
      commands.get(input - 1).execute();
    } else {
      System.out.println(RED + "Такого варианта нет" + RESET);
    }
  }
}
