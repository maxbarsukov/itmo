from swiplserver import PrologThread


class FindCountriesInAlliance:
    def __init__(self, alliance: str):
        self.alliance = alliance

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"alliance(Country, {self.get_alliance_tag()})"

    def success(self, res):
        print(f"Найдено {len(res)} стран, которые входят в альянс {self.alliance}:")
        for index, line in enumerate(res, 1):
            print(f"{index}. ", line["Country"])

    def failure(self, res):
        print(f"Нет стран, которые входят в альянс {self.alliance}.")

    def get_alliance_tag(self):
        alliances = {
            "Ось": "axis",
            "Союзники": "allies",
            "Коминтерн": "comintern",
            "Второй объединённый фронт": "united_chinese_front",
            "Великая восточноазиатская сфера сопроцветания": "great_east_asia_coprosperity_sphere",
        }
        if self.alliance not in alliances:
            return "not_found"
        return alliances[self.alliance]
