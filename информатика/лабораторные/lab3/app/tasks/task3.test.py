import unittest
import task3

class TestTask3(unittest.TestCase):
    def test_no_vowels(self):
        data = 'пппп ннн цц прпрпрпр звпркнмь'
        result = []
        self.assertEqual(result, task3.solve(data))
    
    def test_one_vowels(self):
        data = 'пап пуп уп у пп'
        result = ['у', 'уп', 'пап', 'пуп']
        self.assertEqual(result, task3.solve(data))

    def test_example(self):
        data = 'Классное слово – обороноспособность, которое должно идти после слов: трава и молоко.'
        result = ['и', 'идти', 'слов', 'слово', 'трава', 'должно', 'молоко', 'обороноспособность']
        self.assertEqual(result, task3.solve(data))

    def test_duplicates(self):
        data = 'пап пап пап пап пап'
        result = ['пап']
        self.assertEqual(result, task3.solve(data))

    def test_only_vowels(self):
        data = 'а у о уу оу уоу'
        result = ['а', 'о', 'у', 'уу']
        self.assertEqual(result, task3.solve(data))

    def test_some_words(self):
        data = 'окно трава молоко'
        result = ['окно', 'трава', 'молоко']
        self.assertEqual(result, task3.solve(data))

    def test_sentence(self):
        data = 'съешь? ещё, этих!!! мягких... французских<> булок</title>, да. выпей чаю'
        result = ['да', 'съешь']
        self.assertEqual(result, task3.solve(data))

if __name__ == '__main__':
    unittest.main()
