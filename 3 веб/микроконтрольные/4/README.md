# Микроконтрольная №4

## Вариант 13579

### 1. Почему `RequestDispatcher` через контекст сервлета можно получить только по абсолютному пути?

#### Коротко и на английском

> \> The ServletContext object is an interface that provides a view of the entire web application. It is not tied to any particular client request, and as such, it doesn't have the context of a 'current' location within the app. Because it serves application-level information, it has no way of knowing what a 'relative' path would mean.
> 
> \> In contrast, ServletRequest is specific to the client's request and has the context of the request URI. So, it knows what a 'relative' path would be relative to.

*by Boris Dvorkin ([@worthant](https://github.com/worthant))*

#### Длинно и на русском

Контекст хранится на уровне приложения. Веб-контейнер запускает приложения одно за другим и запускает их внутри своей JVM. Он хранит singleton-объект в своей JVM, где регистрирует любой объект, помещенный в него. Этот singleton-объект используется всеми приложениями, работающими внутри него, поскольку он хранится внутри JVM самого контейнера.

`ServletContext` — это объект с гораздо более широким scope'ом (весь контекст сервлета), чем `ServletRequest`.

`getServletContext().getRequestDispatcher("path")` означает, что отправка осуществляется относительно корня `ServletContext`.

Таким образом, у контекста сервлета **нет** текущего запроса, *относительно* которого можно перейти по какому-либо *относительному* пути, то есть через контекст `RequestDispatcher` можно получить только по абсолютному пути.

### 2. Какие реализации интерфейса `javax.servlet.Servlet` входят в состав Jakarta EE 9, и в чём разница между ними?

#### GenericServlet

Определяет универсальный, независимый от протокола сервлет. `GenericServlet` реализует интерфейсы `Servlet` и `ServletConfig`.

`GenericServlet` может быть напрямую расширен с помощью сервлета, хотя чаще всего расширяется подкласс, зависящий от протокола, например `HttpServlet`.

`GenericServlet` упрощает написание сервлетов. Он предоставляет простые версии методов жизненного цикла `init` и `destroy` методов интерфейса `ServletConfig`. `GenericServlet` также реализует `log` метод, объявленный в `ServletContext` интерфейсе.

Чтобы написать generic сервлет, вам нужно всего лишь переопределить абстрактный `service` метод.
  
#### HttpServlet

Предоставляет абстрактный класс, который можно разбить на подклассы для создания HTTP-сервлета, подходящего для веб-сайта. Подкласс `HttpServlet` должен переопределить хотя бы один метод, обычно один из следующих: `doGet`, `doPost`, `doPut`, `doDelete`, `init` & `destroy` и т.д.

#### FacesServlet

`FacesServlet` — это Jakarta сервлет, который управляет жизненным циклом обработки запросов для веб-приложений, использующих `Jakarta Server Faces` (**JSF**) для создания пользовательского интерфейса.

Этот класс должен быть помечен `jakarta.servlet.annotation.MultipartConfig`. Это приводит к тому, что контейнер сервлетов Jakarta, в котором выполняется реализация `Jakarta Server Faces`, правильно обрабатывает данные составной формы.

### 3. Сервлет, перенаправляющий запрос странице `index.jsp`, только если в нём присутствует идентификатор пользователя (cookie `userId`).

```java
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
  
public class MyServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request
                       HttpServletResponse response) 
                throws ServletException, IOException {

      Cookie[] cookies = request.getCookies(); 
      boolean containsUserId = false;
      if (cookies != null) {
          for (Cookie cookie: cookies) {
              if (cookie.getName().equals("userId")) {
                  containsUserId = true;
                  break;
              }
          }
      }

      if (containsUserId) {
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/index.html");
        requestDispatcher.forward(request, response);
      }
  }
}
```

## Вариант 02468

### 1. В каком порядке вызываются фильтры в `FilterChain`?

Фильтров может быть несколько, и они последовательно обрабатывают запрос (и ответ). Они объединены в так называемую цепочку — и для них даже есть специальный класс `FilterChain`.

Фильтры будут вызваться в том порядке, в котором они определены в `web.xml`.

После обработка запроса в методе `doFilter()` нужно вызвать метод `doFilter()` следующего фильтра в цепочке. Пример:

```java
public class MyFilter implements Filter {
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws Exception {
      PrintWriter out = resp.getWriter();
      out.print("Дописываем что-то перед телом ответа");

      chain.doFilter(req, resp); //вызываем следующий фильтр в цепочке

      out.print("Дописываем что-то после тела ответа");
    }
}
```

То есть в `FilterChain` фильтры будут выполняться в том порядке, в котором их вызывают в теле предыдущего фильтра через `chain.doFilter()`.

### 2. Где лучше хранить содержимое корзины покупателя — в атрибутах запроса, сессии или контекста, и почему?

Корзина пользователя должна существовать, пока пользователь взаимодействует с приложением. Он может закрыть бразуер, открыть его снова, и корзина не должна очиститься.

#### Атрибуты запроса

Атрибуты запроса сохраняются только в рамках одного запроса, что, очевидно, не подходит для реализации корзины.

#### Контекст

В `ServletContext` помещаются данные, которые касаются всего приложения — количество одновременных сеансов, ссылки на базу данных или сторонний веб-сервис и т. д., что также является неподходящим местом для корзины пользователя.

#### Сессия

Хранение корзины в сессии является наиболее подходящим местом для хранения корзины пользователя, так как позволяет сохранять некоторую информацию на время сеанса.

Для передачи идентификатора сессии наиболее популярными являются такие способы, как использование пользовательских cookies, скрытые поля формы и передача непосредственно в адресе запроса. 

В момент, когда сеанс установлен, сервлет-контейнер создаёт пользовательский `cookie` с именем `JSESSIONID`, значение которого содержит уникальный идентификатор сессии.

Таким образом, в сессии удобнее всего хранить и легко изменять содержимое корзины покупателя на все время сеанса.

### 3. Конфигурация, формирующая 2 экземпляра сервлета `com.example.MyServlet`, обрабатывающих запросы к ресурсам `/page1.do` и `/page2.do` соответственно.

Файл `webapp/WEB-INF/web.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE web-app>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
  <servlet>
    <servlet-name>MyServlet1</servlet-name>
    <servlet-class>com.example.MyServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>MyServlet1</servlet-name>
    <url-pattern>/page1.do</url-pattern>
  </servlet-mapping>

  <servlet>
    <servlet-name>MyServlet2</servlet-name>
    <servlet-class>com.example.MyServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>MyServlet2</servlet-name>
    <url-pattern>/page2.do</url-pattern>
  </servlet-mapping>
</web-app>
```

*Однако, удачи правильно написать* `http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd` *на микрокре* 😃
