import math
import numpy as np
from tabulate import tabulate

import functions


class Simpson:
    type_equation = 0
    start = 0
    a = 0
    b = 0
    steps = 0
    accuracy = 0
    n = 4
    h = 0

    result = 0
    inaccuracy = 0

    xy = []
    table = []

    def __init__(self, a, b, accuracy, type_equation):
        self.a = a
        self.b = b
        self.start = a
        self.accuracy = math.pow(10, -1 * accuracy)
        self.type_equation = type_equation

    def calc(self):
        self.table = []
        self.xy = []
        self.steps = 0
        self.n = self.check_n()
        y_even = 0
        y_odd = 0

        self.h = (self.b - self.a) / self.n

        while self.steps != self.n + 1:
            self.xy.append([self.a, functions.function(self.a)])
            self.table.append([self.steps, self.xy[self.steps][0], self.xy[self.steps][1]])

            if self.steps % 2 == 1 and 0 < self.steps < self.n:
                y_odd += self.xy[self.steps][1]
            elif self.steps % 2 == 0 and 1 < self.steps < self.n - 1:
                y_even += self.xy[self.steps][1]

            self.a += self.h
            self.steps += 1

            if self.steps > 250000:
                self.result = self.h / 3 * (self.xy[0][1] + self.xy[-1][1] + 4 * y_odd + 2 * y_even)
                self.inaccuracy = abs(self.max_value_fun() *
                                      math.pow(self.b - self.start, 5) / 180 / math.pow(self.n, 4))
                print('\t', "Метод Симпсона:")
                self.print_result()
                print("Число вычислений привысило 250000 шагов, интеграл вычислен от " + str(self.start)
                      + " до " + str(self.xy[-1][0]) + "!")
                raise ValueError

        self.result = self.h / 3 * (self.xy[0][1] + self.xy[-1][1] + 4 * y_odd + 2 * y_even)
        self.inaccuracy = abs(self.max_value_fun() *
                              (math.pow(self.b - self.start, 5) / (180 * math.pow(self.n, 4))))

        self.print_table()
        self.print_result()

    def check_n(self):
        n = abs(math.pow(self.max_value_fun() * math.pow(self.b - self.a, 5) / 180 / self.accuracy, 0.25)) // 1
        if n % 2 == 1:
            n += 1
        else:
            n += 2
        if n <= 4:
            return 4
        else:
            return int(n)

    def max_value_fun(self):
        x = np.linspace(self.start, self.b, 100000)
        maximum = [abs(functions.fourth_derivative(i)) for i in x]
        return max(maximum)

    def print_table(self):
        print('\t', "Метод Симпсона:")
        print(tabulate(self.table, headers=["№ шага", "x", "y"], tablefmt="grid", floatfmt="2.5f"))

    def print_result(self, n=0):
        print("I:", self.result)
        if n == 0:
            print("R(n): ", self.inaccuracy)
        else:
            print("R(" + str(n) + "):", self.inaccuracy)
        print("Число разбиений:", self.n)
