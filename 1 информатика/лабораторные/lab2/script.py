# Написать программу на любом языке программирования,
# которая на вход из командной строки получает набор из 7 цифр «0» и «1»,
# записанных подряд, анализирует это сообщение на основе классического
# кода Хэмминга (7,4), а затем выдает правильное сообщение (только
# информационные биты) и указывает бит с ошибкой при его наличии.

def validate_input(string):
    if bool(set(string) - {'1','0'}) or len(string) != 7:
        print('Введённая строка должна быть набором из 7 цифр "0" и "1".')
        exit(1)

def input_to_bits(string):
    return list(map(int, list(string)))

def syndrome(arr):
    s1 = (arr[0] + arr[2] + arr[4] + arr[6]) % 2
    s2 = (arr[1] + arr[2] + arr[5] + arr[6]) % 2
    s3 = (arr[3] + arr[4] + arr[5] + arr[6]) % 2
    return (s1, s2, s3)

def has_error(arr):
    return syndrome(arr) != (0, 0, 0)

def error_index(arr):
    return int(''.join(map(str, syndrome(arr)[::-1])), 2)

def error_symbol(arr):
    return { 1: 'r1', 2: 'r2', 3: 'i1', 4: 'r3', 5: 'i2', 6: 'i3', 7: 'i4' }[error_index(arr)]

def inf_bits(arr):
    return [arr[2], arr[4], arr[5], arr[6]]

def make_result_message(bits):
    return ''.join(map(str, bits))

def fixed_message(arr):
    if not has_error(arr) or error_symbol(arr)[0] == 'r':
        return make_result_message(inf_bits(arr))

    ind = int(error_symbol(arr)[1]) - 1
    result = inf_bits(arr)
    result[ind] = (result[ind] + 1) % 2
    return make_result_message(result)


inp = input('Введите набор из 7 цифр «0» и «1», записанных подряд: ')
validate_input(inp)

bits = input_to_bits(inp)
if has_error(bits):
    print(f'> В сообщении ошибка!\nОшибка в символе {error_symbol(bits)}')
else:
    print('> В сообщении нет ошибок!')

print(f'Правильное сообщение: {fixed_message(bits)}')
