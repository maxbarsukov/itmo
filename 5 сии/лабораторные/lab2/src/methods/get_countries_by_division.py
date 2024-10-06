from swiplserver import PrologThread


class GetCountriesByDivision:
    def __init__(self, division: str):
        self.division = division

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure()
        else:
            self.success(res)

    def query(self):
        return f"has_division(Army, {self.division_name()}), army(Army, Country)."

    def success(self, res):
        print(f"Страны, у которых в войсках есть {self.division} дивизии:")
        unique_res = list({object_["Country"]: object_ for object_ in res}.values())
        for index, line in enumerate(unique_res, 1):
            print(f"{index}. {line['Country']}")

    def failure(self):
        print(f"Нет стран с дивизией {self.division}.")

    def division_name(self):
        divisions = {
            "пехотные": "infantry_division",
            "десантные": "naval_division",
            "артиллерийские": "artillery_division",
            "танковые": "armored_division",
        }
        if self.division not in divisions:
            return "not_found"
        return divisions[self.division]
