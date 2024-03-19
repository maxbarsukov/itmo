import numpy as np

from dto.result import Result
from methods.method import Method

class ChordMethod(Method):
    name = 'Метод хорд'

    def check(self):
        root_exists = self.equation.root_exists(self.left, self.right)
        return root_exists, 'Отсутствует корень на заданном промежутке' if not root_exists else ''

    def solve(self) -> Result:
        f = self.equation.function
        a = self.left
        b = self.right
        epsilon = self.epsilon
        iteration = 0

        x = a - (b - a) * f(a) / (f(b) - f(a))

        iteration = 0
        last_x = x

        while True:
            if np.abs(f(x)) < epsilon:
                break

            iteration += 1

            if f(a) * f(x) < 0:
                b = x
            else:
                a = x

            x = a - (b - a) * f(a) / (f(b) - f(a))
            if self.log:
                print(f'{iteration}: a = {a:.3f}, b = {b:.3f}, x = {x:.3f}, '
                      f'f(a) = {f(a):.3f}, f(b) = {f(b):.3f}, f(x)={f(x):.3f}, |x_k+1 - x_k| = {abs(x - last_x):.3f}')
            last_x = x

        return Result(x, f(x), iteration, self.decimal_places)
