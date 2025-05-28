import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.dates as md
import numpy as np

# Load CSV data
df = pd.read_csv("./stress/result/results.csv")

# Convert timestamp to datetime
df['timeStamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

# Calculate concurrent users based on actual request overlap
def calculate_concurrent_users(df):
    # Create numpy arrays for timestamps and durations
    start_times = df['timeStamp'].values.astype(np.int64)
    durations = df['elapsed'].values
    end_times = start_times + durations * 1_000_000  # Convert ms to ns

    # Initialize concurrent users array
    concurrent_users = np.zeros(len(df), dtype=int)

    # Calculate overlaps using vectorized operations
    for i in range(len(df)):
        # Find all requests that overlap with this request
        mask = (start_times < end_times[i]) & (end_times > start_times[i])
        concurrent_users[i] = np.sum(mask)

    return concurrent_users

# Add concurrent users calculation to DataFrame
df['concurrent_users'] = calculate_concurrent_users(df)

# Extract relevant metrics with calculated concurrency
metrics = df[['timeStamp', 'concurrent_users', 'elapsed', 'responseMessage']]

# Aggregate data by concurrent users
agg_data = metrics.groupby('concurrent_users').agg(
    average_latency=('elapsed', 'mean'),
    failure_rate=('responseMessage', lambda x: (x != 'OK').mean()),
    request_count=('elapsed', 'count')
).reset_index()

# Filter out groups with too few data points (optional)
agg_data = agg_data[agg_data['request_count'] >= 3]  # Require at least 3 samples

# Create visualization
plt.figure(figsize=(12, 8))

# Latency subplot
plt.subplot(2, 1, 1)
plt.plot(agg_data['concurrent_users'], agg_data['average_latency'], 'b-o')
plt.title('Server Performance Under Actual Load')
plt.ylabel('Average Latency (ms)')
plt.grid(True)

# Failure rate subplot
plt.subplot(2, 1, 2)
plt.plot(agg_data['concurrent_users'], agg_data['failure_rate']*100, 'r-o')
plt.xlabel('Calculated Concurrent Users')
plt.ylabel('Failure Rate (%)')
plt.grid(True)

plt.tight_layout()
plt.show()

# Time-based visualization with calculated concurrency
plt.figure(figsize=(12, 6))
plt.scatter(metrics['timeStamp'], metrics['elapsed'],
            c=metrics['concurrent_users'], cmap='viridis', alpha=0.7)
plt.colorbar(label='Concurrent Users')
plt.title('Response Time with Actual Concurrency')
plt.ylabel('Latency (ms)')
plt.xlabel('Time')
plt.gca().xaxis.set_major_formatter(md.DateFormatter('%H:%M:%S'))
plt.grid(True)
plt.show()

# Failure point analysis
print("\nConcurrency Analysis:")
if (agg_data['failure_rate'] > 0).any():
    failure_point = agg_data[agg_data['failure_rate'] > 0]['concurrent_users'].min()
    print(f"First failures observed at {failure_point} concurrent users")
else:
    print("No failures observed in the dataset")
    max_users = agg_data['concurrent_users'].max()
    avg_latency = agg_data['average_latency'].max()
    print(f"Maximum observed concurrency: {max_users} users")
    print(f"Maximum average latency: {avg_latency:.2f} ms")

# Additional concurrency distribution plot
plt.figure(figsize=(10, 6))
plt.hist(metrics['concurrent_users'], bins=20, edgecolor='black')
plt.title('Distribution of Concurrent Users')
plt.xlabel('Number of Concurrent Users')
plt.ylabel('Frequency')
plt.grid(True)
plt.show()
