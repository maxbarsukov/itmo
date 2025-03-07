from itertools import combinations, groupby


def scramble(message, x, y):
    A = list(map(int, list(message)))
    B = []

    min_xy = min(x, y)
    max_xy = max(x, y)

    for i in range(len(A)):
        if i < min_xy:
            # При i < min(x, y): B[i] = A[i]
            B.append(A[i])
        elif min_xy <= i < max_xy:
            # При min(x, y) <= i < max(x, y): B[i] = A[i] ⊕ B[i - x]
            B.append(A[i] ^ B[i - x])
        else:
            # При i >= max(x, y): B[i] = A[i] ⊕ B[i - x] ⊕ B[i - y]
            B.append(A[i] ^ B[i - x] ^ B[i - y])
    return ''.join(map(str, B))


def max_repeated_length(message):
    return max(len(list(g)) for _, g in groupby(message))


def find_optimal_xy(message, xy_range):
    optimal_x, optimal_y = None, None
    min_max_length = float('inf')

    for x, y in combinations(xy_range, 2):
        scrambled_message = scramble(message, x, y)
        current_max_length = max_repeated_length(scrambled_message)

        if current_max_length < min_max_length:
            min_max_length = current_max_length
            optimal_x, optimal_y = x, y

    return optimal_x, optimal_y, min_max_length


message = "110000011100110011000000"

xy_range = range(2, 11)
optimal_x, optimal_y, min_max_length = find_optimal_xy(message, xy_range)

print("Исходное сообщение:", message)
print("\nОптимальные параметры:")
print(f"x = {optimal_x}, y = {optimal_y}")
print(f"Минимальная максимальная длина постоянной составляющей: {min_max_length}")

scrambled_optimal = scramble(message, optimal_x, optimal_y)
print("\nСкремблированное сообщение с оптимальными параметрами:")
print(scrambled_optimal)
