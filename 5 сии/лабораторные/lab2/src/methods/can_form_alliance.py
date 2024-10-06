from swiplserver import PrologThread


class CanFormAlliance:
    def __init__(self, country: str):
        self.country = country

    def run(self, prolog: PrologThread):
        res = prolog.query(self.query())
        if not res:
            self.failure()
        else:
            self.success()

    def query(self):
        return f"can_form_alliance({self.country})."

    def success(self):
        print(f"Да, {self.country} может создать новый альянс при текущем уровне мировой напряженности.")

    def failure(self):
        print(f"Нет, {self.country} не может создать новый альянс при текущем уровне мировой напряженности.")
