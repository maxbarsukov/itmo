package ru.itmo.computionalmath.lab1.commands;

import ru.itmo.computionalmath.lab1.algo.SimpleIteration;
import ru.itmo.computionalmath.lab1.models.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static ru.itmo.computionalmath.lab1.utils.Color.*;

public class FileCommand implements Command {
  private final Scanner scanner = new Scanner(System.in);

  private double eps;

  @Override
  public String getMessage() {
    return "Ввести матрицу с файла";
  }

  @Override
  public void execute() {
    System.out.println(CYAN_BOLD + "Вводим матрицу с файла" + RESET);
    System.out.println("Имя файла:");
    String path = scanner.nextLine();
    Matrix matrix = readMatrixFromFile(path);
    SimpleIteration.compute(matrix, eps);
  }

  public Matrix readMatrixFromFile(String fileName) {
    try(var file = new BufferedReader(new FileReader(fileName))) {
      String[] input_const = file.readLine().trim().split(" ");

      int size = Integer.parseInt(input_const[0].trim());
      eps = Double.parseDouble(input_const[1].trim());

      double [][] matrix = new double[size][size + 1];
      for (int i = 0; i < size; i++) {
        String[] row = file.readLine().trim().split(" ");
        if (row.length > size + 1) throw new ArrayIndexOutOfBoundsException();

        for (int j = 0; j < size + 1; j++) {
          matrix[i][j] = Double.parseDouble(row[j].trim());
        }
      }
      return new Matrix(matrix);

    } catch (IOException e) {
      System.out.println(RED + "Ошибка ввода" + RESET);
      System.out.println(e.getMessage());
    }
    return null;
  }
}
