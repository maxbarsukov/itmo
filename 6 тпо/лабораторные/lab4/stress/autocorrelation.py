import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.ticker import MaxNLocator

# Load data and convert timestamps
df = pd.read_csv("./stress/result/results.csv")

df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

# Filter successful requests and sort
success_df = df[df['success'] == True].sort_values('timestamp')

# Handle possible outliers (adjust threshold based on your normal response times)
Q1 = success_df['elapsed'].quantile(0.25)
Q3 = success_df['elapsed'].quantile(0.75)
IQR = Q3 - Q1
filtered_df = success_df[(success_df['elapsed'] >= Q1 - 1.5*IQR) &
                         (success_df['elapsed'] <= Q3 + 1.5*IQR)]

# Resample using median to reduce outlier impact
resampled = filtered_df.set_index('timestamp').resample('1S')['elapsed'].median().dropna()

# Dynamic lag calculation (up to 20% of test duration)
test_duration = resampled.index[-1] - resampled.index[0]
max_lag = min(50, int(test_duration.total_seconds() * 0.2))  # Max 50 lags or 20% of duration

def calculate_autocorrelation(series, max_lag):
    return [series.autocorr(lag=lag) for lag in range(1, max_lag+1)]

if len(resampled) > max_lag*2:  # Ensure sufficient data points
    autocorr_values = calculate_autocorrelation(resampled, max_lag)

    # Plotting
    plt.figure(figsize=(12, 6))
    plt.stem(range(1, max_lag+1), autocorr_values,
             linefmt='blue', markerfmt='bo', basefmt=' ')

    # Confidence intervals (95% for white noise)
    conf_level = 1.96 / np.sqrt(len(resampled))
    plt.axhline(conf_level, color='red', linestyle='--', linewidth=1)
    plt.axhline(-conf_level, color='red', linestyle='--', linewidth=1)

    plt.title('Response Time Autocorrelation (Successful Requests)', fontsize=14)
    plt.xlabel('Time Lag (seconds)', fontsize=12)
    plt.ylabel('Autocorrelation Coefficient', fontsize=12)
    plt.gca().xaxis.set_major_locator(MaxNLocator(integer=True))  # Integer lags
    plt.grid(alpha=0.3)
    plt.ylim(-1.1, 1.1)
    plt.show()
else:
    print(f"Not enough data for autocorrelation analysis. After filtering: {len(resampled)} points")
