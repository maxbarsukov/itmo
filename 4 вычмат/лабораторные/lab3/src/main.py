import math

def f1(x):
    return x**2

def f2(x):
    return math.sin(x)

def f3(x):
    return math.exp(x)

def f4(x):
    return 1 / x**2

def f5(x):
    return 1 / x

def f6(x):
    return 1 / math.sqrt(x)

def f7(x):
    return -3*x**3 - 5*x**2 + 4*x - 2

def f8(x):
    return 10

functions = [f1, f2, f3, f4, f5, f6, f7, f8]

def rectangle_rule(func, a, b, n, mode="middle"):
    h = (b - a) / n
    result = 0

    if mode == "left":
        for i in range(n):
            result += func(a + i * h)

    elif mode == "right":
        for i in range(1, n + 1):
            result += func(a + i * h)

    else:
        for i in range(n):
            result += func(a + (i + 0.5) * h)

    result *= h
    return result


def trapezoid_rule(func, a, b, n):
    h = (b - a) / n
    result = (func(a) + func(b)) / 2

    for i in range(1, n):
        result += func(a + i * h)

    result *= h
    return result


def simpson_rule(func, a, b, n):
    h = (b - a) / n
    result = func(a) + func(b)

    for i in range(1, n):
        coef = 3 + (-1)**(i + 1)
        result += coef * func(a + i * h)

    result *= h / 3
    return result


methods = {
    "rectangle_left": lambda func, a, b, n: rectangle_rule(func, a, b, n, mode="left"),
    "rectangle_right": lambda func, a, b, n: rectangle_rule(func, a, b, n, mode="right"),
    "rectangle_middle": rectangle_rule,
    "trapezoid": trapezoid_rule,
    "simpson": simpson_rule
}

def compute_integral(func, a, b, epsilon, method):
    n = 4
    runge_coef = {"rectangle_left": 3, "rectangle_right": 3, "rectangle_middle": 3, "trapezoid": 3, "simpson": 15}
    coef = runge_coef[method]

    result = methods[method](func, a, b, n)
    error = math.inf

    while error > epsilon:
        n *= 2
        new_result = methods[method](func, a, b, n)
        error = abs(new_result - result) / coef

        result = new_result

    return result, n


def get_discontinuity_points(func, a, b, n):
    breakpoints = []

    try:
        func(a)
    except (ZeroDivisionError, OverflowError, ValueError):
        breakpoints.append(a)

    try:
        func(b)
    except (ZeroDivisionError, OverflowError, ValueError):
        breakpoints.append(b)

    try:
        func((a + b) / 2)
    except (ZeroDivisionError, OverflowError, ValueError):
        breakpoints.append((a + b) / 2)

    h = (b - a) / n
    for i in range(n):
        point = a + i * h
        try:
            func(a + i * h)
        except (ZeroDivisionError, OverflowError, ValueError):
            breakpoints.append(point)

    return list(set(breakpoints))


def try_to_compute(func, x):
    try:
        return func(x)
    except Exception:
        return None


if __name__ == "__main__":
    while (True):
        print("Выберите функцию:")
        print("1. x^2")
        print("2. sin(x)")
        print("3. e^x")
        print("4. 1/x^2")
        print("5. 1/x")
        print("6. 1/sqrt(x)")
        print("7. -3x^3 - 5x^2 + 4x - 2")
        print("8. 10")

        choosen_f = int(input("Ваш выбор: ")) - 1
        func = functions[choosen_f]

        if (choosen_f not in [0, 2, 3, 4, 5, 6, 7]):
            print("Пожалуйста, выберите корректный номер функции!\n")
            continue

        while (True):
            a = float(input("Введите начальный предел интегрирования: "))
            b = float(input("Введите конечный предел интегрирования: "))

            if (a >= b):
                print(f'a = {a} >= b = {b}. Пожалуйста, введите a < b')
            else:
                break

        breakpoints = get_discontinuity_points(func, a, b, math.ceil(b - a) * 1_000)

        # Если разрыв: установить сходимость
        if len(breakpoints) != 0:
            print(f"! Обнаружен точка разрыва: функция имеет разрыв или не существует в точке x = {breakpoints[0]}.")

            eps = 0.00001
            converges = True
            for bp in breakpoints:
                y1 = try_to_compute(func, bp - eps)
                y2 = try_to_compute(func, bp + eps)
                if y1 is not None and y2 is not None and abs(y1 - y2) > eps or (y1 == y2 and y1 is not None):
                    converges = False
                    break

            if not converges:
                # расходящийся => выводить сообщение: «Интеграл не существует»
                print('- Интеграл не существует: интеграл не сходится.')
            else:
                # сходящийся => реализовать в программе вычисление несобственных интегралов 2 рода
                print('+ Интеграл сходится.')
                print("Выберите метод интегрирования:")
                for i, method in enumerate(methods, 1):
                    print(f"{i}. {method}")

                method = list(methods.keys())[int(input("Ваш выбор: ")) - 1]
                epsilon = float(input("Введите требуемую точность вычислений: "))

                if len(breakpoints) == 1:
                    if a in breakpoints:
                        a += eps
                    elif b in breakpoints:
                        b -= eps
                else:
                    res = 0
                    n = 0
                    if not (try_to_compute(func, a) is None or try_to_compute(func, breakpoints[0] - eps) is None):
                        res += compute_integral(func, a, breakpoints[0] - eps, epsilon, method)[0]
                        n += compute_integral(func, a, breakpoints[0] - eps, epsilon, method)[1]

                    if not (try_to_compute(func, b) is None or try_to_compute(func, breakpoints[0] + eps) is None):
                        res += compute_integral(func, breakpoints[0] + eps, b, epsilon, method)[0]
                        n += compute_integral(func, breakpoints[0] + eps, b, epsilon, method)[1]

                    if (len(breakpoints) > 1):
                        for bi in range(len(breakpoints) - 1):
                            b_cur = breakpoints[bi]
                            b_next = breakpoints[bi + 1]

                            if not (try_to_compute(func, b_cur) is None or try_to_compute(func, b_next) is None):
                                res += compute_integral(func, b_cur + eps, b_next - eps, epsilon, method)[0]
                                n += compute_integral(func, b_cur + eps, b_next - eps, epsilon, method)[1]

                    print(f"Значение интеграла: {res}")
                    print(f"Число разбиений интервала интегрирования для достижения требуемой точности: {n}")

                if not breakpoints or a - eps in breakpoints or b + eps in breakpoints:
                    result, n = compute_integral(func, a, b, epsilon, method)
                    if result is not None and n is not None:
                        print(f"Значение интеграла: {result}")
                        print(f"Число разбиений интервала интегрирования для достижения требуемой точности: {n}")

        else:
            # Если нет разрыва: просто вычисляем
            print("Выберите метод интегрирования:")
            for i, method in enumerate(methods, 1):
                print(f"{i}. {method}")

            method = list(methods.keys())[int(input("Ваш выбор: ")) - 1]
            epsilon = float(input("Введите требуемую точность вычислений: "))

            result, n = compute_integral(func, a, b, epsilon, method)

            if result is not None and n is not None:
                print(f"Значение интеграла: {result}")
                print(f"Число разбиений интервала интегрирования для достижения требуемой точности: {n}")


        if (input('Еще раз? [y/n]: ') == 'n'):
            break
