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


sum_simpson = 0
for i in range(1, n//2):
    x_i = a + (2*i) * h
    sum_simpson += f.subs(x, x_i)

sum_simpson_2 = 0

for i in range(1, n//2 + 1):
    x_i = a + (2*i - 1) * h
    sum_simpson_2 += f.subs(x, x_i)

integral_simpson = h / 3 * (f.subs(x, a) + 4*sum_simpson + 2*sum_simpson_2 + f.subs(x, b))


print(integral_midpoint)
print(integral_trapezoid)
print(integral_simpson)
