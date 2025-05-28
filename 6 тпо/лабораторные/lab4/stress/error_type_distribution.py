import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

def plot_error_distribution():
    # Load and prepare data
    df = pd.read_csv("./stress/result/results.csv")
    df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

    # Filter errors and bin by minute
    error_df = df[df['success'] == False]
    crosstab = pd.crosstab(
        error_df['timestamp'].dt.floor('T'),
        error_df['responseCode'],
        normalize='index'
    )

    # Plotting
    plt.figure(figsize=(12, 6))
    sns.heatmap(crosstab.T, cmap='YlOrRd', cbar_kws={'label': 'Error Ratio'})
    plt.title('Error Type Distribution Over Time')
    plt.xlabel('Time (1-minute bins)')
    plt.ylabel('HTTP Status Code')
    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    plot_error_distribution()
