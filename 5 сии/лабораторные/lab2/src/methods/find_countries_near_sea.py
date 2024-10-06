from swiplserver import PrologThread


class FindCountriesNearSea:
    def __init__(self, sea: str):
        self.sea = sea

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res or len(res) == 0:
            self.failure(res)
        else:
            self.success(res)

    def query(self):
        return f"has_access_to_sea(Province, {self.get_seas()}), province_of(Country, Province)."

    def success(self, res):
        unique_res = list({object_["Country"]: object_ for object_ in res}.values())
        print(f"Найдено {len(unique_res)} стран с выходом к {self.sea}:")
        for index, line in enumerate(unique_res, 1):
            print(f"{index}. {line['Country']}")

    def failure(self, res):
        print(f"Нет стран с выходом к {self.sea}.")

    def get_seas(self):
        seas = {
            "Чёрному морю": "black_sea",
            "Средиземному морю": "mediterranean_sea",
            "Балтийскому морю": "baltic_sea",
            "Адриатическому морю": "adriatic_sea",
            "Красному морю": "red_sea",
            "Атлантическому океану": "atlantic_ocean",
            "Тихому океану": "pacific_ocean",
            "Индийскому океану": "indian_ocean",
        }
        if self.sea not in seas:
            return "not_found"
        return seas[self.sea]
