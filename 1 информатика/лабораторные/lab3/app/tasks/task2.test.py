import unittest
import task2

class TestTask2(unittest.TestCase):
    def test_no_duplicates(self):
        data = 'nothing interesting'
        self.assertEqual(data, task2.solve(data))

    def test_one_duplicate(self):
        data = 'nothing nothing interesting'
        result = 'nothing interesting'
        self.assertEqual(result, task2.solve(data))

    def test_one_duplicate_with_comma(self):
        data = 'nothing, nothing interesting'
        self.assertEqual(data, task2.solve(data))

    def test_many_spaces(self):
        data = 'nothing          nothing interesting'
        result = 'nothing interesting'
        self.assertEqual(result, task2.solve(data))

    def test_suffix_fake_duplicate(self):
        data = 'no nothing'
        self.assertEqual(data, task2.solve(data))

    def test_example(self):
        data = 'Довольно распространённая ошибка ошибка – это лишний повтор повтор слова слова. Смешно, не не правда ли? Не нужно портить хор хоровод.'
        result = 'Довольно распространённая ошибка – это лишний повтор слова. Смешно, не правда ли? Не нужно портить хор хоровод.'
        self.assertEqual(result, task2.solve(data))

    def test_numbers(self):
        data = '11 11 22 22'
        result = '11 22'
        self.assertEqual(result, task2.solve(data))

if __name__ == '__main__':
    unittest.main()
