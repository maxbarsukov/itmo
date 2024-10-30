# Лабораторная работа 4. Метод k-ближайших соседей

## Вариант `diabetes`

<img alt="myneigbortotoro" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/myneigbortotoro.gif" height="200">

> Почему КНН, если [ТНН](https://neolurk.org/wiki/%D0%A2%D1%8F%D0%BD_%D0%BD%D0%B5_%D0%BD%D1%83%D0%B6%D0%BD%D1%8B)?

## Задание

* Выбор датасетов:
    * Студенты с **четным** порядковым номером в группе должны использовать набор данных [о вине](https://www.kaggle.com/datasets/davorbudimir/winedataset).
    * Студенты с **нечетным** порядковым номером в группе должны использовать набор данных [про диабет](https://www.kaggle.com/datasets/abdallamahgoub/diabetes/data)
* Проведите **предварительную обработку** данных, включая обработку **отсутствующих значений**, кодирование категориальных признаков и **масштабирование**.
* Получите и **визуализируйте** (графически) статистику по датасету (включая количество, среднее значение, стандартное отклонение, минимум, максимум и различные квантили), постройте **3d-визуализацию признаков**.
* Реализуйте **метод k-ближайших соседей** *без использования сторонних библиотек*, кроме `NumPy` и `Pandas`.
* Постройте две модели k-NN с различными наборами признаков:
    * **Модель 1**: Признаки случайно отбираются .
    * **Модель 2**: Фиксированный набор признаков, который выбирается заранее.
* Для каждой модели проведите оценку на тестовом наборе данных при разных значениях `k`. Выберите несколько различных значений `k`, например, `k=3`, `k=5`, `k=10`, и т. д. Постройте матрицу ошибок.

## Выполнение задания

| .pdf | .markdown |
|-|-|
| [jupyter-nb-ais-4](./ais-lab4.pdf) | [jupyter-nb-ais-4](./ais-lab4.md) |

*Примечание:* в закомментированном коде и тексте, начинающемся с `->`, я пытался лучше обработать исходные данные, но в таком случае точность kNN становится удручающе низкой. Использовать по своему усмотрению.

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [github.com/CandyGoose/AIS/lab4](https://github.com/CandyGoose/Artificial_intelligence_systems/blob/main/lab3/lab3.ipynb) | Пример выполнения ЛР 4, вариант `wines` |
| [ru.wikipedia.org/wiki/Метод_k_ближайших_соседей](https://ru.wikipedia.org/wiki/%D0%9C%D0%B5%D1%82%D0%BE%D0%B4_k_%D0%B1%D0%BB%D0%B8%D0%B6%D0%B0%D0%B9%D1%88%D0%B8%D1%85_%D1%81%D0%BE%D1%81%D0%B5%D0%B4%D0%B5%D0%B9) | kNN на wiki |
| [education.yandex.ru/handbook/ml/article/metricheskiye-metody](https://education.yandex.ru/handbook/ml/article/metricheskiye-metody) | Метрические методы и kNN |
| [habr.com/ru/articles/680004/](https://habr.com/ru/articles/680004/) | kNN -- реализация с нуля на Python |
| [proglib.io/p/metod-k-blizhayshih-sosedey](https://proglib.io/p/metod-k-blizhayshih-sosedey-k-nearest-neighbour-2021-07-19) | Метод k-ближайших соседей |
| [www.machinelearning.ru/wiki/index.php?title=Гипотеза_компактности](http://www.machinelearning.ru/wiki/index.php?title=%D0%93%D0%B8%D0%BF%D0%BE%D1%82%D0%B5%D0%B7%D0%B0_%D0%BA%D0%BE%D0%BC%D0%BF%D0%B0%D0%BA%D1%82%D0%BD%D0%BE%D1%81%D1%82%D0%B8) | Гипотеза компактности |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2024 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
