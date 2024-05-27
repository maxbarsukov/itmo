def calc_det2(A):
    return A[0][0] * A[1][1] - A[0][1] * A[1][0]

def solve2(A, B):
    n = 2
    det = calc_det2(A)
    det1 = calc_det2([[B[r], A[r][1]] for r in range(n)])
    det2 = calc_det2([[A[r][0], B[r]] for r in range(n)])
    x1 = det1 / det
    x2 = det2 / det
    return x1, x2


def calc_det3(A):
    pos = A[0][0] * A[1][1] * A[2][2] + \
          A[0][1] * A[1][2] * A[2][0] + \
          A[0][2] * A[1][0] * A[2][1]
    neg = A[0][2] * A[1][1] * A[2][0] + \
          A[0][1] * A[1][0] * A[2][2] + \
          A[0][0] * A[1][2] * A[2][1]
    return pos - neg

def solve3(A, B):
    n = 3
    det = calc_det3(A)
    det1 = calc_det3([[B[r], A[r][1], A[r][2]] for r in range(n)])
    det2 = calc_det3([[A[r][0], B[r], A[r][2]] for r in range(n)])
    det3 = calc_det3([[A[r][0], A[r][1], B[r]] for r in range(n)])
    x1 = det1 / det
    x2 = det2 / det
    x3 = det3 / det
    return x1, x2, x3


def calc_det4(A):
    n = 4
    sign = 1
    r = 0
    res = 0
    for c in range(n):
        A_ = [[A[r_][c_] for c_ in range(n) if c_ != c]
              for r_ in range(n) if r_ != r]
        res += sign * A[r][c] * calc_det3(A_)
        sign *= -1
    return res

def solve4(A, B):
    n = 4
    det = calc_det4(A)
    det1 = calc_det4([[B[r], A[r][1], A[r][2], A[r][3]] for r in range(n)])
    det2 = calc_det4([[A[r][0], B[r], A[r][2], A[r][3]] for r in range(n)])
    det3 = calc_det4([[A[r][0], A[r][1], B[r], A[r][3]] for r in range(n)])
    det4 = calc_det4([[A[r][0], A[r][1], A[r][2], B[r]] for r in range(n)])
    x1 = det1 / det
    x2 = det2 / det
    x3 = det3 / det
    x4 = det4 / det
    return x1, x2, x3, x4


def solve_sle(A, B, n):
    if n == 2:
        return solve2(A, B)
    if n == 3:
        return solve3(A, B)
    if n == 4:
        return solve4(A, B)
    print(f"! n should be 2/3/4, {n} got")
    return None
