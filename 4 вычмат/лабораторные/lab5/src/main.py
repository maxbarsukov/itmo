from math import sin, sqrt
from solve import solve

def read_data_from_file(filename):
    try:
        with open(filename, 'r') as file:
            xs = []
            ys = []
            x_read = False
            for line in file:
                if not x_read:
                    x_read = True
                    x = float(line.strip())
                else:
                    point = line.strip().split()
                    if len(point) == 2:
                        xs.append(float(point[0]))
                        ys.append(float(point[1]))

        return x, xs, ys, None
    except IOError as err:
        return None, None, None, "! Невозможно прочитать файл {0}: {1}".format(filename, err)


def read_data_from_input():
    x = float(input("Введите точку интерполяции: "))
    str = ''
    xs = []
    ys = []
    print("Введите 'quit', чтобы закончить ввод.")
    print("Введите узлы интерполяции:")
    while str != 'quit':
        str = input()
        point = str.strip().split()
        if len(point) == 2:
            xs.append(float(point[0]))
            ys.append(float(point[1]))
        else:
            if str != 'quit':
                print("! Неправильный ввод. Введенная точка не будет использована.")
    return x, xs, ys


def read_data_from_example():
    x = 0.502
    # x = 0.645
    xs = [0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8]
    ys = [1.5320, 2.5356, 3.5406, 4.5462, 5.5504, 6.5559, 7.5594]
    return x, xs, ys


def read_data_from_function():
    print('Функции: ')
    print('1. 2*x^2 - 5*x')
    print('2. x^5')
    print('3. sin(x)')
    print('4. sqrt(x)')

    while True:
        input_func = int(input('Выберите функцию [1/2/3]: '))
        if input_func == 1:
            f = lambda x: 2*x**2 - 5*x
            break
        elif input_func == 2:
            f = lambda x: x**5
            break
        elif input_func == 3:
            f = lambda x: sin(x)
            break
        elif input_func ==4:
            f = lambda x: sqrt(x)
            break
        else:
            print("! Некорректный ввод. Попробуйте еще раз\n")

    n = int(input("Введите число узлов: "))
    x0 = float(input('Введите x0: '))
    xn = float(input('Введите xn: '))

    h = (xn - x0) / (n - 1)
    xs = [x0 + h * i for i in range(n)]
    ys = list(map(f, xs))

    x = float(input('Введите точку интерполяции: '))
    return x, xs, ys


def main():
    while True:
        while True:
            print("Введите:\n  - 'fi' для ввода из файла;\n  - 'e' для задания вычислительной части;\n  - 't' для ввода с терминала;\n  - 'fu' для задания функции.")
            option = input("Ваш ввод: ")
            if option == 'fi':
                while True:
                    filename = input("Введите имя файла: ")
                    x, xs, ys, error = read_data_from_file(filename)
                    if error != None:
                        print(error)
                        one_more_time = input("Вы хотите попробовать другое имя файла? [y/n]: ")
                        if one_more_time == 'y':
                            continue
                        else:
                            print('Ввод с клавиатуры:')
                            x, xs, ys = read_data_from_input()
                            break
                    else:
                        break
                n = len(xs)
                break
            elif option == 't':
                x, xs, ys = read_data_from_input()
                n = len(xs)
                break
            elif option == 'fu':
                x, xs, ys = read_data_from_function()
                n = len(xs)
                break
            elif option == 'e':
                x, xs, ys = read_data_from_example()
                n = len(xs)
                break
            else:
                print("! Некорректный ввод. Попробуйте еще раз\n")

        if len(set(xs)) != len(xs):
            print('! Узлы интерполции не должны совпадать. Введите еще раз.')
        elif xs != sorted(xs):
            print('! X интерполяции должны быть отсортированы. Введите еще раз.')
        else:
            break

    solve(xs, ys, x, n)


if __name__ == "__main__":
    main()
    print("\nСпасибо за использование программы!\n")
    print("""
      |\      _,,,---,,_
ZZZzz /,`.-'`'    -.  ;-;;,_
     |,4-  ) )-,_. ,\ (  `'-'
    '---''(_/--'  `-'\_)
    """)
