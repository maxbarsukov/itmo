import matplotlib.pyplot as plt
import numpy as np

MESSAGE = "11000001 11001100 11000000"
# MESSAGE = "0 1 0 1 0 1 0 0 0 0 1 1 1 1 0 1 1 0 0" # Пример из презентации

# Сообщение
message = MESSAGE.replace(" ", "") # Удаляем пробелы

# Подготовка данных для графика
bits = len(message)
x = np.zeros(bits * 2)
y = np.zeros(bits * 2)

# Линейное построение графика NRZ
for i in range(bits):
    # Две точки для каждого бита
    x[i*2] = i
    x[i*2+1] = i+1

    if message[i] == '1':
        y[i*2] = 1
        y[i*2+1] = 1
    else: # Если бит = '0'
        y[i*2] = -1
        y[i*2+1] = -1

# Создание графика
plt.figure(figsize=(12, 6))

# Рисуем горизонтальные линии сигнала
for i in range(bits):
    # Определяем цвет для текущего уровня
    color = 'blue' if y[i*2] == 1 else 'red'
    # Рисуем горизонтальную линию
    plt.plot([x[i*2], x[i*2+1]], [y[i*2], y[i*2+1]], color=color, linewidth=2)

# Рисуем вертикальные линии (переходы между уровнями)
for i in range(1, bits * 2):
    if y[i] != y[i-1]:  # Если есть изменение уровня
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
plt.yticks([-1, 0, 1], ['0', '', '1'])
plt.xticks(range(0, bits+1))

# Подписи
plt.title('NRZ (Потенциальный код без возврата к нулю)')
plt.xlabel('Биты')
plt.ylabel('Уровень сигнала')
plt.grid(True, alpha=0.3)

plt.tight_layout()
plt.show()
