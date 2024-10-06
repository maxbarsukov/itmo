import logging
import re

from src.methods import (
    can_fabricate_war_goal,
    can_form_alliance,
    can_go_throw_strait,
    find_countries_at_war,
    find_countries_by_ideology,
    find_countries_in_alliance,
    find_countries_near_sea,
    find_defensive_countries,
    get_countries_by_division,
    get_population,
    get_possible_allied_countries,
    get_puppets,
    get_recruits_number,
    get_relationship,
)
from swiplserver import PrologMQI, create_posix_path

KNOWLEDGE_BASE_PATH = "./src/prolog/knowledge_base.pl"

incorrect_request = "Неправильный запрос"

requests = [
    # Ось / Союзники / Коминтерн / Второй объединённый фронт / Великая восточноазиатская сфера сопроцветания
    "Какие страны входят в альянс Ось?",
    "Какие страны воюют друг с другом?",
    # коммунистическими / демократическими / нейтральными / фашистскими
    "Какие страны являются коммунистическими?",
    "Какие страны могут вступить в альянс с uk?",
    "Может ли usa создать новый альянс при текущем уровне мировой напряженности?",
    "Какой количество рекрутов в poland?",
    # Чёрному морю / Средиземному морю / Балтийскому морю / Адриатическому морю / Красному морю
    # Атлантическому океану / Тихому океану / Индийскому океану
    "Какие страны имеют выход к Чёрному морю?",
    # пехотные / десантные / артиллерийские / танковые
    "Армии каких стран имеют в своём составе пехотные дивизии?",
    "Какие страны находятся в оборонительной войне против germany?",
    "Какое население провинции ulaanbaatar в mongolia?",
    "Какие страны марионетки japan?",
    # Босфор и Дарданеллы / Панамский канал / Суэцкий канал / пролив Отранто
    # Гибралтарский пролив / Кильский канал / Баб-эль-Мандебский пролив / Сингапурский пролив
    "Может ли ussr пересечь Босфор и Дарданеллы?",
    "Я играю за страну uk. Какое мое текущее отношение к france?",
    "Может ли hungary сфабриковать цель войны на yugoslavia?",
]

patterns = {
    r"Какие страны входят в альянс (.+)\?": find_countries_in_alliance.FindCountriesInAlliance,
    r"Какие страны воюют друг с другом\?": find_countries_at_war.FindCountriesAtWar,
    r"Какие страны являются (.+)\?": find_countries_by_ideology.FindCountriesByIdeology,
    r"Какие страны могут вступить в альянс с (.+)\?": get_possible_allied_countries.GetPossibleAlliedCountries,
    r"Может ли (.+) создать новый альянс при текущем уровне мировой напряженности\?": can_form_alliance.CanFormAlliance,
    r"Какой количество рекрутов в (.+)\?": get_recruits_number.GetRecruitsNumber,
    r"Какие страны имеют выход к (.+)\?": find_countries_near_sea.FindCountriesNearSea,
    r"Армии каких стран имеют в своём составе (.+) дивизии\?": get_countries_by_division.GetCountriesByDivision,
    r"Какие страны находятся в оборонительной войне против (.+)\?": find_defensive_countries.FindDefensiveCountries,
    r"Какое население провинции (.+) в (.+)\?": get_population.GetPopulation,
    r"Какие страны марионетки (.+)\?": get_puppets.GetPuppets,
    r"Может ли (.+) пересечь (.+)\?": can_go_throw_strait.CanGoThrowStrait,
    r"Я играю за страну (.+). Какое мое текущее отношение к (.+)\?": get_relationship.GetRelationship,
    r"Может ли (.+) сфабриковать цель войны на (.+)\?": can_fabricate_war_goal.CanFabricateWarGoal,
}


def start() -> None:
    logging.getLogger().setLevel(logging.WARNING)

    with PrologMQI() as mqi:
        with mqi.create_thread() as prolog:
            path = create_posix_path(KNOWLEDGE_BASE_PATH)
            logging.debug("DEBUG: knowledge base path %s", path)

            prolog.query(f'consult("{path}")')
            logging.debug("DEBUG: knowledge base loaded")

            print("\nПримеры запросов, которые вам доступны:", "\n * " + ("\n * ".join(requests)))
            print("\nДля завершения введите - exit.")

            while True:
                query = input("> ")
                if query.lower() == "exit":
                    break

                for pattern in patterns:
                    match = re.match(pattern, query, re.IGNORECASE)
                    if match is None:
                        continue

                    logging.debug("DEBUG: '%s' matches to '%s'", query, match)
                    processor = patterns[pattern](*match.groups())
                    processor.run(prolog)
                    break
                else:
                    print(incorrect_request)


if __name__ == "__main__":
    start()
