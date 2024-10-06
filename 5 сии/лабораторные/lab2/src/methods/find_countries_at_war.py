from swiplserver import PrologThread


class FindCountriesAtWar:
    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return "at_offencive_war(Country1, Country2)"

    def success(self, res):
        print(f"Найдено {len(res)} стран, которые воюют друг с другом:")
        for index, line in enumerate(res, 1):
            print(f"{index}. {line['Country1']} воюет с {line['Country2']}")

    def failure(self, res):
        print("Нет стран, которые воюют друг с другом.")
