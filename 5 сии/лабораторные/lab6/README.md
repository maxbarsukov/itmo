# Лабораторная работа 6. Логистическая регрессия

## Вариант `diabetes`

<img alt="rei-falling" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/rei-falling.gif" height="300">

> Sigmoid (Patrick Bateman gigachad)

## Задание

1. Выбор датасетов:
    * Студенты с **четным** порядковым номером в группе должны использовать датасет о [пассажирах Титаника](https://www.kaggle.com/c/titanic);
    * Студенты с **нечетным** порядковым номером в группе должны использовать датасет о [диабете](https://www.kaggle.com/datasets/uciml/pima-indians-diabetes-database).
2. Загрузите выбранный датасет и выполните **предварительную обработку данных**.
3. Получите и **визуализируйте** (графически) статистику по датасету (включая *количество, среднее значение, стандартное отклонение, минимум, максимум и различные квантили*).
4. Разделите данные на **обучающий** и **тестовый** наборы в соотношении, которое вы считаете подходящим.
5. Реализуйте **логистическую регрессию** "с нуля" без использования сторонних библиотек, кроме `NumPy` и `Pandas`. Ваша реализация логистической регрессии **должна включать** в себя:
   - Функцию для вычисления гипотезы (`sigmoid function`).
   - Функцию для вычисления функции потерь (`log loss`).
   - **Метод обучения**, который включает в себя **градиентный спуск**.
   - Возможность **варьировать гиперпараметры**, такие как **коэффициент обучения** (`learning rate`) и **количество итераций**.
6. **Исследование гиперпараметров**:
   - Проведите исследование влияния гиперпараметров на производительность модели. **Варьируйте следующие гиперпараметры**:
     - **Коэффициент обучения** (`learning rate`).
     - **Количество итераций** обучения.
     - **Метод оптимизации** (например, градиентный спуск или оптимизация Ньютона).
7. **Оценка модели**:
    - Для каждой комбинации гиперпараметров **оцените производительность модели** на тестовом наборе данных, используя метрики, такие как `accuracy`, `precision`, `recall` и `F1-Score`.

Сделайте **выводы** о том, какие значения гиперпараметров наилучшим образом работают для данного набора данных и задачи классификации.

Обратите внимание на изменение производительности модели при варьировании гиперпараметров.

## Выполнение задания

| .pdf | .markdown |
|-|-|
| [jupyter-nb-ais-6](./ais-lab6.pdf) | [jupyter-nb-ais-6](./ais-lab6.md) |

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [github.com/CandyGoose/AIS/lab6](https://github.com/CandyGoose/Artificial_intelligence_systems/blob/main/lab6/lab6.ipynb)| Пример выполнения ЛР 6, вариант `diabetes` |
| [www.dmitrymakarov.ru/opt/logistic-regression-05/](https://www.dmitrymakarov.ru/opt/logistic-regression-05/) | Логистическая регрессия |
| [education.yandex.ru/handbook/data-analysis/article/logisticheskaya-regressiya](https://education.yandex.ru/handbook/data-analysis/article/logisticheskaya-regressiya) | Логистическая регрессия |
| [habr.com/ru/articles/428794/](https://habr.com/ru/articles/428794/) | Обзор основных методов математической оптимизации для задач с ограничениями |
| [education.yandex.ru/handbook/ml/article/optimizaciya-v-ml](https://education.yandex.ru/handbook/ml/article/optimizaciya-v-ml) | Оптимизация в ML |
| [habr.com/ru/companies/ods/articles/328372/](https://habr.com/ru/companies/ods/articles/328372/) | Метрики в задачах машинного обучения |
| [ru.wikipedia.org/wiki/Темп_обучения](https://ru.wikipedia.org/wiki/%D0%A2%D0%B5%D0%BC%D0%BF_%D0%BE%D0%B1%D1%83%D1%87%D0%B5%D0%BD%D0%B8%D1%8F) | Learning rate |
| [en.wikipedia.org/wiki/Sigmoid_function](https://en.wikipedia.org/wiki/Sigmoid_function) | Sigmoid function |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2024 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
