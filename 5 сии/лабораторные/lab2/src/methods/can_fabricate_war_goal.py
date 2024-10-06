from swiplserver import PrologThread


class CanFabricateWarGoal:
    def __init__(self, country1: str, country2: str):
        self.country1 = country1
        self.country2 = country2

    def run(self, prolog: PrologThread):
        if self.country1 == self.country2:
            print(
                f"Нет, {self.country1} не может сфабриковать цель войны на {self.country2}, так как это одна и та же страна."
            )
            return

        res = prolog.query(self.query())
        if not res:
            self.failure()
        else:
            self.success()

    def query(self):
        return f"can_justify_war_goal({self.country1}, {self.country2})."

    def success(self):
        print(f"Да, {self.country1} может сфабриковать цель войны на {self.country2}.")

    def failure(self):
        print(f"Нет, {self.country1} не может сфабриковать цель войны на {self.country2}.")
