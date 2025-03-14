# Лабораторная работа #8

## Вариант 69

Доработать программу из [лабораторной работы №7](../lab7) следующим образом:

1. Интерфейс должен быть реализован с помощью библиотеки `JavaFX`
2. Графический интерфейс клиентской части должен поддерживать **русский**, **исландский**, **шведский** и **английский (Индия)** языки / локали. Должно обеспечиваться корректное отображение чисел, даты и времени в соответстии с локалью. Переключение языков должно происходить без перезапуска приложения. Локализованные ресурсы должны храниться в **файле свойств**.

Заменить консольный клиент на клиент с графическим интерфейсом пользователя(GUI).
В функционал клиента должно входить:

1. Окно с авторизацией/регистрацией.
2. Отображение текущего пользователя.
3. Таблица, отображающая все объекты из коллекции
    1. Каждое поле объекта - отдельная колонка таблицы.
    2. Строки таблицы можно фильтровать/сортировать по значениям любой из колонок. Сортировку и фильтрацию значений столбцов реализовать с помощью Streams API.
4. Поддержка всех команд из предыдущих лабораторных работ.
5. Область, визуализирующую объекты коллекции
   1. Объекты должны быть нарисованы с помощью графических примитивов с использованием Graphics, Canvas или аналогичных средств графической библиотеки.
   2. При визуализации использовать данные о координатах и размерах объекта.
   3. Объекты от разных пользователей должны быть нарисованы разными цветами.
   4. При нажатии на объект должна выводиться информация об этом объекте.
   5. При добавлении/удалении/изменении объекта, он должен **автоматически** появиться/исчезнуть/измениться на области как владельца, так и всех других клиентов.
   6. При отрисовке объекта должна воспроизводиться согласованная с преподавателем **анимация**.
6. Возможность редактирования отдельных полей любого из объектов (принадлежащего пользователю). Переход к редактированию объекта возможен из таблицы с общим списком объектов и из области с визуализацией объекта.
7. Возможность удаления выбранного объекта (даже если команды remove ранее не было).

Перед непосредственной разработкой приложения **необходимо** согласовать прототип интерфейса с преподавателем. Прототип интерфейса должен быть создан с помощью средства для построения прототипов интерфейсов(mockplus, draw.io, etc.)

### Вопросы к защите лабораторной работы:

1. Компоненты пользовательского интерфейса. Иерархия компонентов. 
2. Базовые классы `Component`, `Container`, `JComponent`.
3. Менеджеры компоновки.
4. Модель обработки событий. Класс-слушатель и класс-событие.
5. Технология `JavaFX`. Особенности архитектуры, отличия от `AWT` / `Swing`.
6. Интернационализация. Локализация. Хранение локализованных ресурсов.
7. Форматирование локализованных числовых данных, текста, даты и времени. Классы `NumberFormat`, `DateFormat`, `MessageFormat`, `ChoiceFormat`.
