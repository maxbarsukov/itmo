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

functions = [f1, f2, f3, f4, f5, f6, f7]

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
    runge_coef = {"rectangle_left": 2, "rectangle_right": 2, "rectangle_middle": 2, "trapezoid": 2, "simpson": 15}
    coef = runge_coef[method]

    result = methods[method](func, a, b, n)
    error = math.inf

    while error > epsilon:
        n *= 2
        new_result = methods[method](func, a, b, n)
        error = abs(new_result - result) / coef
        result = new_result

    return result, n


def check_convergence(func, a, b):
    if func == f4 and ((a >= -math.inf and b <= 0) or (a >= 0 and b <= math.inf)):
        return True
    elif func == f5 and ((a >= -math.inf and b <= 0) or (a >= 0 and b <= math.inf)):
        return False
    elif func == f6 and (a >= 0 and b <= math.inf):
        return True
    elif func == f1 or func == f2 or func == f3 or func == f7:
        return True
    else:
        return False


def check_discontinuity(func, a, b):
    try:
        func(a)
        func(b)
        return False
    except (ZeroDivisionError, OverflowError, ValueError):
        return True


def compute_integral_modified(func, a, b, epsilon, method):
    if check_discontinuity(func, a, b):
        print("Интеграл не существует: функция имеет разрыв.")
        return None, None

    if not check_convergence(func, a, b):
        print("Интеграл не существует: интеграл не сходится.")
        return None, None

    return compute_integral(func, a, b, epsilon, method)


if __name__ == "__main__":
    print("Выберите функцию:")
    print("1. x^2")
    print("2. sin(x)")
    print("3. e^x")
    print("4. 1/x^2")
    print("5. 1/x")
    print("6. 1/sqrt(x)")
    print("7. -3x^3 - 5x^2 + 4x - 2")

    func = functions[int(input("Ваш выбор: ")) - 1]

    a = float(input("Введите начальный предел интегрирования: "))
    b = float(input("Введите конечный предел интегрирования: "))

    print("Выберите метод интегрирования:")
    for i, method in enumerate(methods, 1):
        print(f"{i}. {method}")

    method = list(methods.keys())[int(input("Ваш выбор: ")) - 1]
    epsilon = float(input("Введите требуемую точность вычислений: "))

    result, n = compute_integral_modified(func, a, b, epsilon, method)

    if result is not None and n is not None:
        print(f"Значение интеграла: {result}")
        print(f"Число разбиений интервала интегрирования для достижения требуемой точности: {n}")
