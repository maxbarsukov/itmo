from math import exp, sqrt
from itertools import pairwise
import matplotlib as plt
import numpy as np

def get_math_expectation(lst):
    ni = sorted(list(set(lst)))
    pi = [lst.count(x) / len(lst) for x in ni]
    return sum([ni[i] * pi[i] for i in range(len(ni))])

def get_dispersion(lst):
    return get_math_expectation([x*x for x in lst]) - get_math_expectation(lst)**2

def get_fixed_dispersion(lst):
    return (len(lst) * get_dispersion(lst)) / (len(lst) - 1)

def get_ecdf(lst):
    xs = sorted(lst)
    return [sum([1 if (xi >= x) else 0 for x in xs]) / len(xs) for xi in xs]



# Новые исходные данные
ns = [-4.10, -3.42, -4.53, -4.42, -3.96, -4.03, -4.08, -4.18, -4.04,
      -2.87, -3.93, -4.25, -3.86, -3.91, -4.17, -4.50, -3.65, -4.50]

mx = get_math_expectation(ns)
dx = get_fixed_dispersion(ns)

b = mx
a = sqrt(2 / dx)

print("M(x) =", mx)
print("unfixed D(x) =", get_dispersion(ns))
print("fixed D(x) =", dx)
print("s =", sqrt(dx))
print("alpha =", a)
print("beta =", b)

# Функции плотности вероятности и распределения для распределения Лапласа
def f_laplace(x, a, b):
    return (a / 2) * exp(-a * abs(x - b))

def F_laplace(x, a, b):
    if x <= b:
        return 0.5 * exp(a * (x - b))
    else:
        return 1 - 0.5 * exp(-a * (x - b))


# Генерация значений для графиков
x_values = np.linspace(min(ns) - 1, max(ns) + 1, 400)
f_values = [f_laplace(x, a, b) for x in x_values]
F_values = [F_laplace(x, a, b) for x in x_values]


# Построение графиков

# График функции плотности вероятности
plt.figure(figsize=(14, 5))
plt.plot(x_values, f_values, label="f(x)", color="blue")
plt.title("Функция плотности вероятности f(x)")
plt.xlabel("x")
plt.ylabel("f(x)")
plt.grid(True)
plt.legend()
plt.show()

# График теоретической функции распределения
plt.figure(figsize=(14, 5))
plt.plot(x_values, F_values, label="Теоретическая F(x)", color="red")
plt.title("Теоретическая функция распределения F(x)")
plt.xlabel("x")
plt.ylabel("F(x)")
plt.grid(True)
plt.legend()
plt.show()


# График эмпирической функции распределения
plt.figure(figsize=(14, 5))
plt.ylim(-0.05, 1.2)
plt.xlim(-5.1, -2.4)

## plt.xticks(sorted(ns), rotation ='vertical') # <- отметки на X

xx = [min(ns) - 0.5] + sorted(ns) + [max(ns) + 0.5]
Fx = [0] + get_ecdf(ns) + [1]

pairs_xx = list(pairwise(xx))
for i in range(len(pairs_xx)):
    if (pairs_xx[i][0] != pairs_xx[i][1]):
        plt.scatter(x=pairs_xx[i][1], y=Fx[i], c="green", s=10)
    line = plt.hlines(y=Fx[i], xmin=pairs_xx[i][0], xmax=pairs_xx[i][1], color="green")

line.set_label("Эмпирическая F(x)")
plt.title("Эмпирическая функция распределения F(x)")
plt.xlabel("x")
plt.ylabel("F(x)")
plt.grid(True)
plt.legend()
plt.show()

