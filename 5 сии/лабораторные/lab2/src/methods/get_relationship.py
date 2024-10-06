from swiplserver import PrologThread


class GetRelationship:
    def __init__(self, country1: str, country2: str):
        self.country1 = country1
        self.country2 = country2

    def run(self, prolog: PrologThread):
        if self.country1 == self.country2:
            print(
                f"Не удалось найти информацию об отношении {self.country1} к {self.country2}, так как это одна и та же страна."
            )
            return

        res = prolog.query(self.query())
        if not res:
            self.failure()
        else:
            self.success(res)

    def query(self):
        return f"current_relationship({self.country1}, {self.country2}, Relationship)."

    def success(self, res):
        relationship = res[0]["Relationship"]
        print(f"Отношение {self.country1} к {self.country2} составляет {relationship}.")

    def failure(self):
        print(f"Не удалось найти информацию об отношении {self.country1} к {self.country2}.")
