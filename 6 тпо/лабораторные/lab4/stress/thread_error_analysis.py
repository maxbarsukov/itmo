import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

def analyze_thread_errors():
    # Load data
    df = pd.read_csv("./stress/result/results.csv")

    # Calculate error rates per thread
    thread_stats = df.groupby('threadName').agg(
        total_requests=('success', 'count'),
        errors=('success', lambda x: sum(~x))
    )
    thread_stats['error_rate'] = thread_stats['errors'] / thread_stats['total_requests']

    # Plot distribution
    plt.figure(figsize=(12, 6))
    sns.boxplot(x=thread_stats['error_rate'], color='skyblue', width=0.3)
    sns.stripplot(x=thread_stats['error_rate'], color='darkred', size=5, alpha=0.7)
    plt.title('Error Rate Distribution Across Threads')
    plt.xlabel('Error Rate')
    plt.grid(axis='x', alpha=0.3)
    plt.tight_layout()
    plt.show()

if __name__ == "__main__":
    analyze_thread_errors()
