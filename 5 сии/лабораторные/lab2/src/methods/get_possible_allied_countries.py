from swiplserver import PrologThread


class GetPossibleAlliedCountries:
    def __init__(self, country: str):
        self.country = country

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure()
        else:
            self.success(res)

    def query(self):
        return f"can_invite_to_alliance({self.country}, _, Country)."

    def success(self, res):
        print(f"Страны, которые могут вступить в альянс с {self.country}:")
        for index, line in enumerate(res, 1):
            print(f"{index}. {line['Country']}")

    def failure(self):
        print(f"Нет стран, которые могут вступить в альянс с {self.country}.")
