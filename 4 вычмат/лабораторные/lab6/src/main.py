from math import sin, cos, exp, inf
import matplotlib.pyplot as plt
import os, signal

from methods import improved_euler_method, fourth_order_runge_kutta_method, milne_method


MAX_ITERS = 20


def my_input(str):
    s = input(str)
    if (s == 'q'):
        print("! Выход из программы.")
        thanks()
        os.kill(os.getpid(), signal.SIGINT)
    else:
        return s


def select_odu():
    print('ОДУ: ')
    print('1. y + (1 + x)*y^2')
    print('2. x + y')
    print('3. sin(x) - y')
    print('4. y / x')
    print('5. e^x\n')

    while True:
        try:
            input_func = int(my_input('> Выберите ОДУ [1/2/3/4/5]: '))
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
                f = lambda x, y: y / x
                exact_y = lambda x, x0, y0: (x*y0) / x0
                break
            elif input_func == 5:
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


def solve(f, x0, xn, n, y0, exact_y, eps):
    print()
    methods = [("Усовершенствованный метод Эйлера", improved_euler_method),
               ("Метод Рунге-Кутта 4-го порядка", fourth_order_runge_kutta_method),
               ("Метод Милна", milne_method)]

    for name, method in methods:
        ni = n
        print(name + ":\n")

        try:
            iters = 0

            xs = [x0 + i * ((xn - x0) / ni) for i in range(ni)]
            ys = method(f, xs, y0, eps)
            inaccuracy = inf

            while inaccuracy > eps:
                if (iters >= MAX_ITERS):
                    print(f"! Не удалось увеличить точность. Произведено {iters} итераций.")
                    break

                iters += 1
                ni *= 2
                xs = [x0 + i * (xn - x0) / ni for i in range(ni)]
                new_ys = method(f, xs, y0, eps)

                if method is milne_method:
                    inaccuracy = max([abs(exact_y(x, x0, y0) - y) for x, y in zip(xs, new_ys)])
                else:
                    p = 4 if method is fourth_order_runge_kutta_method else 2
                    coef = 2**p - 1
                    inaccuracy = abs(new_ys[-1] - ys[-1]) / coef

                ys = new_ys.copy()

            #     print("\tDEBUG:", str(iters) + ")", inaccuracy, (">=" if inaccuracy >= eps else "<"), eps)
            # print()

            if (iters != 1):
                print(f"Для точности eps={eps} интервал был разбит на n={ni} частей с шагом h={round((xn - x0) / ni, 6)} за {iters} итераций.\n")
            else:
                print(f"Для точности eps={eps} интервал был разбит на n={ni} частей с шагом h={round((xn - x0) / ni, 6)}.\n")

            if (len(xs) < 100):
                print("y:\t[", *map(lambda x: round(x, 5), ys), "]")
                print("y_точн:\t[", *map(lambda x: round(exact_y(x, x0, y0), 5), xs), "]")
            elif (my_input("Показать все значения y (количество > 100)? [y/n]: ") == 'y'):
                print("y:\t[", *map(lambda x: round(x, 5), ys), "]")
                print("y_точн:\t[", *map(lambda x: round(exact_y(x, x0, y0), 5), xs), "]")

            print()
            if method is milne_method:
                print(f"Погрешность (max|y_iточн - y_i|): {inaccuracy}")
            else:
                print(f"Погрешность (по правилу Рунге): {inaccuracy}")

            print("\nПостроение графика ...")
            print('-' * 30 + '\n')

            plt.title(name)
            draw_plot(xs[0], xs[-1], exact_y, x0, y0)
            for i in range(len(xs)):
                plt.scatter(xs[i], ys[i], c='r')

            plt.xlabel("X")
            plt.ylabel("Y")
            plt.show()
        except OverflowError:
            print('-' * 30 + '\n')
            print("! Невозможно вычислить. Число/точность слишком большое.")


def main():
    f, exact_y = select_odu()

    while True:
        try:
            x0 = float(my_input('> Введите первый элемент интервала x0: '))
            xn = float(my_input('> Введите последний элемент интервала xn: '))
            n = int(my_input('> Введите количество элементов в интервале n: '))

            y0 = float(my_input('> Введите y0: '))
            eps = float(my_input('> Введите точность eps: '))

            if xn <= x0:
                print('! xn должен быть больше x0. Введите еще раз.')
            elif n <= 1:
                print('! Количество элементов n должно быть > 1. Введите еще раз.')
            else:
                break
        except:
            print("! Некорректный ввод. Попробуйте еще раз\n")

    solve(f, x0, xn, n, y0, exact_y, eps)


def thanks():
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

if __name__ == "__main__":
    main()
    thanks()
