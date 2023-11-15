import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    var classloader = Thread.currentThread().getContextClassLoader();
    var inputStream = classloader.getResourceAsStream("input.txt");
    assert inputStream != null;

    var fileReader = new InputStreamReader(inputStream);
    var scanner = new Scanner(fileReader);
    scanner.useLocale(Locale.ENGLISH); // Use dots, not commas

    final var n = 20;
    final var elements = new double[n];

    for (int i = 0; i < n; i++) {
      elements[i] = scanner.nextDouble();
    }
    scanner.close();

    var runner = new ProbabilityTheory(elements);

    // вариационный ряд
    runner.getVarValues();

    // экстремальные значения
    runner.getExtremeValues();

    // размах
    runner.getSelectionSize();

    // оценки математического ожидания и среднеквадратического отклонения
    runner.discrepancyCalculation();

    // эмпирическую функцию распределения и её график
    runner.calculateEmpiricFunction();

    // гистограмму и полигон приведенных частот группированной выборки
    runner.drawFrequencyPolygon();
    runner.drawHistogram(elements.length);
    runner.printData();
  }
}
