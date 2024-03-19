from typing import Tuple

from dto.equation import Equation
from dto.result import Result


class Method:
    name = None

    def __init__(self, equation: Equation, left: float, right: float,
                 epsilon: float, decimal_places: int, log: bool):
        self.log = log
        self.decimal_places = decimal_places
        self.epsilon = epsilon
        self.right = right
        self.left = left
        self.equation = equation

    def solve(self) -> Result:
        pass

    def check(self) -> Tuple[bool, str]:
        return True, ''
