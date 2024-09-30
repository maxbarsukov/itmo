%% Простые запросы к базе знаний для поиска фактов.

% Есть ли в игре страна tabulestan?
country(tabulestan). % => false
country(tannutuva). % => true

% Кирп - провинция Великобритании?
province_of(uk, cyprus). % => true

% Имеет ли Савойя доступ к средиземному морю?
has_access_to_sea(savoy, mediterranean). % => true

% Гарантирует ли Великобритания независимость Югославии?
guarantee_independence(uk, yugoslavia). % => true


%% Запросы, использующие логические операторы (**и, или, не**) для
%% формулирования сложных условий (или использовать логические операторы в правилах).

% Идеология ussr коммунситическая или нейтральная?
ideology(ussr, communist); ideology(ussr, neutral).


%% Запросы, использующие переменные для поиска объектов с определенными характеристиками.

% Какая страна, обороняющаяся от Германии, не является демократией?
at_defensive_war(X, germany), not(ideology(X, democratic)).

% Вывести страны из альянса Axis, которые участвуют в наступательной войне на Францию.
alliance(X, axis), at_offencive_war(X, france).

% Вывести лидера альянса, в котором находится Монголия.
alliance(mongolia, Alliance), alliance_head(Alliance, AllianceLeader).

% Какие страны могут переплыть через Суэцкий канал и пройти из Красного моря в Индийский океан?
can_cross_the_strait(X, suez_canal), is_connected_for_country(X, red_sea, indian_ocean).


%% Запросы, которые требуют выполнения правил для получения результата.

% Может ли Япония совершить высадку в Китай?
can_land(japan, china).

% Может ли Великобритания использовать ресурсы Бриатнской Малайи?
can_use_resources(uk, british_malaya).

% Какие отношения между Великобританией и Югославией?
current_relationship(yugoslavia, uk, X).
current_relationship(uk, yugoslavia, X).
