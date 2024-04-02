import numpy as np
from scipy.misc import derivative

from dto.result import Result
from methods.method import Method


dx = 0.00001

class NewtonMethod(Method):
    name = 'Метод Ньютона'

    def solve(self) -> Result:
        f = self.equation.function
        x0 = self.left

        epsilon = self.epsilon
        iteration = 0

        while True:
            iteration += 1

            df = derivative(f, x0, dx=dx)
            x1 = x0 - f(x0) / df
            if self.log:
                print(f'{iteration}: x_k = {x0:.3f}, f(x_k) = {f(x0):.3f}, '
                f'f\'(x_k) = {df:.3f}, x_k+1 = {x1:.3f}, |x_k+1 - x_k| = {abs(x1 - x0)}')

            if abs(x1 - x0) < epsilon and f(x1) < epsilon:
                break

            x0 = x1

        return Result(x1, f(x1), iteration, self.decimal_places)
