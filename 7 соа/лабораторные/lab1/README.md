# Лабораторная работа №1

## Вариант `8702`

<img alt="evil-iruma-kun-iruma-kun" src="https://github.com/maxbarsukov/itmo/blob/master/.docs/evil-iruma-kun-iruma-kun.gif" height="300">

> [!NOTE]
> Развернутая на сервере `Helios` веб-документация: [se.ifmo.ru/~s367081/soa/lab1/](https://se.ifmo.ru/~s367081/soa/lab1/).

|.pdf|.docx|
|-|-|
| [report](./docs/report.pdf) | [report](./docs/report.docx) |

---

## Задание

Разработать спецификацию в формате OpenAPI для набора веб-сервисов, реализующего следующую функциональность:

**Первый веб-сервис** должен осуществлять управление коллекцией объектов. В коллекции необходимо хранить объекты класса `Person`, описание которого приведено ниже:

```java
public class Person {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Float height; //Поле может быть null, Значение поля должно быть больше 0
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле не может быть null
}
public class Coordinates {
    private int x;
    private int y;
}
public class Location {
    private Integer x; //Поле не может быть null
    private Long y; //Поле не может быть null
    private Integer z; //Поле не может быть null
    private String name; //Длина строки не должна быть больше 704, Поле может быть null
}
public enum Color {
    RED, BLUE, YELLOW, ORANGE;
}
public enum Color {
    GREEN, RED, YELLOW, ORANGE, BROWN;
}
public enum Country {
    CHINA, INDIA, ITALY, NORTH_KOREA;
}
```

Веб-сервис должен удовлетворять следующим требованиям:
- API, реализуемый сервисом, должен соответствовать рекомендациям подхода RESTful.
- Необходимо реализовать следующий базовый набор операций с объектами коллекции: добавление нового элемента, получение элемента по ИД, обновление элемента, удаление элемента, получение массива элементов.
- Операция, выполняемая над объектом коллекции, должна определяться методом HTTP-запроса.
- Операция получения массива элементов должна поддерживать возможность сортировки и фильтрации по любой комбинации полей класса, а также возможность постраничного вывода результатов выборки с указанием размера и порядкового номера выводимой страницы.
- Все параметры, необходимые для выполнения операции, должны передаваться в URL запроса.
- Информация об объектах коллекции должна передаваться в формате **json**.
- В случае передачи сервису данных, нарушающих заданные на уровне класса ограничения целостности, сервис должен возвращать код ответа HTTP, соответствующий произошедшей ошибке.

Помимо базового набора, веб-сервис должен поддерживать следующие операции над объектами коллекции:
- Удалить все объекты, значение поля `nationality` которого эквивалентно заданному.
- Удалить один (любой) объект, значение поля `location` которого эквивалентно заданному.
- Вернуть массив объектов, значение поля `location` которых больше заданного.

Эти операции должны размещаться на отдельных URL.

**Второй веб-сервис** должен располагаться на URL `/demography`, и реализовывать ряд дополнительных операций, связанных с вызовом API первого сервиса:
- `/hair-color/{hair-color}/percentage` : вывести долю людей с заданным цветом волос в общей популяции (в процентах).
- `/eye-color/{eye-color}` : вывести количество людей с заданным цветом глаз.

Эти операции также должны размещаться на отдельных URL.

Для разработанной спецификации необходимо сгенерировать интерактивную веб-документацию с помощью [Swagger UI](https://swagger.io/tools/swagger-ui/). Документация должна содержать описание всех REST API обоих сервисов с текстовым описанием функциональности каждой операции. Созданную веб-документацию необходимо развернуть на сервере `Helios`.

### Вопросы к защите лабораторной работы

1. Подходы к проектированию приложений. "Монолитная" и сервис-ориентированная архитектура.
2. Понятие сервиса. Общие свойства сервисов.
3. Основные принципы SOA. Подходы к реализации SOA, стандарты и протоколы.
4. Общие принципы построения и элементы сервис-ориентированных систем.
5. Понятие веб-сервиса. Определение, особенности, отличия от веб-приложений.
6. Категоризация веб-сервисов. RESTful и SOAP. Сходства и отличия, области применения.
7. RESTful веб-сервисы. Особенности подхода. Понятия ресурса, URI и полезной нагрузки (payload).
8. Виды RESTful-сервисов. Интерпретация методов HTTP в RESTful.
9. Правила именования ресурсов в RESTful сервисах.
10. Спецификация RESTful-сервисов. Стандарт OpenAPI.
11. Автодокументирование RESTful-сервисов. Swagger Editor, Swagger UI (и Swagger Codegen).
12. Архитектурный принцип HATEOAS.

---

## Полезные ссылки

| Ссылка | Описание |
| --- | --- |
| [swagger.io](https://swagger.io/) | Swagger: API Documentation & Design Tools |
| [editor.swagger.io](https://editor.swagger.io/) | Swagger Editor |
| [swagger.io/tools/swagger-codegen/](https://swagger.io/tools/swagger-codegen/) | Swagger Codegen |
| [spec.openapis.org/oas/latest.html](https://spec.openapis.org/oas/latest.html) | OpenAPI Specification |
| [restfulapi.net](https://restfulapi.net/) <br> [restfulapi.net/http-methods/](https://restfulapi.net/http-methods/) <br> [restfulapi.net/http-status-codes/](https://restfulapi.net/http-status-codes/) | REST API Tutorial & Guides |
| [habr.com/ru/articles/483328/](https://habr.com/ru/articles/483328/) | REST API — Что такое HATEOAS? |
| [restcookbook.com/Basics/hateoas/](https://restcookbook.com/Basics/hateoas/) | What is HATEOAS and why is it important for my REST API? |
| [docs.oasis-open.org/soa-rm/...](https://docs.oasis-open.org/soa-rm/soa-ra/v1.0/cs01/soa-ra-v1.0-cs01.html) | Reference Architecture Foundation for Service Oriented Architecture Version 1.0 |
| [en.wikipedia.org/wiki/Service-oriented_architecture](https://en.wikipedia.org/wiki/Service-oriented_architecture) | Service-oriented architecture |
| [www.w3.org/TR/soap12/](https://www.w3.org/TR/soap12/) | SOAP Version 1.2 Part 1: Messaging Framework (Second Edition) |

## Лицензия <a name="license"></a>

Проект доступен с открытым исходным кодом на условиях [Лицензии MIT](https://opensource.org/licenses/MIT). \
*Авторские права 2025 Max Barsukov*

**Поставьте звезду :star:, если вы нашли этот проект полезным.**
