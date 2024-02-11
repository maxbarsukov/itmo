package ru.itmo.computionalmath.lab1.utils;

import ru.itmo.computionalmath.lab1.models.Matrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Printer {
  private final MathContext context = new MathContext(3, RoundingMode.HALF_UP);

  public void println(Object message) {
    System.out.println(message);
  }

  public void print(Object message) {
    System.out.print(message);
  }

  public void printVector(Object message, ArrayList<Double> list){
    println(message);

    for(int i = 0; i < list.size(); i++){
        println("["+(i+1)+"] = " + String.format("%.15f",list.get(i)));
    }
  }

  public void printMatrix(Matrix matrix) {
    println("Матрица:");

    if (matrix == null) {
      return;
    }

    for (int i = 0; i < matrix.getSize(); i++) {
      for (int j = 0; j < matrix.getSize() + 1; j++) {
        print(new BigDecimal(matrix.getMatrix()[i][j], context));
        print(" ");
      }
      println("");
    }
  }
}
