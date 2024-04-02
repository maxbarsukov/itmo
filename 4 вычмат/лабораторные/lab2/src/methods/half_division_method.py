from dto.result import Result
from methods.method import Method


class HalfDivisionMethod(Method):
    name = 'Метод половинного деления'

    def check(self):
        root_exists = self.equation.root_exists(self.left, self.right)
        return root_exists, 'Отсутствует корень на заданном промежутке или корней > 2' if not root_exists else ''

    def solve(self) -> Result:
        f = self.equation.function
        a = self.left
        b = self.right
        epsilon = self.epsilon

        iteration = 0
        while True:
            iteration += 1
            fa = f(a)
            fb = f(b)
            x = (a + b) / 2
            fx = f(x)
            if self.log:
                print(f'{iteration}: a = {a:f}, b = {b:.3f}, x = {x:.3f}, '
                      f'f(a) = {fa:.3f}, f(b) = {fb:.3f}, f(x)={fx}, |a-b| = {abs(a - b)}')

            if abs(a - b) <= epsilon and abs(fx) <= epsilon:
                break

            if fa * fx < 0:
                b = x
            else:
                a = x

        return Result(x, fx, iteration, self.decimal_places)
