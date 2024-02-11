package ru.itmo.computionalmath.lab1.algo;

import ru.itmo.computionalmath.lab1.models.Matrix;
import ru.itmo.computionalmath.lab1.models.Result;
import ru.itmo.computionalmath.lab1.utils.Printer;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static ru.itmo.computionalmath.lab1.utils.Color.*;

public class SimpleIteration {
  private static final Printer printer = new Printer();
  private static double[][] data;

  public static void compute(Matrix matrix, double eps) {
    printer.printMatrix(matrix);

    if (checkDiagonal(matrix.getMatrix(), matrix.getSize())) {
      Result rs = methodOfSimpleIterations(matrix, eps);

      printer.println(rs.getTable());
      printer.printVector(CYAN_BOLD + "Решение системы: " + RESET, rs.getResult());
      printer.printVector(CYAN_BOLD + "Вектор невязки: " + RESET, rs.getResiduals());
      return;
    }

    permuteMatrix(matrix, 0);
    if (data != null) {
      Matrix matrix1 = new Matrix(data);
      printer.println(BLUE + "Матрица после перестановки строк" + RESET);
      printer.printMatrix(matrix1);

      Result rs = methodOfSimpleIterations(matrix1, eps);

      printer.println(rs.getTable());
      printer.printVector(CYAN_BOLD + "Решение системы: " + RESET, rs.getResult());
      printer.printVector(CYAN_BOLD + "Вектор невязки: " + RESET, rs.getResiduals());
    } else {
      printer.println(RED + "Отсутствие диагонального преобладания" + RESET);
    }
  }

  public static boolean checkDiagonal(double[][] matrix, int size) {
    int i, j, k = 1;
    double sum;

    for (i = 0; i < size; i++) {
      sum = 0;
      for (j = 0; j < size; j++) {
        sum += abs(matrix[i][j]);
      }

      sum -= abs(matrix[i][i]);
      if (sum >= abs(matrix[i][i])) {
        k = 0;
      }
    }
    return (k == 1);
  }

  private static void permuteMatrix(Matrix matrix, int index) {
    if (index >= matrix.getMatrix().length - 1) {
      if (checkDiagonal(matrix.getMatrix(), matrix.getSize())) {
        data = new double[matrix.getSize()][matrix.getSize() + 1];

        for (int i = 0; i < matrix.getSize(); i++) {
          for (int j = 0; j < matrix.getSize() + 1; j++) {
            data[i][j] = matrix.getMatrix()[i][j];
          }
        }
      }
    } else {
      for (int i = index; i < matrix.getMatrix().length; i++) {
        var t = matrix.getMatrix()[index];
        matrix.getMatrix()[index] = matrix.getMatrix()[i];
        matrix.getMatrix()[i] = t;

        permuteMatrix(matrix, index + 1);

        t = matrix.getMatrix()[index];
        matrix.getMatrix()[index] = matrix.getMatrix()[i];
        matrix.getMatrix()[i] = t;
      }
      printer.printMatrix(matrix);
    }
  }

  private static Result methodOfSimpleIterations(Matrix matrix, double eps) {
    var rs = new Result();
    var x = new double[matrix.getSize()];

    double norma, sum, t;
    do {
      ArrayList<Double> esps = new ArrayList<>();
      norma = 0;

      for (int i = 0; i < matrix.getSize(); i++) {
        t = x[i];
        sum = 0;

        for (int j = 0; j < matrix.getSize(); j++) {
          if (j != i)
            sum += matrix.getMatrix()[i][j] * x[j];
        }
        x[i] = (matrix.getVector()[i] - sum) / matrix.getMatrix()[i][i];
        esps.add(abs(x[i] - t));

        if (abs(x[i] - t) > norma) {
          norma = abs(x[i] - t);
        }
      }
      rs.addIter(x);
      rs.addE(esps);
    }
    while (norma > eps);


    rs.setResult(x);

    ArrayList<Double> residuals = new ArrayList<>();

    for (int i = 0; i < matrix.getSize(); i++) {
      double S = 0;
      for (int j = 0; j < matrix.getSize(); j++) {
        S += matrix.getMatrix()[i][j] * x[j];
      }
      residuals.add(S - matrix.getVector()[i]);
    }

    rs.setResiduals(residuals);
    return rs;
  }
}
