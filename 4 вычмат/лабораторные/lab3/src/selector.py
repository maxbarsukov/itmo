import colors as color
from methods.rectangles import Rectangles
from methods.trapezoid import Trapezoid
from methods.simpson import Simpson


class Input:
    type_equation = 0
    type_method = 0
    a = 0
    b = 0
    accuracy = 0

    def __init__(self, type_equation):
        self.type_equation = type_equation
        self.choose_boundaries()
        self.choose_accuracy()
        self.calculation()

    def choose_boundaries(self):
        print(color.UNDERLINE + color.YELLOW, "Выбор границы интегрирования.", color.END)
        while True:
            try:
                print(color.BOLD + color.YELLOW, "Формат ввода границ, например: -10 10", color.END)
                segment = list(input("Введите границы: ").split())
                if len(segment) == 2 and float(segment[0].strip()) < float(segment[1].strip()):
                    self.a = float(segment[0].strip())
                    self.b = float(segment[1].strip())
                    break
                else:
                    get_ready_answer(1)
                    continue
            except TypeError:
                get_ready_answer(1)
                continue

    def choose_accuracy(self):
        print(color.UNDERLINE + color.YELLOW, "Выбор точности вычисления.", color.END)
        while True:
            try:
                print(color.BOLD + color.YELLOW, "Введите кол-во знаков после запятой, для вычисления.", color.END)
                accuracy = float(input("Количество знаков: ").strip())
                if accuracy % 1 != 0 or accuracy <= 0:
                    get_ready_answer(2)
                    continue
                else:
                    self.accuracy = accuracy
                    break
            except TypeError:
                get_ready_answer(2)
                continue

    def calculation(self):
        while True:
            try:
                print(color.BOLD + color.YELLOW, "Выберите метод решения:", color.END)
                while True:
                    print('\t', "1. Метод прямоугольников (левые, средние, правые)", '\n',
                          '\t', "2. Метод трапеций", '\n',
                          '\t', "3. Метод Симпсона")
                    self.type_method = int(input("Тип метода (цифра): ").strip())
                    if self.type_method == 1:
                        calculator = Rectangles(self.a, self.b, self.accuracy, self.type_equation)
                        calculator.calc()
                        break
                    elif self.type_method == 2:
                        calculator = Trapezoid(self.a, self.b, self.accuracy, self.type_equation)
                        calculator.calc()
                        break
                    elif self.type_method == 3:
                        calculator = Simpson(self.a, self.b, self.accuracy, self.type_equation)
                        calculator.calc()
                        break
                    else:
                        get_ready_answer(3)
                        continue

                del calculator
                break
            except TypeError:
                get_ready_answer(4)
                break
            except ValueError:
                break


def get_ready_answer(type_answer):
    answers = {
        1: color.BOLD + color.RED + "Неправильный ввод границ!" + color.END,
        2: color.BOLD + color.RED + "Неправильный ввод точности!" + color.END,
        3: color.BOLD + color.RED + "Неправильный ввод!" + color.END,
        4: color.BOLD + color.RED + "Интеграл расходится на выбранном промежутке!" + color.END
    }
    print(answers.get(type_answer, color.BOLD + color.RED + "Неправильный выбор готового ответа!" + color.END))
