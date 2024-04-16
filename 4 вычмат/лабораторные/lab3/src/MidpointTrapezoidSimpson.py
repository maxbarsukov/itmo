from sympy import *

x = symbols('x')

f = -3*x**3 - 5*x**2 + 4*x - 2

a, b = -3, -1
n = 10
h = (b-a) / n


sum_midpoint = 0
for i in range(n):
    x_i = a + (i + 0.5) * h
    sum_midpoint += f.subs(x, x_i)

integral_midpoint = h * sum_midpoint


sum_trapezoid = 0
for i in range(1, n):
    x_i = a + i * h
    sum_trapezoid += f.subs(x, x_i)

integral_trapezoid = h / 2 * (f.subs(x, a) + 2*sum_trapezoid + f.subs(x, b))


sum_simpson = f.subs(x, a) + f.subs(x, b)

for i in range(1, n):
    coef = 3 + (-1)**(i + 1)
    sum_simpson += coef * f.subs(x, a + i * h)

integral_simpson = h / 3 * sum_simpson


print(integral_midpoint)
print(integral_trapezoid)
print(integral_simpson)
