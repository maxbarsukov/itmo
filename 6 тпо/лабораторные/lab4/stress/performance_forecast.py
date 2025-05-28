import pandas as pd
import matplotlib.pyplot as plt
from statsmodels.tsa.arima.model import ARIMA

def forecast_performance():
    # Load and prepare data
    df = pd.read_csv("./stress/result/results.csv")
    df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')
    success_df = df[df['success'] == True]

    # Resample to 1-minute intervals
    resampled = success_df.set_index('timestamp').resample('0.05T')['elapsed'].median().dropna()

    if len(resampled) > 20:
        # Fit ARIMA model
        model = ARIMA(resampled, order=(2,1,1))
        results = model.fit()

        # Generate forecast
        forecast = results.get_forecast(steps=10)

        # Plot results
        plt.figure(figsize=(12, 6))
        resampled.plot(label='Historical')
        forecast.predicted_mean.plot(label='Forecast', color='red')
        plt.fill_between(forecast.conf_int().index,
                        forecast.conf_int()['lower elapsed'],
                        forecast.conf_int()['upper elapsed'],
                        color='red', alpha=0.2)
        plt.title('Response Time Forecast')
        plt.ylabel('Elapsed Time (ms)')
        plt.legend()
        plt.grid()
        plt.tight_layout()
        plt.show()
    else:
        print("Insufficient data for forecasting")

if __name__ == "__main__":
    forecast_performance()
