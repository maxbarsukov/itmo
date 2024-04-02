import numpy as np

from dto.result import Result
from methods.method import Method

class ChordMethod(Method):
    name = 'Метод хорд'

    def check(self):
        root_exists = self.equation.root_exists(self.left, self.right)
        return root_exists, 'Отсутствует корень на заданном промежутке или корней > 2' if not root_exists else ''

    def solve(self) -> Result:
        f = self.equation.function
        a = self.left
        b = self.right
        epsilon = self.epsilon
        iteration = 0

        x = a - (b - a) * f(a) / (f(b) - f(a))

        iteration = 0
        last_x = x

        while iteration < 10_000:
            iteration += 1

            if f(a) * f(x) < 0:
                b = x
            else:
                a = x

            x = a - (b - a) * f(a) / (f(b) - f(a))
            if self.log:
                print(f'{iteration}: a = {a:.3f}, b = {b:.3f}, x = {x:.3f}, '
                      f'f(a) = {f(a):.3f}, f(b) = {f(b):.3f}, f(x)={f(x)}, |x_k+1 - x_k| = {abs(x - last_x)}')

            if np.abs(f(x)) <= epsilon and abs(x - last_x) <= epsilon:
                break

            last_x = x

        return Result(x, f(x), iteration, self.decimal_places)
