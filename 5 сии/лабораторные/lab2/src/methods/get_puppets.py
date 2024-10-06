from swiplserver import PrologThread


class GetPuppets:
    def __init__(self, country: str):
        self.country = country

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure()
        else:
            self.success(res)

    def query(self):
        return f"puppet({self.country}, PuppetCountry)."

    def success(self, res):
        print(f"Марионетки {self.country}:")
        for index, line in enumerate(res, 1):
            print(f"{index}. {line['PuppetCountry']}")

    def failure(self):
        print(f"У {self.country} нет марионеток.")
