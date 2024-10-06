from swiplserver import PrologThread


class GetPopulation:
    def __init__(self, province: str, country: str):
        self.province = province
        self.country = country

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"population({self.province}, Population), province_of({self.country}, {self.province})."

    def success(self, res):
        population = res[0]["Population"]
        print(f"Население провинции {self.province} в стране {self.country}: {population}")

    def failure(self, res):
        print(f"Не удалось найти информацию о населении провинции {self.province} в стране {self.country}.")
