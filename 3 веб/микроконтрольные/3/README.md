# Микроконтрольная №3

## Вариант 01234

- [Разбор микроконтрольной](https://github.com/worthant/web-programming-course/blob/main/test3.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-01234) от [*@worthant*](https://github.com/worthant);
- [Разбор микроконтрольной](https://github.com/eliteSufferer/ITMO_Studies/blob/main/Web/web_mk3.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-01234) от [*@eliteSufferer*](https://github.com/eliteSufferer).

### 1. В чём отличия типизации между JS и Java?

**Java**:
- Статическая
- Сильная
- Явная

**JavaScript**:
- Динамическая
- Слабая
- Неявная

<details> 
  <summary><b>Ликбез по типизации в ЯП</b></summary>
   
   [Статья на Хабре](https://habr.com/ru/articles/161205/)

   - **Статическая / динамическая** типизация. Статическая определяется тем, что конечные типы переменных и функций устанавливаются на этапе компиляции. Т.е. уже компилятор на 100% уверен, какой тип где находится. В динамической типизации все типы выясняются уже во время выполнения программы.
   - **Сильная / слабая** типизация (также иногда говорят **строгая / нестрогая**). Сильная типизация выделяется тем, что язык не позволяет смешивать в выражениях различные типы и не выполняет автоматические неявные преобразования, например нельзя вычесть из строки множество. Языки со слабой типизацией выполняют множество неявных преобразований автоматически, даже если может произойти потеря точности или преобразование неоднозначно.
   - **Явная / неявная** типизация. Явно-типизированные языки отличаются тем, что тип новых переменных / функций / их аргументов нужно задавать явно. Соответственно языки с неявной типизацией перекладывают эту задачу на компилятор / интерпретатор.
</details>

### 2. Для чего нужна конструкция `@mixin` в SCSS?

**Миксины** - блоки `SASS` кода (или **примеси-шаблоны**), которые могут принимать аргументы (опционально) и позволяют значительно расширить возможности написания стилей и *сократить затраты времени на применении однотипных правил и даже целых CSS блоков*. Это что-то вроде функции, которая может принять аргумент, выполнить огромный объем работы и выдать результат в зависимости от входного параметра.

Миксин объявляется директивой `@mixin`, после объявления должно быть указано имя миксина. Вызывается миксин директивой `@include`, которая принимает имя миксина и передаваемые аргументы, если такие имеют место быть.

```sass
@mixin border($color)
  border: $color 1px solid
 
p
  @include border(#333)
 
 
@mixin transition($time)
  -webkit-transition: all $time ease;
  -moz-transition:    all $time ease;
  -o-transition:      all $time ease;
  transition:         all $time ease;
 
p
  @include transition(.25s)
```

### 3. JavaScript-функция, удаляющая все гиперссылки со страницы.

Заменяет все ссылки на текст:

```javascript
function removeLinks() {
    const links = document.querySelectorAll("a");
    for (let i = 0; i < links.length; i++) {
        const span = document.createElement("span");
        const link = links[i];
        if (link.className) span.className = link.className;
        if (link.id) span.id = link.id;

        span.innerHTML = link.innerHTML;

        links[i].parentNode.replaceChild(span, links[i]);
    }
}
```

Удаляет аттрибут `href` у гиперссылок:

```javascript
function removeLinks() {
    const links = document.querySelectorAll("a");
    for (let i = 0; i < links.length; i++) {
        links[i].removeAttribute("href");
    }
}
```

---

## Вариант 56789

- [Разбор микроконтрольной](https://github.com/worthant/web-programming-course/blob/main/test3.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-56789) от [*@worthant*](https://github.com/worthant);
- [Разбор микроконтрольной](https://github.com/eliteSufferer/ITMO_Studies/blob/main/Web/web_mk3.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-56789) от [*@eliteSufferer*](https://github.com/eliteSufferer).

### 1. Для чего нужна конструкция `@extend` в SCSS?

Иногда вам приходится писать один и тот же набор свойств в разных правилах CSS.

Предположим, к примеру, что ваш дизайн использует небольшие заглавные буквы по всей странице: кнопки, панель навигации, заголовки боковой панели, вкладки и др.

Как бы это выглядело в вашем CSS? Вы можете:
- использовать общий класс CSS, вроде `.small-uppercase`;
- группировать селекторы;
- использовать расширение SASS.

Использование правила CSS `.small-uppercase` семантически некорректно, потому что вы в конечном итоге пишете ваш HTML как `<p class="small-uppercase">`, что возвращает, в целом, к написанию стилей в вашем HTML.

`@extend` в SASS позволяет наследовать свойства CSS от другого селектора:

```scss
.small-uppercase {
  color: lightslategrey;
  font-size: 10px;
  letter-spacing: 0.1em;
  line-height: 12px;
  text-transform: uppercase;
}
  
.modal-background {
  @extend .small-uppercase;
}
.product-link {
  @extend .small-uppercase;
}
```

Генерируемый CSS:
```css
.small-uppercase,
.modal-background,
.product-link {
  color: lightslategrey;
  font-size: 10px;
  letter-spacing: 0.1em;
  line-height: 12px;
  text-transform: uppercase;
}
```

`@extend` перегруппирует общие свойства под списком селекторов.

Этот список прост в обслуживании, потому что вы только добавляете селекторы один за другим и непосредственно в соответствующем селекторе.

Ваш HTML остаётся семантическим, поскольку каждый элемент сохраняет своё описательное имя класса.

**Итак, вы могли бы думать: «Подождите, это же тогда как примеси?».**

Есть два отличия.
- У правила `@extend` нет параметров. У примесей есть.
- Правило `@extend` группирует селекторы. Примеси нет.

Список свойств просто повторяется столько раз, сколько вызывается `@include overlay()`.

Правило `@extend` является более эффективным, поскольку оно записывает общие свойства только один раз.

**Разница между расширением, заполнителем и примесью:**

| | Описание | Вызов | Группирует селекторы? | Допускает параметры? |
| - | - | - | - | - |
| Примеси | `@mixin name()` | `@include name()` | Нет | Да |
| Расширения | Любой класс | `@extend .class` | Да | Нет |
| Заполнители | `%placeholder` | `@extend %placeholder` | Да | Нет |


### 2. Прицнипы реализации наследования в JS (`ES5`).

- [Полезная ссылка 1](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Inheritance_and_the_prototype_chain)
- [Полезная ссылка 2](https://habr.com/ru/articles/189432/)

Идея наследования в JS крутится вокруг клонирования методов объекта и дополения его собственным поведением. Объект, который клонируется, называется **прототипом** (не путать со свойством prototype у функций).

**Прототип** — обычный объект, которому довелось расширять методы другого объекта — он выступает как родитель объекта. (Это — несколько смещённое понятие относительно общепринятого, когда родителем называют функцию-конструктор, содержащую этот прототип).

Однако, клонирование не означает, что вы будете иметь различные копии функций или данных. На самом деле, в JS реализовано наследование через делегирование: все свойства хранятся у родителя, а наследникам дают попользоваться.

Цепочка родителей определена в скрытых объектах каждого родителя, именуемых `[[Prototype]]`. Их нельзя изменить напрямую (кроме реализаций, где поддерживается `.__proto__`), поэтому единственный (специфицированный) способ — сеттеры при создании.


```javascript
function Person(name, age) {
    this.name = name;
    this.age = age;
}

const me = new Person('Joe', 20);
console.log(me); // {name: 'Joe', age: 20}

Person.prototype.greet = function() {
  console.log('Hi', this.name);
}

me.greet(); // Hi Joe
const you = new Person('Alice', 22);
you.greet(); // Hi Alice

// Наследование
function Employee(name, age, title) {
  Person.call(this, name, age);
  this.title = title;
}

// создание Employee прототипа из Person прототипа
Employee.prototype = Object.create(Person.prototype);

const joe = new Employee('Joe', 22, 'Developer');
console.log(joe.name); // Joe
joe.greet(); // Hi Joe
```

### 3. JS-функция, заменяющая содержимое адресной строки браузера на https://se.ifmo.ru

**Редирект на страницу:**

```javascript
function goToIfmo() {
    window.location.replace("https://se.ifmo.ru");
}
```

**Заменить адресную строку без обновления:**

Использует [History API](https://developer.mozilla.org/en-US/docs/Web/API/History_API).

Работает только на том же домене, на который обновляется URL, так как:

> Новый URL-адрес должен иметь то же происхождение, что и текущий URL-адрес; в противном случае `pushState()` выдаст исключение.


```javascript
function goToIfmo() {
    var newUrl = "https://se.ifmo.ru";
    window.history.pushState(
        { path: newUrl },
        "Главная - Программная инженерия - Кафедра ВТ",
        newUrl
    );
}
```

или

```javascript
...
    window.history.replaceState(...)
...
```

Разница между методом `pushState()` и `replaceState()`, как следует из названия, заключается в том, что `pushState()` добавляет новую историю в браузер. С другой стороны, `replateState()` метод заменяет текущую историю новой.

Если находясь на `/settings` сделать `replaceState()` на `/about`, то нельзя будет вернуться к `/settings`, потому что этот метод заменит историю этой страницы на `/about`.
