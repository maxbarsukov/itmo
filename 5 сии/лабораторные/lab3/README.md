# Лабораторная работа 3. Линейная регрессия

## Вариант `student_performance`

<img alt="ranma-tsubasa" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/ranma-tsubasa.gif" height="180">

> I don't trust linear regressions when it's harder to guess the direction of the correlation from the scatter plot than to find new constellations on it.

## Задание

* Выбор датасетов:
    * Студенты с **четным** порядковым номером в группе должны использовать набор данных о [жилье в Калифорнии](https://developers.google.com/machine-learning/crash-course/california-housing-data-description?hl=ru) Скачать [тут](https://download.mlcc.google.com/mledu-datasets/california_housing_train.csv)
    * Студенты с **нечетным** порядковым номером в группе должны использовать [про обучение студентов](https://www.kaggle.com/datasets/nikhil7280/student-performance-multiple-linear-regression)
* Получите и **визуализируйте** (графически) статистику по датасету (включая количество, среднее значение, стандартное отклонение, минимум, максимум и различные квантили).
* Проведите **предварительную обработку данных**, включая обработку отсутствующих значений, кодирование категориальных признаков и нормировка.
* **Разделите** данные на **обучающий** и **тестовый** наборы данных.
* **Реализуйте линейную регрессию** с использованием метода наименьших квадратов без использования сторонних библиотек, кроме `NumPy` и `Pandas` (для использования коэффициентов использовать библиотеки тоже нельзя). Использовать минимизацию суммы квадратов разностей между фактическими и предсказанными значениями для нахождения оптимальных коэффициентов.
* Постройте **три модели** с различными наборами признаков.
* Для каждой модели проведите оценку производительности, используя **метрику коэффициент детерминации**, чтобы измерить, насколько хорошо модель соответствует данным.
* Сравните результаты трех моделей и сделайте выводы о том, какие признаки работают лучше всего для каждой модели.

### Бонусное задание

  * Ввести синтетический признак при построении модели

### Выполнение задания

Задание выполено в Jupyter Notebook'е [`ais-lab3.ipynb`](./ais-lab3.ipynb)

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [github.com/CandyGoose/AIS/lab3](https://github.com/CandyGoose/Artificial_intelligence_systems/blob/main/lab3/lab3.ipynb) | Пример выполнения ЛР 3, вариант `california_housing_train` |
| [github.com/filberol/labs_sii/lab4](https://github.com/filberol/labs_sii/tree/master/lab4) | Пример выполнения ЛР 3, вариант `student_performance` |
| https://www.geeksforgeeks.org/regression-metrics/ | Метрики регрессии |
| https://www.geeksforgeeks.org/regression-in-machine-learning/ | Регрессия в МО |
| https://habr.com/ru/articles/514818/ | Основы линейной регрессии |
| https://habr.com/ru/companies/skillfactory/articles/848858/ | Предварительная обработка данных в МО |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2024 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
