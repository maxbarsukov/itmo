from math import log, sin, cos


def f(x: float) -> float:
    """Функция f."""
    return log(1 + x**2) - sin(x)


def f_derivative_1(x: float) -> float:
    """Первая производная функции f."""
    return (2*x / (x**2 + 1)) - cos(x)


def f_derivative_2(x: float) -> float:
    """Вторая производная функции f."""
    return sin(x) - ((2*(x**2 - 1)) / ((1 + x**2)**2))


F_DERIVATIES = [
    f,
    f_derivative_1,
    f_derivative_2,
]
