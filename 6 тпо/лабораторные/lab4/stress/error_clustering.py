import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.cluster import DBSCAN
from matplotlib.dates import DateFormatter

def cluster_errors():
    # Load and prepare data
    df = pd.read_csv("./stress/result/results.csv")
    df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')
    error_df = df[df['success'] == False]

    # Convert timestamps to nanoseconds
    timestamps = error_df['timestamp'].values.astype(np.int64).reshape(-1, 1)

    # DBSCAN clustering (5-second epsilon)
    clustering = DBSCAN(eps=5e9, min_samples=3).fit(timestamps)

    # Plot results
    plt.figure(figsize=(12, 4))
    plt.scatter(error_df['timestamp'], [1]*len(error_df),
                c=clustering.labels_, cmap='tab20', s=50)
    plt.gca().xaxis.set_major_formatter(DateFormatter("%H:%M:%S"))
    plt.title('Error Event Clusters')
    plt.yticks([])
    plt.grid(alpha=0.3)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    cluster_errors()
