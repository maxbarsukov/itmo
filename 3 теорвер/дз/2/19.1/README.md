# Идз 19.1 из сборника Рябушко

Решение будет сгенерировано в файл `result.tex.</BR>
Для работы скрипта надо записать значения из варианта, номер варианта и имя:
```python
if __name__ == "__main__":
    # Значения из варианта
    values = """
    """

    variant = ""  # Номер варианта
    name = ""  # Имя автора

    values = make_table(values)
    template_handler = TemplateHandler(variant, name)
    process(values, template_handler, True)
```
После можно запускать скрипт.
Для работы нужен `matplotlib`.
