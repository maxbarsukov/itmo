import java.util.*;

public class ProbabilityTheory {
  private final double[] values;

  private final List<Double> xi = new ArrayList<>();
  private final List<Integer> ni = new ArrayList<>();
  private final List<Double> pi = new ArrayList<>();

  public ProbabilityTheory(double[] values) {
    this.values = values;
  }

  public void getVarValues() {
    Arrays.sort(this.values);
    System.out.println("! Вариационный ряд:\n" + Arrays.toString(values) + "\n\n");
  }

  public void getExtremeValues() {
    System.out.println("! Экстремальные значения:\nMIN = " + values[0] + "\nMAX = " + values[values.length - 1] + "\n\n");
  }

  public void getSelectionSize() {
    System.out.println("! Размах выборки:\n" + (values[values.length - 1] - values[0]) + "\n\n");
  }

  public void discrepancyCalculation() {
    Arrays.stream(values).distinct().forEach(x -> {
      var count = 0;
      xi.add(x);
      for (double element : values) {
        if (element == x) count++;
      }

      ni.add(count);
      pi.add(((double) count / (double) values.length));
    });

    double expectedValue = 0;
    for (int i = 0; i < xi.size(); i++) {
      expectedValue += xi.get(i) * pi.get(i);
    }

    System.out.printf("! Оценка математического ожидания\n%.2f\n\n", expectedValue);

    double disperancy = 0;
    for (int i = 0; i < xi.size(); i++) {
      disperancy += Math.pow((xi.get(i) - expectedValue), 2) * ni.get(i);
    }

    disperancy /= values.length;
    double fixedDisperancy = xi.size() * disperancy / (xi.size() - 1);
    System.out.printf("! Дисперсия\n%.2f\n\n", disperancy);
    System.out.printf("! Исправленная дисперсия\n%.2f\n\n", fixedDisperancy);
    System.out.printf("! Cреднеквадратическоe отклонение\n%.2f\n\n", Math.sqrt(disperancy));
    System.out.printf("! Исправленное СКО\n%.2f\n\n", Math.sqrt(fixedDisperancy));
  }

  public double getH() {
    return (values[values.length - 1] - values[0]) / (1 + ((Math.log(values.length) / Math.log(2))));
  }

  public int getM() {
    return (int) Math.ceil(1 + (Math.log(values.length) / Math.log(2)));
  }

  public void calculateEmpiricFunction() {
    System.out.println("\t\t\t! Функция");
    System.out.printf("\t\t\tx\t<=\t%.2f\t->\t%.2f\n", xi.get(0), 0.0);

    double h = pi.get(0);
    var chart = new Chart("x", "f(X)", "Эмпирическая функция");
    chart.addChart("x <= " + xi.get(0), xi.get(0) - 0.5, xi.get(0), 0);

    for (int i = 0; i < xi.size() - 1; i++) {
      System.out.printf("%.2f\t<\tx\t<=\t%.2f\t->\t%.2f\n", xi.get(i), xi.get(i + 1), h);
      chart.addChart(xi.get(i) + " < x <= " + xi.get(i + 1), xi.get(i), xi.get(i + 1), h);
      h += pi.get(i + 1);
    }

    System.out.printf("%.2f\t<\tx\t\t\t\t->\t%.2f\n", xi.get(xi.size() - 1), h);
    chart.addChart(xi.get(xi.size() - 1) + " < x", xi.get(xi.size() - 1), xi.get(xi.size() - 1) + 1, h);
    chart.plot("EmpiricFunction");
  }

  public void drawFrequencyPolygon() {
    var frequencyPolygon = new Chart("x", "p_i", "Полигон частот");

    double xStart = values[0] - getH() / 2;
    for (int i = 0; i < getM(); i++) {
      int count = 0;
      for (double value : values) {
        if (value >= xStart && value < (xStart + getH())) {
          count++;
        }
      }

      frequencyPolygon.polygonalChart(xStart + getH() / 2, (double) count / values.length);
      System.out.println("[ " + xStart + " : " + (xStart + getH()) + " ) -> " + (double) count / values.length);

      xStart += getH();
    }

    frequencyPolygon.plotPolygon("FrequencyPolygon");
  }

  public void drawHistogram(int size) {
    var chart = new Chart("x", "p_i / h", "Гистограмма частот");
    double xStart = values[0] - getH() / 2;

    for (int i = 0; i < getM(); i++) {
      int s = 0;
      for (double value : values) {
        if (value >= xStart && value < (xStart + getH())) {
          s++;
        }
      }

      chart.addHistogram(
        xStart + " : " + xStart + getH(), xStart,
        xStart + getH(),
        ((double) s / size) / getH()
      );

      xStart += getH();
    }
    chart.plot("Histogram");
  }

  public void printData() {
    System.out.println("\n\n! DEBUG:\n");
    for (int i = 0; i < xi.size(); i++) {
      System.out.println(xi.get(i) + " " + ni.get(i) + " " + pi.get(i));
    }
  }
}
