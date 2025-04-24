import matplotlib.pyplot as plt
import numpy as np

DAYS = 30
DAILY_NEW = 650  # МБ новых данных в день
RETENTION_DAYS = 28  # Хранение последних 28 дампов
COMPRESSION_RATIO = 3  # Сжатие 3:1

days = np.arange(1, DAYS + 1)
daily_size = (DAILY_NEW * days) / COMPRESSION_RATIO

cumulative_size = []
current_total = 0
window = []

for day in days:
    current_size = daily_size[day-1]
    window.append(current_size)

    if len(window) > RETENTION_DAYS:
        removed = window.pop(0)
        current_total -= removed

    current_total += current_size
    cumulative_size.append(current_total)

fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 8), sharex=True)

# График 1: Размер ежедневных дампов
ax1.bar(days, daily_size, color='skyblue', edgecolor='black', label='Размер дампа')
ax1.set_ylabel('Размер дампа (МБ)')
ax1.set_title('Ежедневные резервные копии и накопленный объем')
ax1.grid(linestyle='--', alpha=0.7)
ax1.legend()

# График 2: Накопленный объем
ax2.plot(days, cumulative_size, color='red', marker='o',
        linestyle='-', linewidth=2, markersize=5, label='Накопленный объем')
ax2.set_xlabel('Дни')
ax2.set_ylabel('Общий объем (МБ)')
ax2.grid(linestyle='--', alpha=0.7)
ax2.legend()

ax2.annotate(f'{cumulative_size[-1]/1024:.1f} ГБ',
           xy=(DAYS, cumulative_size[-1]),
           xytext=(DAYS-5, cumulative_size[-1]*0.8),
           arrowprops=dict(facecolor='black', shrink=0.05))

ax1.set_xticks(np.arange(0, DAYS+1, 5))
ax1.set_xlim(0, DAYS+1)

secaxy = ax2.secondary_yaxis('right', functions=(lambda x: x/1024, lambda x: x*1024))
secaxy.set_ylabel('Общий объем (ГБ)')

plt.tight_layout()
plt.show()
