package src;

import java.util.Random;
import java.util.Arrays;

public class Lab1 {
    private static final int ARR1_FROM = 16;
    private static final int ARR1_TO = 4;

    private static final int ARR2_SIZE = 11;
    private static final double RAND_FROM = -12.0d;
    private static final double RAND_TO = 11.0d;

    private static final int ARR3_COLUMNS_NUM = 7;
    private static final int ARR3_ROWS_NUM = 11;
    private static final int DECIMAL_PLACES = 4;

    public static void main(String[] args) {
        long[] p =  makeArray1(ARR1_FROM, ARR1_TO);
        System.out.println("First array:\n" + Arrays.toString(p) + "\n\n");

        double[] x = makeArray2(ARR2_SIZE, RAND_FROM, RAND_TO);
        System.out.println("Second array:");
        printArray(x, DECIMAL_PLACES);

        double[][] s = makeArray3(ARR3_COLUMNS_NUM, ARR3_ROWS_NUM, p, x);
        System.out.println("Third matrix:");
        printMatrix(s, DECIMAL_PLACES);
    }

    private static long[] makeArray1(int from, int to) {
        int size = (from - to) / 2 + 1;
        long[] result = new long[size];
        long temp = from;

        for (int i = 0; i < size; i++) {
            result[i] = temp;
            temp -= 2;
        }
        return result;
    }

    private static double[] makeArray2(int size, double from, double to) {
        double[] result = new double[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            result[i] = rand.nextDouble() * (to - from) + from;
        }
        return result;
    }

    private static double[][] makeArray3(int columnsSize, int rowsSize, long[] p, double[] x) {
        double[][] result = new double[columnsSize][rowsSize];

        for (int i = 0; i < columnsSize; i++) {
            for (int j = 0; j < rowsSize; j++) {
                result[i][j] = calculateValue(p[i], x[j]);
            }
        }
        return result;
    }

    private static void printArray(double[] array, int places) {
        for (int i = 0; i < array.length; i++) {
            System.out.format("%6.4f ", array[i]);
        }
        System.out.println("\n");
    }

    private static void printMatrix(double[][] matrix, int places) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.format("%10.4f ", round(matrix[row][col], places));
            }
            System.out.println();
        }
    }

    public static double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value *= factor;
        return (double) Math.round(value) / factor;
    }

    private static double calculateValue(long p, double x) {
        if (p == 12) {
            return Math.pow(
                    Math.pow((0.5 * Math.pow(2 * x, 2)), 3),
                    Math.sin(Math.pow(x, ((2.0 / 3.0) * (3.0 - x))))
            );
        }

        else if (p == 4 || p == 8 || p == 10) {
            return Math.log(Math.pow(Math.E, Math.pow(Math.E, Math.pow(x, x))));
        }

        return Math.pow(
                Math.pow(
                        Math.tan(Math.tan(x)) * (Math.pow(((1.0 / 4.0) / Math.tan(x)), 3) - 1.0),
                        3
                ),
                2
        );
    }
}
