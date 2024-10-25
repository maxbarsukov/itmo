import numpy as np
from scipy import stats
import matplotlib.pyplot as plt
from math import sqrt

x = np.loadtxt("data.txt")
y = np.loadtxt("data_generated.txt")

xm = np.mean(x)
ym = np.mean(y)

sxyi = 0
for i in range(len(x)):
  sxyi += (x[i] - xm) * (y[i] - ym)

sxi2 = 0
for i in range(len(x)):
  sxi2 += (x[i] - xm)**2

syi2 = 0
for i in range(len(x)):
  syi2 += (x[i] - xm)**2

r = sxyi / (sqrt(sxi2) * sqrt(syi2))
print(r)
