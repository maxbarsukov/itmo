import pandas as pd
import numpy as np
from scipy import stats
import json
from datetime import datetime

class NumpyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.integer):
            return int(obj)
        elif isinstance(obj, np.floating):
            return float(obj)
        elif isinstance(obj, np.ndarray):
            return obj.tolist()
        elif isinstance(obj, datetime):
            return obj.isoformat()
        elif isinstance(obj, pd.Timestamp):
            return obj.isoformat()
        return super(NumpyEncoder, self).default(obj)

def analyze_stress_test(csv_path):
    df = pd.read_csv(csv_path)
    df['timestamp'] = pd.to_datetime(df['timeStamp'], unit='ms')

    report = {
        "load_characteristics": {
            "duration_sec": float((df['timestamp'].max() - df['timestamp'].min()).total_seconds()),
            "max_threads": int(df['allThreads'].max()),
            "first_failure_threads": int(df[df['success'] == False]['allThreads'].min()),
            "requests_per_sec": float(len(df) / ((df['timestamp'].max() - df['timestamp'].min()).total_seconds()))
        },

        "response_times": {
            "mean": float(df['elapsed'].mean()),
            "median": float(df['elapsed'].median()),
            "p90": float(df['elapsed'].quantile(0.9)),
            "p95": float(df['elapsed'].quantile(0.95)),
            "p99": float(df['elapsed'].quantile(0.99)),
            "max": float(df['elapsed'].max()),
            "min": float(df['elapsed'].min()),
            "std_dev": float(df['elapsed'].std())
        },

        "error_analysis": {
            "total_errors": int(len(df[df['success'] == False])),
            "error_rate": float(len(df[df['success'] == False]) / len(df)),
            "error_codes_distribution": dict(df[df['success'] == False]['responseCode'].value_counts()),
            "error_clustering": analyze_error_clusters(df)
        },

        "network_metrics": {
            "avg_latency": float(df['Latency'].mean()),
            "avg_connect_time": float(df['Connect'].mean()),
            "bytes_sent_avg": float(df['sentBytes'].mean()),
            "bytes_received_avg": float(df['bytes'].mean())
        },

        "advanced_metrics": {
            "throughput_mb_sec": float((df['bytes'].sum() / 1024 / 1024) / ((df['timestamp'].max() - df['timestamp'].min()).total_seconds())),
            "response_time_slope": float(calculate_degradation_slope(df)),
            "anomaly_count": int(detect_anomalies(df).get('anomaly_count', 0)),
            "domino_effect_ratio": float(calculate_domino_effect(df))
        },

        "thread_stats": {
            "threads_with_errors": int(df[df['success'] == False]['threadName'].nunique()),
            "max_errors_per_thread": int(df[df['success'] == False]['threadName'].value_counts().max()),
            "thread_utilization": float(df.groupby('threadName')['elapsed'].sum().mean() / ((df['timestamp'].max() - df['timestamp'].min()).total_seconds()))
        }
    }

    with open('stress_test_report.json', 'w') as f:
        json.dump(report, f, indent=4, cls=NumpyEncoder)

    return report

def analyze_error_clusters(df):
    error_times = df[df['success'] == False]['timestamp'].astype(np.int64)
    if len(error_times) > 1:
        intervals = error_times.diff().dropna()
        return {
            "avg_interval_sec": float(intervals.mean() / 1e9),
            "min_interval_sec": float(intervals.min() / 1e9),
            "cluster_threshold_sec": 5.0,
            "is_clustered": bool(intervals.min() / 1e9 < 5.0)
        }
    return {}

def calculate_degradation_slope(df):
    time_series = df.set_index('timestamp')['elapsed'].resample('30S').mean()
    if len(time_series) > 2:
        x = np.arange(len(time_series))
        slope, _, _, _, _ = stats.linregress(x, time_series.values)
        return float(slope)
    return 0.0

def detect_anomalies(df):
    features = df[['elapsed', 'Latency', 'allThreads']].dropna()
    if len(features) > 10:
        from sklearn.ensemble import IsolationForest
        clf = IsolationForest(contamination=0.05)
        anomalies = clf.fit_predict(features)
        return {
            "anomaly_count": int(sum(anomalies == -1)),
            "anomaly_ratio": float(sum(anomalies == -1) / len(anomalies))
        }
    return {}

def calculate_domino_effect(df):
    error_intervals = df[df['success'] == False]['timestamp'].diff().dt.total_seconds().dropna()
    if len(error_intervals) > 1:
        rolling_mean = error_intervals.rolling(window=min(5, len(error_intervals))).mean()
        return float((error_intervals.iloc[-1] - error_intervals.iloc[0]) / error_intervals.iloc[0])
    return 0.0

if __name__ == "__main__":
    report = analyze_stress_test("./stress/result/results.csv")
    print("Анализ завершен. Отчет сохранен в stress_test_report.json")
