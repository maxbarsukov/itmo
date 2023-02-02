import re

def solve(string):
    """ 367081 % 5 = 1 =>
    С помощью регулярного выражения найти в тексте слова, в которых встречается
    строго одна гласная буква (встречаться она может несколько раз). Пример таких
    слов: окно, трава, молоко, etc.
    После чего данные слова требуется отсортировать по увеличению длины слова.
    """
    words = re.findall(r'\b\w+\b', string)
    words_with_one_vowel = []
    for word in words:
        for vowel in ['Аа', 'Ее', 'Ёё', 'Ии', 'Оо', 'Уу', 'Ыы', 'Ээ', 'Юю', 'Яя']:
            x = re.sub(f'[{vowel}]', '', 'АЕЁИОУЫЭЮЯаеёиоуыэюя')
            if re.search(rf"^[^{x}]*[{vowel}]+[^{x}]*$", word):
                words_with_one_vowel.append(word)
    
    return sorted(set(words_with_one_vowel), key=lambda x: (len(x), x))
