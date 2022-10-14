import re

def solve(string):
    """ Возвращает количество смайликов вида ;<) в строке
    367081 % 6 = 1 => Глаза: ;
    367081 % 5 = 1 => Нос: <
    367081 % 7 = 1 => Рот: )
    """
    pattern = r';<\)'
    return len(re.findall(pattern, string))
