# Лабораторная работа 5. Деревья решений

## Вариант `student-grades`

<img alt="momiji-homura" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/momiji-homura.gif" height="280">

> Используешь для классификации Decision Tree? Чел хорош \
> 🔫💀🔫 \
> А как насчёт Wise Mystical Tree?

## Задание

1. Выбор датасетов:
    * Студенты с **четным** порядковым номером в группе должны использовать датасет с [классификацией грибов](https://archive.ics.uci.edu/dataset/73/mushroom);
    * Студенты с **нечетным** порядковым номером в группе должны использовать датасет с [данными про оценки студентов инженерного и педагогического факультетов](https://archive.ics.uci.edu/dataset/856/higher+education+students+performance+evaluation) (для данного датасета нужно ввести метрику: студент успешный/неуспешный на основании грейда).
2. Отобрать **случайным** образом `sqrt(n)` признаков.
3. Реализовать *без использования сторонних библиотек* построение **дерева решений**  (дерево не бинарное, `numpy` и `pandas` использовать можно, использовать список списков  для реализации  дерева - нельзя) для **решения задачи бинарной классификации**.
4. Провести оценку реализованного алгоритма с использованием `Accuracy`, `Precision` и `Recall`.
5. Построить кривые `AUC-ROC` и `AUC-PR` (в пунктах 4 и 5 **использовать библиотеки нельзя**).

## Выполнение задания

| .pdf | .markdown |
|-|-|
| [jupyter-nb-ais-5](./ais-lab5.pdf) | [jupyter-nb-ais-5](./ais-lab5.md) |

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [sunnysubmarines.notion.site/decision-trees](https://sunnysubmarines.notion.site/1-4ac65285f2464ab6964367456fd0206a) | Деревья решений. Теоретическая часть (1) |
| [github.com/CandyGoose/AIS/lab5](https://github.com/CandyGoose/Artificial_intelligence_systems/blob/main/lab5/lab5.ipynb) | Пример выполнения ЛР 5, вариант `mushroom` |
| [https://ru.wikipedia.org/wiki/Дерево_решений](https://ru.wikipedia.org/wiki/%D0%94%D0%B5%D1%80%D0%B5%D0%B2%D0%BE_%D1%80%D0%B5%D1%88%D0%B5%D0%BD%D0%B8%D0%B9) | Дерево решений |
| [habr.com/ru/articles/801515](https://habr.com/ru/articles/801515/) | Дерево решений (CART). От теоретических основ до продвинутых техник и реализации с нуля на Python |
| [loginom.ru/blog/classification-quality](https://loginom.ru/blog/classification-quality) | **Метрики качества моделей бинарной классификации** |
| [habr.com/ru/articles/661119](https://habr.com/ru/articles/661119/) | Precision и recall. Как они соотносятся с порогом принятия решений? |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии GNU GPL 3](https://opensource.org/license/gpl-3-0/). \
*Авторские права 2024 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
