from swiplserver import PrologThread


class FindCountriesByIdeology:
    def __init__(self, ideology: str):
        self.ideology = ideology

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"ideology(Country, {self.get_ideology()})."

    def success(self, res):
        print(f"Найдено {len(res)} стран с идеологией {self.ideology}:")
        for index, line in enumerate(res, 1):
            print(f"{index}. {line['Country']}")

    def failure(self, res):
        print(f"Нет стран с идеологией {self.ideology}.")

    def get_ideology(self):
        ideologies = {
            "коммунистическими": "communist",
            "демократическими": "democratic",
            "нейтральными": "neutral",
            "фашистскими": "fascist",
        }
        if self.ideology not in ideologies:
            return "not_found"
        return ideologies[self.ideology]
