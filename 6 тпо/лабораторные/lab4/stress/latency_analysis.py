import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.dates as md
import numpy as np

# Load CSV data
df = pd.read_csv("./stress/result/results.csv")

# Convert timestamp to datetime
df['timeStamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

# Calculate concurrent users based on actual request overlap
def calculate_concurrent_requests(df):
    # Create numpy arrays for timestamps and durations
    start_times = df['timeStamp'].values.astype(np.int64)
    durations = df['elapsed'].values
    end_times = start_times + durations * 1_000_000  # Convert ms to ns

    # Initialize concurrent users array
    concurrent_requests = np.zeros(len(df), dtype=int)

    # Calculate overlaps using vectorized operations
    for i in range(len(df)):
        # Find all requests that overlap with this request
        mask = (start_times < end_times[i]) & (end_times > start_times[i])
        concurrent_requests[i] = np.sum(mask)

    return concurrent_requests

# Add concurrent users calculation to DataFrame
df['concurrent_requests'] = calculate_concurrent_requests(df)

# Extract relevant metrics with calculated concurrency
metrics = df[['timeStamp', 'concurrent_requests', 'elapsed', 'responseMessage']]

# Aggregate data by concurrent users
agg_data = metrics.groupby('concurrent_requests').agg(
    average_latency=('elapsed', 'mean'),
    failure_rate=('responseMessage', lambda x: (x != 'OK').mean()),
    request_count=('elapsed', 'count')
).reset_index()

# Filter out groups with too few data points (optional)
agg_data = agg_data[agg_data['request_count'] >= 3]  # Require at least 3 samples
# Modified failure rate analysis with binning and smoothing
plt.figure(figsize=(12, 8))

# Create user count bins (adjust bin size as needed)
bin_size = 5  # Number of users per bin
max_users = agg_data['concurrent_requests'].max()
bins = np.arange(0, max_users + bin_size, bin_size)

# Bin the data and calculate failure rates
binned_data = agg_data.groupby(pd.cut(agg_data['concurrent_requests'], bins=bins)).agg(
    average_latency=('average_latency', 'mean'),
    failure_rate=('failure_rate', 'mean'),
    bin_mid=('concurrent_requests', lambda x: (x.min() + x.max())/2),
    request_count=('request_count', 'sum')
).reset_index()

# Apply rolling average smoothing
window_size = 10 # Number of bins to average
binned_data = binned_data.sort_values('bin_mid')
binned_data['smoothed_failure'] = binned_data['failure_rate'].rolling(
    window=window_size, center=True, min_periods=1).mean()

# Filter bins with sufficient data (adjust threshold as needed)
binned_data = binned_data[binned_data['request_count'] >= 5]

# Failure rate subplot with raw and smoothed data
plt.subplot(2, 1, 2)
plt.bar(binned_data['bin_mid'], binned_data['failure_rate']*100,
        width=bin_size*0.8, alpha=0.3, color='r', label='Raw Failure Rate')
plt.plot(binned_data['bin_mid'], binned_data['smoothed_failure']*100,
         'r-', linewidth=2, label=f'{window_size}-bin Moving Average')
plt.xlabel('Calculated Concurrent Requests (Binned)')
plt.ylabel('Failure Rate (%)')
plt.title('Failure Rate Analysis')
plt.legend()
plt.grid(True)

# Keep latency subplot similar but use binned data
plt.subplot(2, 1, 1)
plt.plot(binned_data['bin_mid'], binned_data['average_latency'], 'b-o')
plt.title('Latency Analysis')
plt.ylabel('Average Latency (ms)')
plt.grid(True)

plt.tight_layout()
plt.show()
