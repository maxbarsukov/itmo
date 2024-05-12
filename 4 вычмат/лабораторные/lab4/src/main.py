import inspect
from math import sqrt, exp, log
import sys
import matplotlib.pyplot as plt

from matrix import solve_sle


def linear_approximation(xs, ys, n):
    sx = sum(xs)
    sxx = sum(x ** 2 for x in xs)
    sy = sum(ys)
    sxy = sum(x * y for x, y in zip(xs, ys))

    a, b = solve_sle(
        [
            [n, sx],
            [sx, sxx]
        ],
        [sy, sxy], 2)
    return lambda xi: a + b * xi, a, b


def quadratic_approximation(xs, ys, n):
    sx = sum(xs)
    sxx = sum(x ** 2 for x in xs)
    sxxx = sum(x ** 3 for x in xs)
    sxxxx = sum(x ** 4 for x in xs)
    sy = sum(ys)
    sxy = sum(x * y for x, y in zip(xs, ys))
    sxxy = sum(x * x * y for x, y in zip(xs, ys))
    a, b, c = solve_sle(
        [
            [n, sx, sxx],
            [sx, sxx, sxxx],
            [sxx, sxxx, sxxxx]
        ],
        [sy, sxy, sxxy],
        3
    )
    return lambda xi: a + b * xi + c * xi ** 2, a, b, c


def cubic_approximation(xs, ys, n):
    sx = sum(xs)
    sxx = sum(x ** 2 for x in xs)
    sxxx = sum(x ** 3 for x in xs)
    sxxxx = sum(x ** 4 for x in xs)
    sxxxxx = sum(x ** 5 for x in xs)
    sxxxxxx = sum(x ** 6 for x in xs)
    sy = sum(ys)
    sxy = sum(x * y for x, y in zip(xs, ys))
    sxxy = sum(x * x * y for x, y in zip(xs, ys))
    sxxxy = sum(x * x * x * y for x, y in zip(xs, ys))
    a, b, c, d = solve_sle(
        [
            [n, sx, sxx, sxxx],
            [sx, sxx, sxxx, sxxxx],
            [sxx, sxxx, sxxxx, sxxxxx],
            [sxxx, sxxxx, sxxxxx, sxxxxxx]
        ],
        [sy, sxy, sxxy, sxxxy],
        4
    )
    return lambda xi: a + b * xi + c * xi ** 2 + d * xi ** 3, a, b, c, d


def exponential_approximation(xs, ys, n):
    ys_ = list(map(log, ys))
    _, a_, b_ = linear_approximation(xs, ys_, n)
    a = exp(a_)
    b = b_
    return lambda xi: a * exp(b * xi), a, b


def logarithmic_approximation(xs, ys, n):
    xs_ = list(map(log, xs))
    _, a_, b_ = linear_approximation(xs_, ys, n)
    a = a_
    b = b_
    return lambda xi: a + b * log(xi), a, b


def power_approximation(xs, ys, n):
    xs_ = list(map(log, xs))
    ys_ = list(map(log, ys))
    _, a_, b_ = linear_approximation(xs_, ys_, n)
    a = exp(a_)
    b = b_
    return lambda xi: a * xi ** b, a, b


def compute_pearson_correlation(x, y, n):
    av_x = sum(x) / n
    av_y = sum(y) / n
    return sum((x - av_x) * (y - av_y) for x, y in zip(x, y)) / \
        sqrt(sum((x - av_x) ** 2 for x in x) * sum((y - av_y) ** 2 for y in y))


def compute_mean_squared_error(x, y, fi, n):
    return sqrt(sum(((fi(xi) - yi) ** 2 for xi, yi in zip(x, y))) / n)


def compute_measure_of_deviation(x, y, fi, n):
    epss = [fi(xi) - yi for xi, yi in zip(x, y)]
    return sum((eps ** 2 for eps in epss))


def compute_coefficient_of_determination(xs, ys, fi, n):
    av_fi = sum(fi(x) for x in xs) / n
    return 1 - sum((y - fi(x)) ** 2 for x, y in zip(xs, ys)) / sum((y - av_fi) ** 2 for y in ys)


def get_str_content_of_func(func):
    str_func = inspect.getsourcelines(func)[0][0]
    return str_func.split('lambda xi: ')[-1].split(',')[0].strip()


def draw_plot(x, y):
    plt.scatter(x, y, label="Вводные точки")
    plt.legend()
    plt.xlabel("x")
    plt.ylabel("y")
    plt.title("Приближение функции различными методами")
    plt.show()


def draw_func(func, name, x, dx=0.001):
    a = x[0]
    b = x[-1]
    xs, ys = [], []
    a -= 0.1
    b += 0.1
    x = a
    while x <= b:
        xs.append(x)
        ys.append(func(x))
        x += dx
    plt.plot(xs, ys, label=name)

def get_coeffs_str(coeffs):
    if len(coeffs) == 2:
        return '(a, b)'
    if len(coeffs) == 3:
        return '(a, b, c)'
    if len(coeffs) == 4:
        return '(a, b, c, d)'
    return '(a, b, c, d, e)'


