# Вопросы к зачёту

```python
import random

def gen(n):
    while True:
        buf = list(range(1, n + 1))
        random.shuffle(buf)
        for v in buf:
            yield v

qs_counts = {
    'all': 22
}

qs = {gr: gen(count) for gr, count in qs_counts.items()}

def tickets():
    print(f"1.  - {qs['all'].__next__()} - / - ")

tickets()
```

Билет для зачёта содержит два вопроса:

1. Вопрос по теоретическому материалу курса (список приведён ниже).
2. Вопрос по материалу из вами выбранной книги (см. `Лабораторная работа №0`). Текст книги должен быть предоставлен учащимся во время зачёта (если у вас только бумажная копия -- фотографии оглавления и индекса).

## Вопросы по теории

(2024)

1. Чистые и грязные функции. Изменяемое состояние. Тотальность функций.
1. Рекурсия. Хвостовая рекурсия. Структурная рекурсия.
1. Мемоизация.
1. Чисто функциональные структуры данных.
1. Ссылочная прозрачность. Сопоставление с образцом.
1. Алгебраический (индуктивный) тип данных. Сравнение с системой типов языка Си. Сопоставление с образцом.
1. Функции высших порядков. Замыкания (Closure). Работа с изменяемым состоянием и областью видимости переменных в функциональных и нефункциональных языках.
1. Свёртки. Отображения. Фильтрация. Map-reduce. Бесточечный стиль, комбинаторы.
1. Ленивые структуры данных, итераторы, генераторы. Побочные эффекты.
1. Динамическая верификация. Модульное и интеграционное тестирование. REPL. Doctest.
1. Динамическая верификация. Golden tests. Fuzzy/Monkey testing. Property-Based Testing.
1. Статическая верификация. Виды систем типов. Понятие "объекта первого класса".
1. Статическая верификация. Механика автоматического вывода типов.
1. Полиморфизм. Универсальный (параметрический и через наследование). и специальный (ограниченный, через перегрузку, через приведение типов).
1. Полиморфизм. Специальный (ограниченный, через перегрузку, через приведение типов).
1. Полиморфизм. Множественный полиморфизм.
1. Лямбда исчисление. Виды термов. Связанные и свободные переменные. Альфа, бета и эта преобразования. Порядок редукции. Булева алгебра и нумералы Чёрча.
1. Структурное программирование, преимущества перед языками с Go To. Аргументация Дейкстры против Go To.
1. Варианты практичного использования Go To сегодня. Механизм Defer языка Golang.
1. Нарушение структурной организации кода. Исключения. Состояния и перезапуски.
1. Нарушение структурной организации кода. Динамическая область видимости переменных.
1. Монады. Связывание операций. Монада Identity, Maybe и State.
<!-- 1. Изменяемое состояние: необходимость и вред. Проблемы нарушения инвариантов составных объектов и построения кешей. Двухфазный цикл жизни объектов. Управляемое и инкапсулированное изменяемое состояние (их отличия). Примеры. Практики в современной разработке. -->
