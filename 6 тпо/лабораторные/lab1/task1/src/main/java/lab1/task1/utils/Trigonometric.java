package lab1.task1.utils;

public class Trigonometric {

  public static double arcsin(double x) {
    return arcsin(x, Integer.MAX_VALUE);
  }

  /**
   * Реализация разложения arcsin(x) в степенной ряд.
   *
   * @param x аргумент функции аргумент функции (должен быть в диапазоне [-1, 1])
   * @param n максимальное количество членов ряда для вычисления
   * @return значение arcsin(x)
   */
  public static double arcsin(double x, int n) {
    if (Math.abs(x) > 1) {
      return Double.NaN;
    }

    // Если аргумент равен нулю, то результатом
    // будет нуль с тем же знаком, что и у аргумента.
    if (x == -0d || Double.isNaN(x)) {
      return x;
    }

    // Когда x близок к 1 или -1, можно воспользоваться свойствами функции и сделать замену переменной
    // Это позволяет избежать проблем со сходимостью
    if (Math.abs(x) >= 0.999999) {
      double sqrtTerm = Math.sqrt(1 - x * x);
      double result = arcsin(sqrtTerm, n);
      return (x > 0) ? (Math.PI / 2) - result : -(Math.PI / 2) + result; // Учитываем знак
    }

    double res = x;
    double fact = x;

    for (int i = 0; i < n; i++) {
      double old = res;

      // Вычисляем новый член ряда на основе предыдущего
      fact *= (2 * i + 1) * (2 * i + 2);
      fact /= 4.0 * (i + 1) * (i + 1) * (2 * i + 3);
      fact *= x * x * (2 * i + 1);

      res += fact;

      // Проверка сходимости
      if (Math.abs(res - old) < Double.MIN_VALUE) break;
    }

    return res;
  }
}
