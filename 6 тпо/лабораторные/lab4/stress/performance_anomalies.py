import pandas as pd
import matplotlib.pyplot as plt
from sklearn.ensemble import IsolationForest

def detect_anomalies():
    # Load and prepare data
    df = pd.read_csv("./stress/result/results.csv")
    df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

    # Feature selection
    features = df[['elapsed', 'Latency', 'allThreads']].dropna()

    # Train Isolation Forest
    clf = IsolationForest(contamination=0.05, random_state=42)
    df = df.loc[features.index]
    df['anomaly'] = clf.fit_predict(features)

    # Plot results
    plt.figure(figsize=(12, 6))
    plt.scatter(df['timestamp'], df['elapsed'],
                c=df['anomaly'].map({-1: 'red', 1: 'green'}),
                alpha=0.6, s=10)
    plt.title('Performance Anomaly Detection')
    plt.ylabel('Response Time (ms)')
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    detect_anomalies()
