"""
Метод Ньютона
"""

from typing import Callable


def solve(
    f_derivatives: list[Callable[[float], float]],
    a: float,
    b: float,
    e: float,
) -> tuple[float, float]:
    f = f_derivatives[0]
    f_d1 = f_derivatives[1]
    f_d2 = f_derivatives[2]

    x = (a + b) / 2
    print('Шаг 0:')
    print(f'Начнем с середины заданного отрезка, т.е x0 = {x}.')

    iteration = 1
    while abs(f_d1(x)) > e:
        print(f'Шаг {iteration}:')
        x = x - (f_d1(x) / f_d2(x))

        print(f"Касательная к графику функции f'(x) в точке x{iteration} пересекает ось Oy в точке x{iteration+1} = {x}.")
        print(f"Выберем это новой точкой. В ней f'(x{iteration + 1}) = {f_d1(x)}.\n")
        iteration += 1

    print(f"\n|f'(x)| <= ε. Минимум с заданной погрешностью ε = {e} найден!")
    print(f"Минимум достигается в точке xm = {x}.")
    print(f"Значение в минимуме ym = {f(x)}.\n")

    return x, f(x)