def run(functions, x, y, n):
    best_mse = float("inf")
    best_func = None

    for approximation, name in functions:
        try:
            fi, *coeffs = approximation(x, y, n)

            s = compute_measure_of_deviation(x, y, fi, n)
            mse = compute_mean_squared_error(x, y, fi, n)
            r2 = compute_coefficient_of_determination(x, y, fi, n)

            if mse < best_mse:
                best_mse = mse
                best_func = name

            draw_func(fi, name, x)

            print(f"{name} функция:")
            print(f"*  Функция: f(x) =", get_str_content_of_func(fi))
            print(f"*  Коэффициенты {get_coeffs_str(coeffs)}: {list(map(lambda cf: round(cf, 4), coeffs))}")
            print(f"*  Среднеквадратичное отклонение: σ = {mse:.5f}")
            print(f"*  Коэффициент детерминации: R^2 = {r2:.5f}")
            print(f"*  Мера отклонения: S = {s:.5f}")
            if approximation == linear_approximation:
                correlation = compute_pearson_correlation(x, y, n)
                print(f"*  Коэффициент корреляции Пирсона: r = {correlation}")

        except Exception as e:
            print(f"Ошибка приближения {name} функции: {e}\n")

        print('\n' + ('-' * 30) + '\n')

    print(f"Лучшая функция приближения: {best_func}")
    draw_plot(x, y)


def read_data_from_file(filename):
    try:
        with open(filename, 'r') as file:
            x = []
            y = []
            for line in file:
                point = line.strip().split()
                if len(point) == 2:
                    x.append(float(point[0]))
                    y.append(float(point[1]))

        return x, y, None
    except IOError as err:
        return None, None, "! Невозможно прочитать файл {0}: {1}".format(filename, err)


def read_data_from_input():
    str = ''
    x = []
    y = []
    while str != 'quit':
        str = input()
        point = str.strip().split()
        if len(point) == 2:
            x.append(float(point[0]))
            y.append(float(point[1]))
        else:
            if str != 'quit':
                print("! Неправильный ввод. Введенные числа не будут использованы.")
    return x, y


def main():
    while True:
        option = input("Напишите 'f' для ввода из файла, 'e' для задания или 't' для ввода с клавиатуры: ")
        if option == 'f':
            while True:
                filename = input("Введите имя файла: ")
                x, y, error = read_data_from_file(filename)
                if error != None:
                    print(error)
                    one_more_time = input("Вы хотите попробовать другое имя файла? [y/n]: ")
                    if one_more_time == 'y':
                        continue
                    else:
                        print('Ввод с клавиатуры:')
                        print('Введите \'q\', чтобы закончить ввод')
                        x, y = read_data_from_input()
                else:
                    break
            n = len(x)
            break
        elif option == 't':
            print("Введите 'quit', чтобы закончить ввод")
            x, y = read_data_from_input()
            n = len(x)
            break
        elif option == 'e':
            h = 0.4
            x0 = 0
            n = 11
            f = lambda xi: 15 * xi / (xi ** 4 + 2)

            x = [round(x0 + i * h, 2) for i in range(n)]
            y = [round(f(x), 2) for x in x]
            break
        else:
            print("! Некорректный ввод. Попробуйте еще раз")


    # Функции для исследования
    if all(map(lambda xi: xi > 0, x)):
        if all(map(lambda yi: yi > 0, y)):
            functions = [
                (linear_approximation,       "Линейная"),
                (quadratic_approximation,    "Полиноминальная 2-й степени"),
                (cubic_approximation,        "Полиноминальная 3-й степени"),
                (exponential_approximation,  "Экспоненциальная"),
                (logarithmic_approximation,  "Логарифмическая"),
                (power_approximation,        "Степенная")
            ]
        else:
            functions = [
                (linear_approximation,       "Линейная"),
                (quadratic_approximation,    "Полиноминальная 2-й степени"),
                (cubic_approximation,        "Полиноминальная 3-й степени"),
                (logarithmic_approximation,  "Логарифмическая"),
            ]
    else:
        if all(map(lambda yi: yi > 0, y)):
            functions = [
                (linear_approximation,       "Линейная"),
                (quadratic_approximation,    "Полиноминальная 2-й степени"),
                (cubic_approximation,        "Полиноминальная 3-й степени"),
                (exponential_approximation,  "Экспоненциальная"),
            ]
        else:
            functions = [
                (linear_approximation,       "Линейная"),
                (quadratic_approximation,    "Полиноминальная 2-й степени"),
                (cubic_approximation,        "Полиноминальная 3-й степени"),
            ]

    with open('out.txt', 'w') as output:
        option = input("Вывод в файл 'f' или в терминал 't'? [f/t] ")
        if option == 'f':
            print("Выбран вариант вывода в файл 'out.txt'")
            sys.stdout = output
        else:
            print("Выбран вариант вывода в терминал.")
            print('\n' + ('-' * 30) + '\n')

        run(functions, x, y, n)


if __name__ == "__main__":
    main()
    print("\nСпасибо за использование программы!")
