from dataclasses import dataclass


@dataclass
class Result:
    root: float
    function_value_at_root: float
    iterations: int
    decimal_places: int

    def __str__(self):
        return 'Результат:\n' \
               f'Найденный корень уравнения: {round(self.root, self.decimal_places)}\n' \
               f'Значение функции в корне: {self.function_value_at_root}\n' \
               f'Число итераций: {self.iterations}'
