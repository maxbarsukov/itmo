package ru.itmo.computionalmath.lab1.commands;

import ru.itmo.computionalmath.lab1.algo.SimpleIteration;
import ru.itmo.computionalmath.lab1.models.Matrix;
import static ru.itmo.computionalmath.lab1.utils.Color.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleCommand implements Command {
  private final Scanner scanner = new Scanner(System.in);

  @Override
  public String getMessage() {
    return "Ввести матрицу с консоли";
  }

  @Override
  public void execute() {
    System.out.println(CYAN_BOLD + "Вводим матрицу с консоли" + RESET);
    Matrix matrix = createMatrixFromKeyBoard();

    System.out.println("Введите точность:");
    double eps;
    while (true) {
      try {
        String buffer = scanner.nextLine();
        eps = Double.parseDouble(buffer);
        break;
      }
      catch (Exception ignored){ }
    }
    SimpleIteration.compute(matrix, eps);
  }

  public Matrix createMatrixFromKeyBoard(){
    try {
      System.out.println("Введите размерность матрицы:");
      String buffer = scanner.nextLine();
      buffer = buffer.trim();

      int size = Integer.parseInt(buffer);
      if (size > 20 || size < 1) {
        throw new InputMismatchException();
      }

      System.out.println("Введите строки матрицы:");
      String [][] arr = new String[size][size+1];

      for (int i = 0; i < size; i++) {
        buffer = scanner.nextLine();
        arr[i] = buffer.trim().split(" ");
      }

      double [][] matrix = new double[size][size+1];
      for (int i = 0; i < size; i++){
        for (int j = 0; j < size+1; j++) {
          matrix[i][j] = Double.parseDouble(arr[i][j].trim());
        }
      }
      return new Matrix(matrix);

    } catch (InputMismatchException e) {
      System.out.println(RED + "Введена неверная размерность" + RESET);
    }
    return null;
  }
}
