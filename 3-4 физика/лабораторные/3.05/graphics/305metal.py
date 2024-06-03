import matplotlib.pyplot as plt
import numpy as np


# Data provided by the user for the metal's resistance as a function of temperature
R = np.array([
    #1561.630219, 
    #1522.167488,
    1504.892368,
    1469.171484,
    1436.847104, 
    1407.685098,
    1381.481481,
    1354.986276,
    1330.316742,
    1303.84271,
    1279.151943
    ])
t = np.array([
    #76.85,
    #71.85,
    66.85, 
    61.85, 
    56.85, 
    51.85, 
    46.85, 
    41.85,
    36.85,
    31.85,
    26.85 
    ])

# Plot the data
plt.figure(figsize=(8, 6))
plt.plot(t, R, 'o', label='R; t')

# Fit a line to the data
coefficients = np.polyfit(t, R, 1)
fit_line = np.polyval(coefficients, t)

# Plot the fitted line
plt.plot(t, fit_line, label='Fit: R(t)')

# Labels and title
plt.xlabel('t, °C')
plt.ylabel('R, Ω')
plt.title('График зависимости R от t для металла')
plt.legend()
plt.grid(True)

# Display the plot
plt.savefig("305metal.pdf")
