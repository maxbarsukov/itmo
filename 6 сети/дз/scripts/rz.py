import matplotlib.pyplot as plt
import numpy as np

MESSAGE = "11000001 11001100 11000000"
# MESSAGE = "0 1 0 1 0 1 0 0 0 0 1 1 1 1 0 1 1 0 0" # Пример из презентации

# Сообщение
message = MESSAGE.replace(" ", "") # Удаляем пробелы

# Подготовка данных для графика
bits = len(message)
# Для каждого бита нам нужно 4 точки (начало, середина-вверх, середина-вниз, конец)
x = np.zeros(bits * 4)
y = np.zeros(bits * 4)

# Линейное построение графика RZ
for i in range(bits):
    # Каждый бит занимает интервал от i до i+1
    x[i*4] = i      # Начало бита
    x[i*4+1] = i+0.5  # Середина бита (первая точка)
    x[i*4+2] = i+0.5  # Середина бита (вторая точка)
    x[i*4+3] = i+1    # Конец бита

    if message[i] == '1':
        # Для '1' сигнал положительный на первой половине, затем возврат к нулю
        y[i*4] = 1      # Начало с высокого уровня
        y[i*4+1] = 1    # До середины высокий
        y[i*4+2] = 0    # После середины возврат к нулю
        y[i*4+3] = 0    # Конец на нулевом уровне
    else: # Если бит = '0'
        # Для '0' сигнал отрицательный на первой половине, затем возврат к нулю
        y[i*4] = -1     # Начало с низкого уровня
        y[i*4+1] = -1   # До середины низкий
        y[i*4+2] = 0    # После середины возврат к нулю
        y[i*4+3] = 0    # Конец на нулевом уровне

# Создание графика
plt.figure(figsize=(12, 6))

# Рисуем горизонтальные линии сигнала
for i in range(bits):
    # Определяем цвет для текущего уровня
    if message[i] == '1':
        color = 'blue' # Уровень 1
    else:
        color = 'red' # Уровень 0
    # Рисуем горизонтальные линии
    plt.plot([x[i*4], x[i*4+1]], [y[i*4], y[i*4+1]], color=color, linewidth=2) # Первая половина бита
    plt.plot([x[i*4+2], x[i*4+3]], [y[i*4+2], y[i*4+3]], color='black', linewidth=2) # Вторая половина бита (возврат к нулю)

# Рисуем вертикальные линии (переходы между уровнями)
for i in range(1, bits * 4):
    if y[i] != y[i-1]: # Если есть изменение уровня
        plt.plot([x[i], x[i]], [y[i-1], y[i]], color='black', linewidth=2)

# Горизонтальная линия для нуля
plt.axhline(y=0, color='black', linestyle='-', alpha=0.3)

# Добавление вертикальных линий для обозначения границ битов
for i in range(bits+1):
    plt.axvline(x=i, color='gray', linestyle='--', alpha=0.5)

# Добавление меток битов
for i in range(bits):
    color = 'red' if message[i] == '0' else 'blue'
    plt.text(i+0.5, -1.5, message[i], horizontalalignment='center', fontsize=12, color=color, fontweight='bold')

# Настройка осей
plt.ylim(-2, 2)
plt.yticks([-1, 0, 1], ['-1', '0', '1'])
plt.xticks(range(0, bits+1))

# Подписи
plt.title('RZ (Биполярный импульсный код с возвратом к нулю)')
plt.xlabel('Биты')
plt.ylabel('Уровень сигнала')
plt.grid(True, alpha=0.3)

plt.tight_layout()
plt.show()
