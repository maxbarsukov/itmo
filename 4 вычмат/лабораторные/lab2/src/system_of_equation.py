from math import tan
import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import fsolve

def a(xy):
    x, y = xy
    return [x**2 + y**2 - 1, x**2 - y - 0.5]

def b(xy):
    x, y = xy
    return [x**2 + y**2 - 1, x - y**2]

def plot_system(system):
    x = np.linspace(-2, 2, 400)
    y = np.linspace(-2, 2, 400)
    X, Y = np.meshgrid(x, y)

    Z1 = np.array([system([x_, y_])[0] for x_, y_ in zip(np.ravel(X), np.ravel(Y))]).reshape(X.shape)
    Z2 = np.array([system([x_, y_])[1] for x_, y_ in zip(np.ravel(X), np.ravel(Y))]).reshape(X.shape)

    plt.contour(X, Y, Z1, levels=[0], colors='r')
    plt.contour(X, Y, Z2, levels=[0], colors='b')
    plt.xlabel('x')
    plt.ylabel('y')
    plt.show()

def solve(system, x0, y0, epsilon, max_iter=1_000):
    def jacobian(xy):
        x, y = xy
        return np.array([[2*x, 2*y], [2*x, -1]])

    xy = np.array([x0, y0], dtype=float)
    for i in range(max_iter):
        J_inv = np.linalg.inv(jacobian(xy))
        F = np.array(system(xy))
        xy_next = xy - np.dot(J_inv, F)
        error = np.linalg.norm(xy_next - xy)

        if error < epsilon:
            return xy_next, i + 1, error

        xy = xy_next

    raise Exception("Solution not found in {} iterations".format(max_iter))


def choose_system_of_equations(functions):
    print("Выберите систему уравнений:")
    for key, value in functions.items():
        print(str(key) + ": " + value[1])

    try:
        equations_number = int(input("Введите номер системы: "))
    except ValueError:
        print('(!) Вы ввели не число')
        return choose_system_of_equations(functions)
    if equations_number < 1 or equations_number > len(functions):
        print("(!) Такого номера нет.")
        return choose_system_of_equations(functions)
    return functions[equations_number][0]

def run():
    systems = {1: [a, "x^2 + y^2 - 1, x^2 - y - 0.5"], 2: [b, "x^2 + y^2 - 1, x - y^2"]}
    system = choose_system_of_equations(systems)

    x0, y0 = map(float, input("Введите начальные приближения x0, y0: ").split())
    epsilon = float(input('Введите погрешность вычисления: '))

    xy_solution, iterations, error = solve(system, x0, y0, epsilon)

    print(f"\nВектор неизвестных: x1 = {xy_solution[0]:.5f}, x2 = {xy_solution[1]:.5f}")
    print(f"Количество итераций: {iterations}")
    print(f"Вектор погрешностей: {error:.5e}")

    residuals = np.abs(np.array(system(xy_solution)))
    print("Проверка решения системы уравнений:")
    print(f"Невязки: {residuals[0]:.5e}, {residuals[1]:.5e}\n")

    plot_system(system)
