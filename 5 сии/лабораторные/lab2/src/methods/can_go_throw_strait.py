from swiplserver import PrologThread


class CanGoThrowStrait:
    def __init__(self, country: str, strait: str):
        self.country = country
        self.strait = strait

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res:
            self.failure()
        else:
            self.success()

    def query(self):
        return f"can_cross_the_strait({self.country}, {self.get_strait()})."

    def success(self):
        print(f"Да, {self.country} может пересечь {self.strait}.")

    def failure(self):
        print(f"Нет, {self.country} не может пересечь {self.strait}.")

    def get_strait(self):
        straits = {
            "Босфор и Дарданеллы": "bosporus_and_dardanelles",
            "Панамский канал": "panama_canal_strait",
            "Суэцкий канал": "suez_canal",
            "пролив Отранто": "otranto",
            "Гибралтарский пролив": "gibraltar_strait",
            "Кильский канал": "kiel_canal",
            "Баб-эль-Мандебский пролив": "bab_el_mandeb",
            "Сингапурский пролив": "singapur_strait",
        }
        if self.strait not in straits:
            return "not_found"
        return straits[self.strait]
