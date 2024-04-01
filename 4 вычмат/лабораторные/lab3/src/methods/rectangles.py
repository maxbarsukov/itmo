import math
import numpy as np
from tabulate import tabulate

import functions


class Rectangles:
    type_equation = 0
    start = 0
    a = 0
    b = 0
    steps = 0
    accuracy = 0
    previous_count = 0
    n = 4
    h = 0

    result = [0, 0, 0]
    inaccuracy = [0, 0, 0]

    xy = []
    xy_avg = []
    table = []

    def __init__(self, a, b, accuracy, type_equation):
        self.a = a
        self.b = b
        self.start = a
        self.previous_count = 0
        self.result = [0, 0, 0]
        self.accuracy = math.pow(10, -1 * accuracy)
        self.type_equation = type_equation

    def calc(self):
        self.table = []
        self.xy = []
        self.xy_avg = []
        self.steps = 0
        self.n = self.check_n()
        y_left = 0
        y_right = 0
        y_mid = 0

        self.h = (self.b - self.a) / self.n

        while self.steps != self.n + 1:
            self.xy.append([self.a, functions.function(self.a)])

            if self.steps > 0:
                avg_x = (self.previous_count + self.a) / 2
                self.xy_avg.append([avg_x, functions.function(avg_x)])
                self.table.append([self.steps, self.xy[self.steps][0], self.xy[self.steps][1],
                                   self.xy_avg[self.steps - 1][0], self.xy_avg[self.steps - 1][1]])
            else:
                self.table.append([self.steps, self.xy[self.steps][0], self.xy[self.steps][1], "-", "-"])

            self.previous_count = self.a

            if 0 <= self.steps < self.n:
                y_left += self.xy[self.steps][1]
            if 0 < self.steps <= self.n:
                y_right += self.xy[self.steps][1]
                y_mid += self.xy_avg[self.steps - 1][1]

            self.a += self.h
            self.steps += 1

            if self.steps > 250000:
                self.result[0] = self.h * y_left
                self.result[1] = self.h * y_mid
                self.result[2] = self.h * y_right

                self.inaccuracy[0] = abs(self.max_value_fun_first() *
                                         math.pow(self.b - self.start, 2) / (2 * self.n))
                self.inaccuracy[1] = abs(self.max_value_fun_second() *
                                         math.pow(self.b - self.start, 3) / (24 * math.pow(self.n, 2)))
                self.inaccuracy[2] = abs(self.max_value_fun_second() *
                                         math.pow(self.b - self.start, 2) / (2 * self.n))

                print('\t', "Метод прямоугольников:")
                self.print_result()
                print("Число вычислений привысило 250000 шагов, интеграл вычислен от " + str(self.start)
                      + " до " + str(self.xy[-1][0]) + "!")
                raise ValueError

        self.result[0] = self.h * y_left
        self.result[1] = self.h * y_mid
        self.result[2] = self.h * y_right

        self.inaccuracy[0] = abs(self.max_value_fun_first() *
                                 math.pow(self.b - self.start, 2) / (2 * self.n))
        self.inaccuracy[1] = abs(self.max_value_fun_second() *
                                 math.pow(self.b - self.start, 3) / (24 * math.pow(self.n, 2)))
        self.inaccuracy[2] = abs(self.max_value_fun_first() *
                                 math.pow(self.b - self.start, 2) / (2 * self.n))

        self.print_table()
        self.print_result()

    def check_n(self):
        n = abs(math.pow(self.max_value_fun_second() * math.pow(self.b - self.a, 3) / 24 / self.accuracy, 0.5)) // 1

        if n % 2 == 1:
            n += 1
        else:
            n += 2

        if n <= 4:
            return 4
        else:
            return int(n)

    def max_value_fun_second(self):
        x = np.linspace(self.start, self.b, 100000)
        maximum = [abs(functions.second_derivative(i)) for i in x]
        return max(maximum)

    def max_value_fun_first(self):
        x = np.linspace(self.start, self.b, 100000)
        maximum = [abs(functions.first_derivative(i)) for i in x]
        return max(maximum)

    def print_table(self):
        print('\t', "Метод прямоугольников:")
        print(tabulate(self.table, headers=["№ шага", "x", "y", "x(i-0.5)", "y(i-0.5)"],
                       tablefmt="grid", floatfmt="2.5f"))

    def print_result(self, n=0):
        print("I(left):", self.result[0])
        print("I(mid):", self.result[1])
        print("I(right):", self.result[2])

        if n == 0:
            print("R(n) left: ", self.inaccuracy[0])
            print("R(n) mid: ", self.inaccuracy[1])
            print("R(n) right: ", self.inaccuracy[2])
        else:
            print("R(" + str(n) + ") left:", self.inaccuracy[0])
            print("R(" + str(n) + ") mid:", self.inaccuracy[1])
            print("R(" + str(n) + ") right:", self.inaccuracy[2])

        print("Число разбиений:", self.n)
