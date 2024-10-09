import numpy as np
from scipy import stats
import matplotlib.pyplot as plt
from math import ceil, sqrt


# Чтение данных из файла
def read_data(filename):
    return np.loadtxt(filename)

# Функции для расчета характеристик
def calculate_statistics(data):
    mean = np.mean(data)
    variance = np.var(data)
    std_dev = np.std(data)
    coef_variation = std_dev / mean
    return mean, variance, std_dev, coef_variation

# Доверительные интервалы
def confidence_intervals(data, confidence):
    n = len(data)
    mean = np.mean(data)
    std_error = stats.sem(data)
    h = std_error * stats.t.ppf((1 + confidence) / 2, n - 1)
    return mean - h, mean + h

# Расчет относительного отклонения
def relative_deviation(value, best_value):
    return abs((value - best_value) / best_value) * 100

# Построение графика значений
def plot_data(data):
    plt.plot(data)
    plt.title('График числовой последовательности')
    plt.xlabel('Индекс')
    plt.ylabel('Значение')
    plt.show()

def plot_histogram(data):
    # Определяем количество интервалов (bins)
    bins = ceil(sqrt(300))  # Округляем вверх sqrt(300), чтобы получить 18 интервалов

    # Вычисляем количество значений в каждом интервале и границы интервалов
    counts, bin_edges = np.histogram(data, bins=bins)

    # Построение гистограммы
    plt.figure(figsize=(12, 6))
    plt.hist(data, bins=bin_edges, color='blue', edgecolor='black', alpha=0.7)

    # Формируем подписи для каждого интервала в формате "min - max"
    bin_labels = [f"{bin_edges[i]:.2f} - {bin_edges[i+1]:.2f}" for i in range(len(bin_edges) - 1)]

    for i in range(len(bin_edges) - 1):
      print(f"{bin_edges[i]:.2f}", f"{bin_edges[i+1]:.2f}", )

    # Настраиваем метки по оси X
    plt.xticks((bin_edges[:-1] + bin_edges[1:]) / 2, bin_labels, rotation=45, ha='right')

    # Добавляем подписи и сетку
    plt.title('Гистограмма распределения частот', fontsize=15)
    plt.xlabel('Интервалы значений', fontsize=12)
    plt.ylabel('Частота', fontsize=12)
    plt.grid(True)

    plt.tight_layout()  # Для корректного отображения подписей
    plt.show()

# Автокорреляционный анализ
def autocorrelation(data, lag):
    n = len(data)
    mean = np.mean(data)
    c0 = np.sum((data - mean) ** 2) / n
    acf = [np.sum((data[:n-k] - mean) * (data[k:] - mean)) / (n - k) / c0 for k in range(1, lag + 1)]
    return np.array(acf)

# Построение графика автокорреляции
def plot_autocorrelation(data, lag=10):
    acf = autocorrelation(data, lag)
    print(acf)
    plt.plot(range(1, lag+1), acf)
    plt.title('Автокорреляционный анализ')
    plt.xlabel('Сдвиг')
    plt.ylabel('Коэффициент автокорреляции')
    plt.show()

def generate_erlang(alpha, k, size=1000):
    # Генерация случайных величин по закону Эрланга
    return np.random.gamma(k, 1/alpha, size)

def generate_exponential(lmbda, size=1000):
    # Генерация экспоненциально распределенных случайных величин
    return np.random.exponential(1/lmbda, size)

def main():
    # Чтение данных
    data = read_data('data2.txt')

    # Исследование для выборок разных размеров
    sample_sizes = [10, 20, 50, 100, 200, 300]
    best_sample = data[:300]

    best_mean, best_variance, best_std, best_coef_var = calculate_statistics(best_sample)

    for size in sample_sizes:
        sample = data[:size]
        mean, variance, std_dev, coef_var = calculate_statistics(sample)

        print(f"\nSample size: {size}")
        print(f"Mean: {mean:.3f}, Variance: {variance:.3f}, Std Dev: {std_dev:.3f}, Coef of Variation: {coef_var:.3f}")
        print(f"Relative deviation (mean): {relative_deviation(mean, best_mean):.2f}%")

        # Доверительные интервалы
        for confidence in [0.90, 0.95, 0.99]:
            ci = confidence_intervals(sample, confidence)
            print(f"{int(confidence*100)}% Confidence Interval: {ci[0]:.3f} - {ci[1]:.3f}")

    # Построение графиков
    plot_data(data[:300])
    plot_histogram(data[:300])
    plot_autocorrelation(data[:300])

main()
