def improved_euler_method(f, xs, y0, eps):
    ys = [y0]
    h = xs[1] - xs[0]
    for i in range(len(xs)):
        y_pred = f(xs[i], ys[i])
        y_corr = f(xs[i] + h, ys[i] + h * y_pred)
        ys.append(ys[i] + 0.5 * h * (y_pred + y_corr))
    return ys


def fourth_order_runge_kutta_method(f, xs, y0, eps):
    ys = [y0]
    h = xs[1] - xs[0]
    for i in range(len(xs)):
        k1 = h * f(xs[i], ys[i])
        k2 = h * f(xs[i] + h / 2, ys[i] + k1 / 2)
        k3 = h * f(xs[i] + h / 2, ys[i] + k2 / 2)
        k4 = h * f(xs[i] + h, ys[i] + k3)
        ys.append(ys[i] + 1 / 6 * (k1 + 2 * k2 + 2 * k3 + k4))
    return ys


def milne_method(f, xs, y0, eps):
    n = len(xs)
    h = xs[1] - xs[0]
    y = [y0]
    for i in range(1, 4):
        k1 = h * f(xs[i - 1], y[i - 1])
        k2 = h * f(xs[i - 1] + h / 2, y[i - 1] + k1 / 2)
        k3 = h * f(xs[i - 1] + h / 2, y[i - 1] + k2 / 2)
        k4 = h * f(xs[i - 1] + h, y[i - 1] + k3)
        y.append(y[i - 1] + (k1 + 2 * k2 + 2 * k3 + k4) / 6)

    for i in range(4, n):
        # Предиктор
        yp = y[i - 4] + 4 * h * (2 * f(xs[i - 3], y[i - 3]) - f(xs[i - 2], y[i - 2]) + 2 * f(xs[i - 1], y[i - 1])) / 3

        # Корректор
        y_next = yp
        while True:
            yc = y[i - 2] + h * (f(xs[i - 2], y[i - 2]) + 4 * f(xs[i - 1], y[i - 1]) + f(xs[i], y_next)) / 3
            if abs(yc - y_next) < eps:
                y_next = yc
                break
            y_next = yc

        y.append(y_next)

    return y
