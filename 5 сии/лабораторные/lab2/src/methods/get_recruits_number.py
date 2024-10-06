from swiplserver import PrologThread


class GetRecruitsNumber:
    def __init__(self, country: str):
        self.country = country

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"recruits({self.country}, Recruits)."

    def success(self, res):
        recruits = res[0]["Recruits"]
        print(f"Количество рекрутов в {self.country}: {recruits}")

    def failure(self, res):
        print(f"Не удалось найти информацию о рекрутах в {self.country}.")
