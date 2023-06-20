-- 1. Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:
-- Н_ТИПЫ_ВЕДОМОСТЕЙ, Н_ВЕДОМОСТИ.
-- Вывести атрибуты: Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ, Н_ВЕДОМОСТИ.ДАТА.
-- Фильтры (AND):
-- a) Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ = Ведомость.
-- b) Н_ВЕДОМОСТИ.ЧЛВК_ИД > 163249.
-- Вид соединения: RIGHT JOIN.
EXPLAIN ANALYZE SELECT Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ, Н_ВЕДОМОСТИ.ДАТА
FROM Н_ТИПЫ_ВЕДОМОСТЕЙ
RIGHT JOIN Н_ВЕДОМОСТИ ON Н_ТИПЫ_ВЕДОМОСТЕЙ.ИД = Н_ВЕДОМОСТИ.ТВ_ИД
WHERE Н_ТИПЫ_ВЕДОМОСТЕЙ.НАИМЕНОВАНИЕ = 'Ведомость' AND
    Н_ВЕДОМОСТИ.ЧЛВК_ИД > 163249;

-- 2. Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:
-- Таблицы: Н_ЛЮДИ, Н_ОБУЧЕНИЯ, Н_УЧЕНИКИ.
-- Вывести атрибуты: Н_ЛЮДИ.ИМЯ, Н_ОБУЧЕНИЯ.ЧЛВК_ИД, Н_УЧЕНИКИ.ИД.
-- Фильтры: (AND)
-- a) Н_ЛЮДИ.ИМЯ < Александр.
-- b) Н_ОБУЧЕНИЯ.ЧЛВК_ИД > 163276.
-- c) Н_УЧЕНИКИ.ГРУППА = 3100.
-- Вид соединения: INNER JOIN.
EXPLAIN ANALYZE SELECT Н_ЛЮДИ.ИМЯ, Н_ОБУЧЕНИЯ.ЧЛВК_ИД, Н_УЧЕНИКИ.ИД
FROM Н_ЛЮДИ
INNER JOIN Н_ОБУЧЕНИЯ ON Н_ЛЮДИ.ИД = Н_ОБУЧЕНИЯ.ЧЛВК_ИД
INNER JOIN Н_УЧЕНИКИ ON Н_ОБУЧЕНИЯ.ЧЛВК_ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ИМЯ < 'Александр' AND
    Н_ОБУЧЕНИЯ.ЧЛВК_ИД > 163 AND
    Н_УЧЕНИКИ.ГРУППА = '3100';


-- Explain 1
 Nested Loop  (cost=830.93..6751.65 rows=74147 width=426) (actual time=6.784..72.728 rows=190897 loops=1)
   ->  Seq Scan on "Н_ТИПЫ_ВЕДОМОСТЕЙ"  (cost=0.00..1.04 rows=1 width=422) (actual time=0.011..0.016 rows=1 loops=1)
         Filter: (("НАИМЕНОВАНИЕ")::text = 'Ведомость'::text)
         Rows Removed by Filter: 2
   ->  Bitmap Heap Scan on "Н_ВЕДОМОСТИ"  (cost=830.93..6009.14 rows=74147 width=12) (actual time=6.769..44.131 rows=190897 loops=1)
         Recheck Cond: ("ТВ_ИД" = "Н_ТИПЫ_ВЕДОМОСТЕЙ"."ИД")
         Filter: ("ЧЛВК_ИД" > 1)
         Heap Blocks: exact=4040
         ->  Bitmap Index Scan on "ВЕД_ТВ_FK_I"  (cost=0.00..812.40 rows=74147 width=0) (actual time=6.138..6.138 rows=190897 loops=1)       
               Index Cond: ("ТВ_ИД" = "Н_ТИПЫ_ВЕДОМОСТЕЙ"."ИД")
 Planning Time: 0.257 ms
 Execution Time: 81.498 ms

-- Explain 2
 Nested Loop  (cost=0.57..444.90 rows=8 width=21) (actual time=0.375..3.745 rows=3 loops=1)
   Join Filter: ("Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД")
   ->  Nested Loop  (cost=0.28..286.73 rows=184 width=21) (actual time=0.025..3.010 rows=190 loops=1)
         ->  Seq Scan on "Н_ЛЮДИ"  (cost=0.00..163.97 rows=188 width=17) (actual time=0.014..2.625 rows=189 loops=1)
               Filter: (("ИМЯ")::text < 'Александр'::text)
               Rows Removed by Filter: 4929
         ->  Index Only Scan using "ОБУЧ_ЧЛВК_FK_I" on "Н_ОБУЧЕНИЯ"  (cost=0.28..0.64 rows=1 width=4) (actual time=0.001..0.002 rows=1 loops=189)
               Index Cond: (("ЧЛВК_ИД" = "Н_ЛЮДИ"."ИД") AND ("ЧЛВК_ИД" > 163))
               Heap Fetches: 0
   ->  Index Scan using "УЧЕН_ОБУЧ_FK_I" on "Н_УЧЕНИКИ"  (cost=0.29..0.85 rows=1 width=8) (actual time=0.004..0.004 rows=0 loops=190)        
         Index Cond: ("ЧЛВК_ИД" = "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД")
         Filter: (("ГРУППА")::text = '3100'::text)
         Rows Removed by Filter: 4
 Planning Time: 0.711 ms
 Execution Time: 3.781 ms
