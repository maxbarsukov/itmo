package ru.itmo.computionalmath.lab1.models;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {
  private final double [][] data;
  private final int size;

  public Matrix(double [][] data) {
    this.data = data;
    this.size = data.length;
  }

  public double[][] getMatrix() {
    return data;
  }

  public int getSize() {
    return size;
  }

  public double[] getVector(){
    double [] vector = new double[size];
    for(int i = 0; i < this.size; i++){
      vector[i] = data[i][size];
    }
    return vector;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Matrix matrix1 = (Matrix) o;
    return size == matrix1.size && Arrays.deepEquals(data, matrix1.data);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(size);
    result = 31 * result + Arrays.deepHashCode(data);
    return result;
  }

  @Override
  public String toString() {
    return "Matrix{" +
            "data=" + Arrays.toString(data) +
            ", size=" + size +
            '}';
  }
}
