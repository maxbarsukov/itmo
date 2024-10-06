from swiplserver import PrologThread


class FindDefensiveCountries:
    def __init__(self, country: str):
        self.country = country

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure()
        else:
            self.success(res)

    def query(self):
        return f"at_defensive_war(Country, {self.country})."

    def success(self, res):
        print(f"На {self.country} нападают страны:")
        for index, line in enumerate(res, 1):
            print(f"{index}. {line['Country']}")

    def failure(self):
        print(f"{self.country} не находится в оборонительной войне.")
