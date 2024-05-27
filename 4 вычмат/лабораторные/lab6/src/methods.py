def improved_euler_method(f, xs, y0, eps):
    ys = [y0]
    h = xs[1] - xs[0]
    for i in range(1, len(xs)):
        y_pred = ys[i-1] + h * f(xs[i-1], ys[i-1])
        y_corr = ys[i-1] + h * f(xs[i], y_pred)
        ys.append(0.5 * (y_pred + y_corr))
    return ys


def fourth_order_runge_kutta_method(f, xs, y0, eps):
    ys = [y0]
    h = xs[1] - xs[0]
    for i in range(1, len(xs)):
        k1 = h * f(xs[i - 1], ys[i - 1])
        k2 = h * f(xs[i - 1] + h / 2, ys[i - 1] + k1 / 2)
        k3 = h * f(xs[i - 1] + h / 2, ys[i - 1] + k2 / 2)
        k4 = h * f(xs[i - 1] + h, ys[i - 1] + k3)
        ys.append(ys[i - 1] + 1 / 6 * (k1 + 2 * k2 + 2 * k3 + k4))
    return ys


def milne_method(f, xs, y0, eps=1e-7):
    ys = fourth_order_runge_kutta_method(f, xs[:4], y0, eps)
    h = xs[1] - xs[0]
    for i in range(4, len(xs)):
        pre_y = ys[i - 4] + 4 * h / 3 * \
                (2 * f(xs[i - 3], ys[i - 3]) -
                 f(xs[i - 2], ys[i - 2]) +
                 2 * f(xs[i - 1], ys[i - 1]))
        cor_y = ys[i - 2] + h / 3 * \
                (f(xs[i - 2], ys[i - 2]) +
                 4 * f(xs[i - 1], ys[i - 1]) +
                 f(xs[i], pre_y))
        while abs(pre_y - cor_y) > eps:
            pre_y = cor_y
            cor_y = ys[i - 2] + h / 3 * \
                    (f(xs[i - 2], ys[i - 2]) +
                     4 * f(xs[i - 1], ys[i - 1]) +
                     f(xs[i], pre_y))
        ys.append(cor_y)
    return ys
