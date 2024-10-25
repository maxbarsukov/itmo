import numpy as np
import pandas as pd
import scipy.stats as stats
import matplotlib.pyplot as plt


data = np.loadtxt("data.txt")
generated_data = np.loadtxt("data_generated.txt")

pd.set_option('display.max_rows', 500)
pd.set_option('display.max_columns', 500)
pd.set_option('display.width', 400)

# Определяем выборки для анализа
sample_sizes = [10, 20, 50, 100, 200, 300]
results = []

for n in sample_sizes:
    sample = data[:n]

    # Расчет статистических моментов
    mean = np.mean(sample)
    variance = np.var(sample, ddof=1)
    std_dev = np.std(sample, ddof=1)
    coeff_var = std_dev / mean  # Коэффициент вариации в процентах

    # Доверительные интервалы
    alpha_values = [0.1, 0.05, 0.01]
    confidence_intervals = []
    margin_of_errors = []

    for alpha in alpha_values:
        z_score = stats.norm.ppf(1 - alpha / 2)
        margin_of_error = z_score * (std_dev / np.sqrt(n))
        margin_of_errors.append(round(margin_of_error, 4))
        confidence_intervals.append((mean - margin_of_error, mean + margin_of_error))

    # Относительные отклонения
    best_mean = np.mean(data)  # Наилучшее значение по всей выборке
    relative_deviation = (mean - best_mean) / best_mean * 100

    results.append({
        'n': n,
        'mean': mean,
        'variance': round(variance, 4),
        'std_dev': round(std_dev, 4),
        'coeff_var': coeff_var,
        'margin_of_errors': margin_of_errors,
        'relative_deviation': relative_deviation,
        'confidence_intervals': confidence_intervals
    })

# Создание DataFrame для удобного представления результатов
results_df = pd.DataFrame(results)

for r in results:
  print(r['confidence_intervals'])
print('\n')

for r in results:
  print(r['margin_of_errors'])
print('\n')

# Вывод результатов в таблицу
print(results_df)
print('\n')

# График значений числовой последовательности
plt.figure(figsize=(12, 6))
plt.plot(data, marker='o', linestyle='-', color='b')
plt.title('Числовая последовательность')
plt.xlabel('Индекс')
plt.ylabel('Значение')
plt.grid()
plt.show()

# Автокорреляционный анализ
def autocorrelation(x):
    n = len(x)
    result = np.correlate(x - np.mean(x), x - np.mean(x), mode='full')
    return result[result.size // 2:] / (np.var(x, ddof=1) * n)

autocorr_values = autocorrelation(data)

print(autocorr_values[1:11])

plt.figure(figsize=(12, 6))
plt.stem(autocorr_values[1:11], use_line_collection=True)
plt.title('Автокорреляция')
plt.xlabel('Лаг')
plt.ylabel('Коэффициент автокорреляции')
plt.grid()
plt.show()

# Гистограмма распределения частот
plt.figure(figsize=(12, 6))
plt.hist(data, bins=30, density=True, alpha=0.6, color='g')
plt.title('Гистограмма распределения частот')
plt.xlabel('Значение')
plt.ylabel('Плотность вероятности')
plt.grid()
plt.show()

# Аппроксимация закона распределения
cv = coeff_var
if cv < 0.1:
    distribution_name = 'Нормальное'
elif cv < 0.5:
    distribution_name = 'Экспоненциальное'
else:
    distribution_name = 'Гиперэкспоненциальное'

print(f'Предполагаемый закон распределения: {distribution_name}')

# Сравнительный анализ
plt.figure(figsize=(12, 6))
plt.hist(generated_data, bins=30, alpha=0.5, label='Сгенерированные данные', density=True)
plt.hist(data, bins=30, alpha=0.5, label='Исходные данные', density=True)
plt.title('Сравнительный анализ распределений')
plt.xlabel('Значение')
plt.ylabel('Плотность вероятности')
plt.legend()
plt.grid()
plt.show()

# Автокорреляционный анализ сгенерированной последовательности
autocorr_generated = autocorrelation(generated_data)

plt.figure(figsize=(12, 6))
plt.stem(autocorr_generated[:20], use_line_collection=True)
plt.title('Автокорреляция сгенерированных данных')
plt.xlabel('Лаг')
plt.ylabel('Коэффициент автокорреляции')
plt.grid()
plt.show()

# Оценка корреляционной зависимости
correlation_coefficient = np.corrcoef(data[:300], generated_data)[0, 1]
print(f'Корреляционная зависимость между исходными и сгенерированными данными: {correlation_coefficient}')
