import math


def function(type_equation, x):
    try:
        if type_equation == 1:
            return math.pow(x, 2) - 3
        elif type_equation == 2:
            return 5 / x - 2 * x
        elif type_equation == 3:
            return math.exp(2 * x) - 2
        elif type_equation == 4:
            return 2 * math.pow(x, 3) - 3 * math.pow(x, 2) + 5 * x - 9
    except ZeroDivisionError:
        raise TypeError

def first_derivative(type_equation, x):
    try:
        if type_equation == 1:
            return 2 * x
        elif type_equation == 2:
            return -5 / math.pow(x, 2) - 2
        elif type_equation == 3:
            return 2 * math.exp(2 * x)
        elif type_equation == 4:
            return 6 * math.pow(x, 2) - 6 * x + 5
    except ZeroDivisionError:
        return first_derivative(x + 1e-8)

def second_derivative(type_equation, x):
    try:
        if type_equation == 1:
            return 2
        elif type_equation == 2:
            return 10 / math.pow(x, 3)
        elif type_equation == 3:
            return 4 * math.exp(2 * x)
        elif type_equation == 4:
            return 12 * x - 6
    except ZeroDivisionError:
        return second_derivative(x + 1e-8)

def fourth_derivative(type_equation, x):
    try:
        if type_equation == 1:
            return 0
        elif type_equation == 2:
            return 120 / math.pow(x, 5)
        elif type_equation == 3:
            return 16 * math.exp(2 * x)
        elif type_equation == 4:
            return 0
    except ZeroDivisionError:
        return fourth_derivative(x + 1e-8)
