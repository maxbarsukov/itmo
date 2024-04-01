import colors as color
import selector as select

if __name__ == "__main__":
    print(color.BOLD + color.RED + "Решатель интегралов. " + color.CYAN + "Барсуков М.А." + color.END)

    while True:
        try:
            print('\n', color.UNDERLINE + color.YELLOW + "Выберите функцию:" + color.END)
            print(color.GREEN,
                  '\t', "1: x^2 - 3", '\n',
                  '\t', "2: 5/x - 2x", '\n',
                  '\t', "3: e^(2x) - 2", '\n',
                  '\t', "4: 2x^3 - 3x^2 + 5x - 9", '\n',
                  '\t', "5: Выход", color.END)

            choice = int(input("Ввод: ").strip())
            if choice in [1, 2, 3, 4]:
                select.Input(choice)
                continue
            elif choice == 5:
                print(color.BOLD + color.PURPLE, 'Спасибо за использование программы!', color.END)
                break
            else:
                print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
                continue

        except TypeError:
            print(color.BOLD + color.RED, "Неправильный ввод!", color.END)
            continue

        except ValueError:
            continue
