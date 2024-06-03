from constants import A, B, E
from derivatives import F_DERIVATIES

from methods.newton import solve as solve_via_newton_method
from methods.golden_ratio import solve as solve_via_golden_ratio
from methods.half_division import solve as solve_via_half_division

METHODS = [
    dict(
        name='Вычисление по методу Ньютона',
        func=solve_via_newton_method,
    ),
    dict(
        name='Вычисление по методу половинного деления',
        func=solve_via_half_division
    ),
    dict(
        name='Вычисление по методу золотого сечения',
        func=solve_via_golden_ratio,
    )
]


def main():
    for method in METHODS:
        print(f'===========\n\n{method["name"]}:\n')

        solve = method['func']
        x_m, y_m = solve(F_DERIVATIES, A, B, E)

        print(f'x_m = {x_m}')
        print(f'y_m = f(x_m) = {y_m}')
        print()

if __name__ == '__main__':
    main()
