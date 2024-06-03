import matplotlib.pyplot as plt
import numpy as np

# Data provided by the user
ln_R = np.array([
    #6.61, 
    #6.44, 
    6.23,
    6.03,
    5.84,
    5.66,
    5.45,
    5.23,
    5.06,
    4.57
    ])
inv_T = np.array([
    #3.70, 
    #3.63,
    3.57,
    3.50, 
    3.44,
    3.38,
    3.33, 
    3.27,
    3.22,
    3.17
    ])

# Plot the data
plt.figure(figsize=(8, 6))
plt.plot(inv_T, ln_R, 'o', label='ln(R); 1/T')

# Fit a line to the data
coefficients = np.polyfit(inv_T, ln_R, 1)
fit_line = np.polyval(coefficients, inv_T)

# Plot the fitted line
plt.plot(inv_T, fit_line)

# Labels and title
plt.xlabel('$T^{-1} \\times 10^3$, $1/K$')
plt.ylabel('$\ln(R)$')
plt.title('График зависимости ln(R) от 1/T для полупроводника')
plt.legend()
plt.grid(True)

# Display the plot
plt.savefig("305semiconducor.pdf")
