from math import exp, sqrt
from itertools import pairwise


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


xx = list(sorted(map(float, "10.01 6.76 9.94 9.81 9.66 9.67 8.58 8.09 11.55 8.04 8.39 8.85 8.18 6.40".split(" "))))
yy = list(sorted(map(float, "9.35 9.27 8.36 6.13 8.27 8.79 7.84 7.87 5.93 7.38 9.65 8.30 9.59 9.36 9.45".split(" "))))
nx = len(xx)
ny = len(yy)

mx = get_math_expectation(xx)
my = get_math_expectation(yy)

dx = get_fixed_dispersion(xx)
dy = get_fixed_dispersion(yy)

print("X: ", xx, "; nx =", nx)
print("Y: ", yy, "; ny =", ny)

print("M(x) =", mx)
print("M(y) =", my)

print("fixed D(x) =", dx)
print("fixed D(y) =", dy)

print("F =", dx / dy)
