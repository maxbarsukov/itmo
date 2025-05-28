
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

df = pd.read_csv("./stress/result/results.csv")
df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

quantiles = df.groupby(pd.Grouper(key='timestamp', freq='1T'))['elapsed'].quantile([0.5, 0.75, 0.90, 0.95, 0.99]).unstack()

quantiles.plot(figsize=(12,6))
plt.title('Response Time Percentiles Over Time')
plt.show()
