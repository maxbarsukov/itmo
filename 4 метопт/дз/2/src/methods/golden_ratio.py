"""
Метод золотого сечения
"""

from typing import Callable


GOLDEN_RATIO_1 = 0.382
GOLDEN_RATIO_2 = 0.618

def solve(
    f_derivatives: list[Callable[[float], float]],
    _a: float,
    _b: float,
    e: float,
) -> tuple[float, float]:
    f = f_derivatives[0]
    a = _a
    b = _b

    x1 = a + GOLDEN_RATIO_1 * (b - a)
    x2 = a + GOLDEN_RATIO_2 * (b - a)

    iteration = 1
    y1 = f(x1)
    y2 = f(x2)
    while (b - a > e):
        print(f"Шаг {iteration}:")
        print(f"Рассматриваем отрезок [a = {a}; b = {b}].")

        # print(f"x1 = {x1}; x2 = {x2}; y1 = {f(x1)}; y2 = {f(x2)}.")
        if y1 < y2:
            # print(f"y1 < y2 → b = {x2}; x2 = x1. Пересчет y2 не требуется.")
            b = x2
            x2 = x1
            y2 = y1
            x1 = a + GOLDEN_RATIO_1 * (b - a)
            y1 = f(x1)
        else:
            # print(f"y1 ≥ y2 → a = {x1}; x1 = x2. Пересчет y1 не требуется.")
            a = x1
            x1 = x2
            y1 = y2
            x2 = a + GOLDEN_RATIO_2 * (b - a)
            y2 = f(x2)

        print(f"a = {a}, b = {b}.\n")
        # print(f"b - a = {b - a}.\n")
        iteration += 1


    print(f"\nb - a < e. Минмум с заданной погрешностью ε = {e} лежит на середине данного отрезка.")

    x_m = (a + b) / 2
    y_m = f(x_m)

    print(f"Минимум в точке xm = {x_m}.")
    print(f"Значение в минимуме ym = {y_m}.\n")

    return x_m, y_m
