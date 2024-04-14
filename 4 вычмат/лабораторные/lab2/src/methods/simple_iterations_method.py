import numpy
from scipy.misc import derivative

from dto.equation import Equation
from dto.result import Result
from methods.method import Method


dx = 0.00001
steps = 100
MAX_ITERS = 50_000

class SimpleIterationsMethod(Method):
    name = 'Метод простой итерации'

    def __init__(self, equation: Equation, left: float, right: float,
                 epsilon: float, decimal_places: int, log: bool):
        super().__init__(equation, left, right, epsilon, decimal_places, log)
        f = self.equation.function

    def check(self):
        if not self.equation.root_exists(self.left, self.right):
            return False, 'Отсутствует корень на заданном промежутке или корней > 2'

        return True, ''

    def solve(self) -> Result:
        f = self.equation.function
        x = 1

        max_derivative = max(abs(derivative(f, self.left, dx)), abs(derivative(f, self.right, dx)))
        lbd = 1 / max_derivative

        if derivative(f, x, dx) > 0:
            lbd = -lbd

        phi = lambda x: x + 1/23.005 * f(x)

        print('phi\'(a) = ', abs(derivative(phi, self.left, dx)))
        print('phi\'(b) = ', abs(derivative(phi, self.right, dx)))
        for x in numpy.linspace(self.left, self.right, steps, endpoint=True):
            if abs(derivative(phi, x, dx)) >= 1:
                print(f'Не выполнено условие сходимости метода |phi\'(x)| < 1 на интервале при x = {x}')
                break

        iteration = 0
        while True:
            iteration += 1

            if iteration == MAX_ITERS:
              if (input(f'Достигнуто {iteration} итераций и ответа вы не получите! Хотите продолжить? [y/n]\n') != 'y'):
                break

            x_prev = x
            x = phi(x)

            if self.log:
                print(f'{iteration}: xk = {x_prev:.4f}, f(xk) = {f(x_prev)}, '
                      f'xk+1 = 𝜑(𝑥𝑘) = {x:.4f}, |xk - xk+1| = {abs(x - x_prev):}')

            if abs(x - x_prev) <= self.epsilon and abs(f(x)) <= self.epsilon:
                break

        return Result(x, f(x), iteration, self.decimal_places)
