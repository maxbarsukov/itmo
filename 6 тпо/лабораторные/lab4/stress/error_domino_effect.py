import pandas as pd
import matplotlib.pyplot as plt

def analyze_error_cascade():
    # Load and prepare data
    df = pd.read_csv("./stress/result/results.csv")
    df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')
    error_df = df[df['success'] == False].sort_values('timestamp')

    # Calculate error intervals
    error_intervals = error_df['timestamp'].diff().dt.total_seconds()

    if len(error_intervals) > 10:
        # Calculate rolling mean
        window_size = min(15, len(error_intervals)//2)
        rolling_mean = error_intervals.rolling(window=window_size).mean()

        # Plot results
        plt.figure(figsize=(12, 6))
        plt.plot(error_intervals, alpha=0.5, label='Interval Between Errors')
        plt.plot(rolling_mean, color='red', label=f'{window_size}-Error Moving Average')
        plt.title('Error Cascade Analysis')
        plt.ylabel('Seconds Between Errors')
        plt.xlabel('Error Sequence Number')
        plt.legend()
        plt.grid()
        plt.tight_layout()
        plt.show()
    else:
        print("Not enough errors for cascade analysis")

if __name__ == "__main__":
    analyze_error_cascade()
