from math import sin, cos, exp
import matplotlib.pyplot as plt

from methods import improved_euler_method, fourth_order_runge_kutta_method, milne_method


def select_odu():
    print('ОДУ: ')
    print('1. y + (1 + x)*y^2')
    print('2. x + y')
    print('3. sin(x) - y')
    print('4. e^x')

    while True:
        try:
            input_func = int(input('> Выберите ОДУ [1/2/3/4]: '))
            if input_func == 1:
                f = lambda x, y: y + (1 + x) * y ** 2
                # Точные значения находим через https://mathdf.com/dif/ru/#expr=y'%3Dy%2B(1%2Bx)*y%5E2&func=y&arg=x&vals=x_0%3By_0
                exact_y = lambda x, x0, y0: -exp(x) / (x*exp(x) - (x0*exp(x0)*y0 + exp(x0)) / y0)
                break
            elif input_func == 2:
                f = lambda x, y: x + y
                exact_y = lambda x, x0, y0: exp(x - x0) * (y0 + x0 + 1) - x - 1
                break
            elif input_func == 3:
                f = lambda x, y: sin(x) - y
                exact_y = lambda x, x0, y0: (2*exp(x0)* y0-exp(x0)*sin(x0)+exp(x0)*cos(x0)) / (2*exp(x)) + (sin(x)) / 2 - (cos(x)) / 2
                break
            elif input_func == 4:
                f = lambda x, y: exp(x)
                exact_y = lambda x, x0, y0: y0 - exp(x0) + exp(x)
                break
            else:
                print("! Некорректный ввод. Попробуйте еще раз\n")
        except:
            print("! Некорректный ввод. Попробуйте еще раз\n")

    return f, exact_y


def draw_plot(a, b, func, x0, y0, dx=0.01):
    xs, ys = [], []
    a -= dx
    b += dx
    x = a
    while x <= b:
        xs.append(x)
        ys.append(func(x, x0, y0))
        x += dx
    plt.plot(xs, ys, 'g')


def solve(f, xs, x0, y0, exact_y, eps):
    print()
    methods = [("Усовершенствованный метод Эйлера", improved_euler_method),
               ("Метод Рунге-Кутта 4-го порядка", fourth_order_runge_kutta_method),
               ("Метод Милна", milne_method)]

    for name, method in methods:
        print(name + ":")

        ys = method(f, xs, y0, eps)
        print("y:\t[", *map(lambda x: round(x, 5), ys), "]")
        print("y_точн:\t[", *map(lambda x: round(exact_y(x, x0, y0), 5), xs), "]")

        if method is milne_method:
            inaccuracy = max([abs(exact_y(x, x0, y0) - y) for x, y in zip(xs, ys)])
            print(f"Погрешность (max|y_iточн - y_i|): {inaccuracy}")
        else:
            xs2 = []
            for x1, x2 in zip(xs, xs[1:]):
                xs2.extend([x1, (x1 + x2) / 2, x2])
            ys2 = method(f, xs2, y0, eps)

            p = 4 if method is fourth_order_runge_kutta_method else 1
            inaccuracy = max([abs(y1 - y2) / (2 ** p - 1) for y1, y2 in zip(ys, ys2)])
            print(f"Погрешность (по правилу Рунге): {inaccuracy}")

        print('-' * 30 + '\n')

        plt.title(name)
        draw_plot(xs[0], xs[-1], exact_y, x0, y0)

        for i in range(len(xs)):
            plt.scatter(xs[i], ys[i], c='r')

        plt.xlabel("X")
        plt.ylabel("Y")
        plt.show()


def main():
    f, exact_y = select_odu()

    while True:
        try:
            x0 = float(input('> Введите первый элемент интервала x0: '))
            h = float(input('> Введите шаг h: '))
            n = int(input('> Введите количество элементов в интервале n: '))

            xs = [x0 + i * h for i in range(n)]
            y0 = float(input('> Введите y0: '))
            eps = float(input('> Введите точность eps: '))

            if h <= 0:
                print('! Шаг h должен быть положительным. Введите еще раз.')
            elif n <= 1:
                print('! Количество элементов n должно быть > 1. Введите еще раз.')
            else:
                break
        except:
            print("! Некорректный ввод. Попробуйте еще раз\n")

    solve(f, xs, x0, y0, exact_y, eps)


if __name__ == "__main__":
    main()
    print("\nСпасибо за использование программы!\n")
    print("""\
                             ,
       ,-.       _,---._ __  / \\
      /  )    .-'       `./ /   \\
     (  (   ,'            `/    /|
      \  `-"             \'\   / |
       `.              ,  \ \ /  |
        /`.          ,'-`----Y   |
       (            ;        |   '
       |  ,-.    ,-'   comp. |  /
       |  | (   |      math  | /
       )  |  \  `.___________|/
       `--'   `--'
    """)
