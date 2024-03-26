"""
Метод половинного деления
"""

from typing import Callable


def solve(
    f_derivatives: list[Callable[[float], float]],
    _a: float,
    _b: float,
    e: float,
) -> tuple[float, float]:
    f = f_derivatives[0]
    a = _a
    b = _b

    iteration = 1
    while (b - a > 2 * e):
        print(f"Шаг {iteration}:")
        print(f"Рассматриваем отрезок [{a}; {b}].")

        x1 = (a + b - e) / 2
        x2 = (a + b + e) / 2
        y1 = f(x1)
        y2 = f(x2)

        # print(f"x1 = {x1}, x2 = {x2}; y1 = {y1}, y2 = {y2}")

        if y1 > y2:
            # print(f"y1 > y2 → Отсекаем начало отрезка: [a; x1], от {a} до {x1}.")
            a = x1
        else:
            # print(f"y1 ≤ y2 → Отсекаем конец отрезка: [x2; b], от {x2} до {b}.")
            b = x2

        print(f"a = {a}, b = {b}.\n")
        # print(f"b - a = {b - a}.\n")
        iteration += 1

    print(f"\nb - a < 2ε. Минимум с заданной погрешностью ε = {e} лежит на середине этого отрезка.")

    x_m = (a + b) / 2
    y_m = f(x_m)
    print(f"Минимум в точке xm = {x_m}.")
    print(f"Значение в минимуме ym = {y_m}.\n")

    return x_m, y_m
