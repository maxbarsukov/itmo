from sympy import *

x = symbols('x')

f = -3*x**3 - 5*x**2 + 4*x - 2

a, b = -3, -1
n = 5

h = (b - a) / n

fa = f.subs(x, a)
f1 = f.subs(x, a + h)
f2 = f.subs(x, a + 2*h)
f3 = f.subs(x, a + 3*h)
f4 = f.subs(x, a + 4*h)
fb = f.subs(x, b)

integral = (b-a)/n * ((7/90)*fa + (32/90)*f1 + (12/90)*f2 + (32/90)*f3 + (7/90)*fb)

integral.evalf()
print(integral)
