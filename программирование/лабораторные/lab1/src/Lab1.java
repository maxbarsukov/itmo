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

//First array:
//[16, 14, 12, 10, 8, 6, 4]
//
//
//Second array:
//1,8280 5,0962 6,8811 -1,1263 -9,0692 -5,1163 6,2290 -6,2965 -9,4314 0,8885 0,4901
//
//Third matrix:
//0,2184     0,2355     0,2092    25,3858     0,0004     1,2133 24019,5383 475133737235,6697 922337203685477,6000   485,1616     0,0221
//0,2184     0,2355     0,2092    25,3858     0,0004     1,2133 24019,5383 475133737235,6697 922337203685477,6000   485,1616     0,0221
//297,6985     3,3705     1,0973     0,0000     0,0000     0,0000     1,2898     0,0000     0,0000     2,7906     0,5185
//20,3362 922337203685477,6000 922337203685477,6000     0,0000     0,0000     0,0000 922337203685477,6000     0,0000     0,0000     2,4603     2,0239
//20,3362 922337203685477,6000 922337203685477,6000     0,0000     0,0000     0,0000 922337203685477,6000     0,0000     0,0000     2,4603     2,0239
//0,2184     0,2355     0,2092    25,3858     0,0004     1,2133 24019,5383 475133737235,6697 922337203685477,6000   485,1616     0,0221
//20,3362 922337203685477,6000 922337203685477,6000     0,0000     0,0000     0,0000 922337203685477,6000     0,0000     0,0000     2,4603     2,0239
