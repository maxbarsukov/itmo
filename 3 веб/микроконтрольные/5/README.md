# Микроконтрольная №5

## Вариант 13579

- [Разбор микроконтрольной](https://github.com/worthant/web-programming-course/blob/main/test5.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-13579) от [*@worthant*](https://github.com/worthant);
- [Разбор микроконтрольной](https://github.com/eliteSufferer/ITMO_Studies/blob/main/Web/mk5.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-13579) от [*@eliteSufferer*](https://github.com/eliteSufferer).

### 1. Чем `@CustomScoped`-бины отличаются от `@ViewScoped`?

- `@ViewScoped`: управляемый бин существует на протяжении времени жизни представления JSF (**пока страница показывается**).
- `@CustomScoped`: бин будет жить столько, сколько он **будет находится в Map**, которая создается для контроля времени жизни бинов.

Любое EL-выражение, разрешающееся в ссылку на реализацию `Map` может быть использовано как  область видимости управляемого бина. Например, этот бин использует аннотацию `@CustomScoped`:

```java
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.CustomScoped;

@ManagedBean
@CustomScoped("#{shoppingCart.items}")
public class CartItems {
    // ... properties omitted
}
```
`Custom Scope` позволяет использовать свое собственное хранилище экземпляров управляемых бинов.


### 2. Чем конструкция `${bean.property}` отличается от конструкции `#{bean.property}`?

- `${}` **Immediate value expressions** — выполняются сразу при отображении страницы
- `#{}` **Deferred value expressions** — выполняются тогда, когда это необходимо (когда к ним обращаются).

Value-expressions могут быть дополнительно классифицированы на выражения `rvalue` и `lvalue`. Выражения `rvalue` могут считывать данные, но не могут их записывать. Выражения `lvalue` могут как считывать, так и записывать данные.

Все выражения, которые вычисляются **немедленно** (**immediately**), используют разделители `${}` и всегда являются выражениями `rvalue`. Выражения, вычисление которых может быть **отложено** (**deferred**), используют разделители `#{}` и могут выступать как в качестве выражений `rvalue`, так и в качестве выражений `lvalue`. Рассмотрим следующие два выражения значений:

```jsp
${customer.name}
#{customer.name}
```

Первый использует синтаксис **немедленной** оценки, в то время как второй использует синтаксис **отложенной** оценки. Первое выражение обращается к свойству `name`, получает его значение, добавляет значение к ответу и выводится на страницу. То же самое может произойти и со вторым выражением. Однако обработчик тега может **отложить вычисление** этого выражения на более поздний период жизненного цикла страницы, если *позволяет технология, использующая этот тег*.


### 3. Код управляемого бина, содержащего метод, возвращающий время с момента рестарта сервера или деплоя приложения.

Считает количество *миллисекунд* с момента развертывания приложения или последнего рестарта сервера.

Bean создаётся при запуске приложени, так как он `@ApplicationScoped` и установлен `eager=true`.

<details>
  <summary>Подробнее про `eager=true`</summary>

  Управляемые bean-компоненты создаются **лениво**. То есть они создаются **при выполнении запроса из приложения**.

  Чтобы принудительно создать экземпляр bean-компонента области приложения и поместить его в application scope **сразу после запуска** приложения и до того, как будет сделан какой-либо запрос, атрибуту `eager` управляемого bean-компонента должно быть присвоено значение `true`.
</details>


```java
@ManagedBeann(name="timerBean", eager=true)
@ApplicationScoped
public class TimerBean {
    private long startTime;

    public TimerBean() {
        this.startTime = System.currentTimeMills();
    }

    public long getMillisecondsAfterRestart() {
        return System.currentTimeMills() - startTime;
    }
}
```

---

## Вариант 02468

- [Разбор микроконтрольной](https://github.com/worthant/web-programming-course/blob/main/test5.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-24680) от [*@worthant*](https://github.com/worthant);
- [Разбор микроконтрольной](https://github.com/eliteSufferer/ITMO_Studies/blob/main/Web/mk5.md#%D0%B2%D0%B0%D1%80%D0%B8%D0%B0%D0%BD%D1%82%D1%8B-24680) от [*@eliteSufferer*](https://github.com/eliteSufferer).

:sparkles: Мне тааааак лень разбирать не свой вариант, запуллреквестите пж как сами разберёте :)

### 1. Чем `@NoneScoped`-бины отличаются от `@ApplicationScoped`?

### 2. Какой из этих фрагментов кода не скомпилируется и почему? **(1)** `#{bean.action}`; **(2)** `#{bean.action()}`

### 3. Код JSF-страницы, показывающей значение параметра `user-name` из HTTP-запроса.
