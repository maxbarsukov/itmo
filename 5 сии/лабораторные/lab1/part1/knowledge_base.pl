% База знаний об игре Hearts of Iron 4
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

/* Факты */

/* Свойства */
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Мировая напряженность
world_tension(51). % Значение от 0 до 100

% Страны

% Европа
country(ussr).
country(latvia).
country(lithuania).
country(estonia).
country(poland).
country(germany).
country(slovakia).
country(italy).
country(hungary).
country(romania).
country(yugoslavia).
country(greece).
country(bulgaria).
country(turkey).
country(belguim).
country(netherlands).
country(luxembourd).
country(france).
country(denmark).
country(iceland).
country(norway).
country(sweden).
country(finland).
country(francist_spain).
country(portugal).
country(uk).
country(ireland).

% Северная Америка
country(usa).
country(canada).
country(mexico).
% Проигнорируем центральноамериканские страны

% Южная Америка
country(brazil).
country(ecuador).
country(peru).
country(bolivia).
country(paraguay).
country(uruguay).
country(chili).
country(argentina).

% Африка
country(italian_eastern_africa).
country(south_africa).
country(liberia).

% Ближний восток
% Проигнорирован

% Азия
country(iran).
country(afganistan).
country(british_raj).
country(nepal).
country(bhutan).
country(mongolia).
country(tannutuva).
country(tibet).
country(sinkiang).
country(xibeisanma).
country(yunnan).
country(guangxi_clique).
country(china).
country(mengkukuo).
country(manchukuo).
country(communist_china).
country(japan).
country(siamese_empire).
country(dutch_east_indies).
country(philipines).
country(british_malaya).

% Океания
country(australia).
country(new_zeland).


% Мажоры и миноры
major_country(ussr).
major_country(usa).
major_country(uk).
major_country(france).
major_country(china).
major_country(germany).
major_country(italy).
major_country(japan).

% Факты о снаряжении
equipment(infantry_rifle).
equipment(tank).
equipment(convoy).
equipment(battle_ship).
equipment(artillery).
equipment(fighter).
equipment(cas).
equipment(bomber).

% Факты о дивизиях
division(infantry_division).
division(armored_division).
division(artillery_division).
division(naval_division).

% Факты о морях
sea(black_sea).
sea(mediterranean_sea).
sea(baltic_sea).
sea(adriatic_sea).
sea(red_sea).
sea(atlantic_ocean).
sea(pacific_ocean).
sea(indian_ocean).

% Закон о призыве по умолчанию 3%
default_draft_rate(2.5).


/* Отношения */
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Законы о призыве
draft_law(ussr, 5).
draft_law(mongolia, 2.5).
draft_law(usa, 1.5).
draft_law(uk, 2.5).
draft_law(france, 5).
draft_law(china, 10).
draft_law(communist_china, 10).
draft_law(germany, 5).
draft_law(italy, 5).
draft_law(japan, 10).
draft_law(poland, 2.5).
draft_law(francist_spain, 2.5).
draft_law(portugal, 1.5).
draft_law(romania, 2.5).
draft_law(hungary, 5).
draft_law(turkey, 2.5).
draft_law(belgium, 2.5).
draft_law(yugoslavia, 2.5).
draft_law(slovakia, 10).
draft_law(british_raj, 1.5).

% Факты о проливах и контролирующих их странах
strait(bosporus_and_dardanelles, turkey).
strait(panama_canal_strait, usa).
strait(suez_canal, uk).
strait(otranto, italy).
strait(gibraltar_strait, uk).
strait(kiel_canal, denmark).
strait(bab_el_mandeb, france).
strait(singapur_strait, uk).

% Состав дивизий и их снаряжение
has_equipment(infantry_division, infantry_rifle).
has_equipment(infantry_division, artillery).

has_equipment(armored_division, tank).
has_equipment(armored_division, infantry_rifle).

has_equipment(artillery_division, artillery).
has_equipment(artillery_division, infantry_rifle).

has_equipment(naval_division, convoy).
has_equipment(naval_division, battle_ship).

% Пример фактов о армиях
army(german_army, germany).
army(ussr_army, ussr).
army(usa_army, usa).
army(uk_army, uk).
army(canadian_expeditional_army, uk).
army(french_army, france).
army(polish_army, poland).
army(italian_army, italy).
army(japanese_army, japan).
army(chinese_army, china).
army(bhutanese_army, bhutan).

% Генералы
general(german_army, guderian).
general(ussr_army, zhukov).
general(usa_army, eisenhower).
general(uk_army, ironside).
general(canadian_expeditional_army, murchie).
general(french_army, leclerc).
general(polish_army, maczek).
general(italian_army, graziani).
general(japanese_army, sugiyama).
general(chinese_army, zhihang).

% Состав армий
has_division(german_army, infantry_division).
has_division(german_army, armored_division).
has_division(german_army, artillery_division).
has_division(german_army, naval_division).

has_division(ussr_army, infantry_division).
has_division(ussr_army, armored_division).
has_division(ussr_army, artillery_division).

has_division(uk_army, infantry_division).
has_division(uk_army, armored_division).
has_division(uk_army, naval_division).

has_division(canadian_expeditional_army, infantry_division).
has_division(canadian_expeditional_army, naval_division).

has_division(french_army, infantry_division).
has_division(french_army, artillery_division).

has_division(polish_army, infantry_division).
has_division(polish_army, artillery_division).

has_division(italian_army, infantry_division).
has_division(italian_army, armored_division).
has_division(italian_army, artillery_division).

has_division(japanese_army, infantry_division).
has_division(japanese_army, armored_division).
has_division(japanese_army, naval_division).

has_division(chinese_army, infantry_division).

has_division(bhutanese_army, infantry_division).


% Факты об идеологиях стран
ideology(ussr, communist).
ideology(mongolia, communist).
ideology(usa, democratic).
ideology(uk, democratic).
ideology(france, democratic).
ideology(china, neutral).
ideology(communist_china, communist).
ideology(germany, fascist).
ideology(italy, fascist).
ideology(japan, fascist).
ideology(poland, neutral).
ideology(francist_spain, neutral).
ideology(portugal, neutral).
ideology(romania, neutral).
ideology(hungary, fascist).
ideology(turkey, neutral).
ideology(belgium, democratic).
ideology(yugoslavia, neutral).
ideology(slovakia, fascist).
ideology(british_raj, neutral).

% Правители
leader(ussr, stalin).
leader(mongolia, choibalsan).
leader(usa, roosevelt).
leader(uk, chamberlain).
leader(france, blum).
leader(china, kaishek).
leader(germany, hitler).
leader(italy, mussolini).
leader(japan, hirohito).
leader(poland, moscicki).
leader(francist_spain, franco).
leader(portugal, salazar).
leader(romania, carol2).
leader(hungary, horthy).
leader(turkey, inonu).
leader(belgium, pierlot).
leader(yugoslavia, prince_paul).
leader(slovakia, tiso).
leader(british_raj, linlithgow).

% Провинции
province_of(tannutuva, tuva).
province_of(mongolia, dornod).
province_of(mongolia, gobi).
province_of(mongolia, khovd).
province_of(mongolia, khovsgol).
province_of(mongolia, ulaanbaatar).
province_of(usa, whole_usa).
province_of(uk, nothern_ireland).
province_of(uk, gibraltar).
province_of(uk, malta).
province_of(uk, cyprus).
province_of(uk, labrador).
province_of(uk, newfoundland).
province_of(uk, suez).
province_of(uk, other_uk).
province_of(france, french_india).
province_of(france, guangzouwan).
province_of(france, french_islands).
province_of(france, alsace_lorraine).
province_of(france, savoy).
province_of(france, other_france).
province_of(china, whole_china).
province_of(germany, whole_germany).
province_of(italy, dalmacia).
province_of(italy, other_italy).
province_of(japan, japanese_china).
province_of(japan, south_sakhalin).
province_of(japan, kuril_islands).
province_of(japan, other_japan).
province_of(ussr, whole_ussr).
province_of(communist_china, shaanxi).
province_of(poland, danzig).
province_of(poland, east_poland).
province_of(poland, west_poland).
province_of(francist_spain, whole_spain).
province_of(portugal, goa).
province_of(portugal, macau).
province_of(portugal, whole_portugal).
province_of(romania, bessarabia).
province_of(romania, southern_bessarabia).
province_of(romania, bucovina).
province_of(romania, moldova).
province_of(romania, muntenia).
province_of(romania, dobrudja).
province_of(romania, oltenia).
province_of(romania, north_transylvania).
province_of(romania, transylvania).
province_of(romania, banat).
province_of(romania, crisana).
province_of(hungary, southern_slovakia).
province_of(hungary, transdanubia).
province_of(hungary, nothern_hungary).
province_of(hungary, alfold).
province_of(hungary, carpathian_ruthenia).
province_of(turkey, whole_turkey).
province_of(turkey, istanbul).
province_of(belgium, wallonie).
province_of(belgium, vlaanderen).
province_of(belgium, belguim_africa).
province_of(yugoslavia, serbia).
province_of(yugoslavia, macedonia).
province_of(yugoslavia, southern_serbia).
province_of(yugoslavia, vojvodina).
province_of(yugoslavia, west_banat).
province_of(slovakia, eastern_slovakia).
province_of(slovakia, western_slovakia).
province_of(british_raj, india).
province_of(british_raj, pakistan).

% Население в провинции
population(dornod, 88358).
population(gobi, 11013).
population(khovd, 149182).
population(khovsgol, 88308).
population(ulaanbaatar, 436082).
population(whole_usa, 125020000).
population(nothern_ireland, 1200000).
population(gibraltar, 21390).
population(malta, 241910).
population(cyprus, 348380).
population(labrador, 22200).
population(newfoundland, 255370).
population(suez, 131890).
population(other_uk, 122530000).
population(french_india, 286760).
population(guangzouwan, 200240).
population(alsace_lorraine, 1910000).
population(savoy, 908600).
population(other_france, 111768300).
population(whole_china, 171290000).
population(whole_germany, 82980000).
population(dalmacia, 1680000).
population(other_italy, 45028420).
population(japanese_china, 248840000).
population(south_sakhalin, 295550).
population(kuril_islands, 15010).
population(other_japan, 64860000).
population(whole_ussr, 161600000).
population(shaanxi, 7010000).
population(danzig, 367170).
population(east_poland, 20370000).
population(west_poland, 11640000).
population(whole_spain, 25710000).
population(goa, 534600).
population(macau, 157990).
population(whole_portugal, 7720000).
population(bessarabia, 2290000).
population(southern_bessarabia, 567290).
population(bucovina, 475170).
population(moldova, 2800000).
population(muntenia, 4460000).
population(dobrudja, 378460).
population(oltenia, 1520000).
population(north_transylvania, 2350000).
population(transylvania, 1620000).
population(banat, 942640).
population(crisana, 633670).
population(southern_slovakia, 855340).
population(transdanubia, 2200000).
population(nothern_hungary, 2730000).
population(alfold, 3750000).
population(carpathian_ruthenia, 855340).
population(whole_turkey, 15510000).
population(istanbul, 1040000).
population(wallonie, 3380000).
population(vlaanderen, 4750000).
population(belguim_africa, 11470000).
population(serbia, 9340000).
population(macedonia, 1370000).
population(southern_serbia, 210250).
population(vojvodina, 839640).
population(west_banat, 586290).
population(eastern_slovakia, 1190000).
population(western_slovakia, 1230000).
population(india, 313130000).
population(pakistan, 37810000).

% Претензии
claim(ireland, nothern_ireland).
claim(turkey, cyprus).
claim(canada, labrador).
claim(canada, newfoundland).
claim(british_raj, french_india).
claim(china, guangzouwan).
claim(communist_china, guangzouwan).
claim(germany, alsace_lorraine).
claim(italy, savoy).
claim(communist_china, whole_china).
claim(yugoslavia, dalmacia).
claim(china, japanese_china).
claim(communist_china, japanese_china).
claim(ussr, south_sakhalin).
claim(ussr, kuril_islands).
claim(china, shaanxi).
claim(germany, danzig).
claim(ussr, east_poland).
claim(british_raj, goa).
claim(china, macau).
claim(communist_china, macau).
claim(ussr, bessarabia).
claim(ussr, southern_bessarabia).
claim(ussr, bucovina).
claim(bulgaria, dobrudja).
claim(hungary, north_transylvania).
claim(hungary, transylvania).
claim(hungary, crisana).
claim(hungary, banat).
claim(yugoslavia, banat).
claim(slovakia, southern_slovakia).
claim(ussr, carpathian_ruthenia).
claim(slovakia, carpathian_ruthenia).
claim(bulgaria, macedonia).
claim(bulgaria, southern_serbia).
claim(hungary, vojvodina).
claim(hungary, west_banat).
claim(ussr, tuva).

% Марионетки
puppet(germany, slovakia).
puppet(italy, italian_eastern_africa).
puppet(usa, philipines).
puppet(uk, australia).
puppet(uk, canada).
puppet(uk, south_africa).
puppet(uk, new_zeland).
puppet(uk, british_raj).
puppet(uk, british_malaya).
puppet(denmark, iceland).
puppet(japan, mengkukuo).
puppet(japan, manchukuo).
puppet(netherlands, dutch_east_indies).

% Факты об альянсах
alliance_head(axis, germany).
alliance_head(comintern, ussr).
alliance_head(great_east_asia_coprosperity_sphere, japan).
alliance_head(united_chinese_front, china).
alliance_head(allies, uk).

alliance(germany, axis).
alliance(italy, axis).
alliance(hungary, axis).
alliance(slovakia, axis).
alliance(italian_eastern_africa, axis).

alliance(ussr, comintern).
alliance(tannutuva, comintern).
alliance(mongolia, comintern).

alliance(japan, great_east_asia_coprosperity_sphere).
alliance(manchukuo, great_east_asia_coprosperity_sphere).
alliance(mengkukuo, great_east_asia_coprosperity_sphere).

alliance(china, united_chinese_front).
alliance(sinkiang, united_chinese_front).
alliance(yunnan, united_chinese_front).
alliance(communist_china, united_chinese_front).
alliance(guangxi_clique, united_chinese_front).
alliance(xibeisanma, united_chinese_front).

alliance(uk, allies).
alliance(australia, allies).
alliance(canada, allies).
alliance(south_africa, allies).
alliance(new_zeland, allies).
alliance(british_raj, allies).
alliance(british_malaya, allies).
alliance(france, allies).

% Факты о выходе провинций к морям
has_access_to_sea(whole_usa, atlantic_ocean).
has_access_to_sea(whole_usa, pacific_ocean).
has_access_to_sea(nothern_ireland, atlantic_ocean).
has_access_to_sea(gibraltar, mediterranean_sea).
has_access_to_sea(gibraltar, atlantic_ocean).
has_access_to_sea(malta, mediterranean_sea).
has_access_to_sea(cyprus, mediterranean_sea).
has_access_to_sea(labrador, atlantic_ocean).
has_access_to_sea(newfoundland, atlantic_ocean).
has_access_to_sea(suez, mediterranean_sea).
has_access_to_sea(suez, red_sea).
has_access_to_sea(other_uk, atlantic_ocean).
has_access_to_sea(other_uk, mediterranean_sea).
has_access_to_sea(other_uk, pacific_ocean).
has_access_to_sea(other_uk, red_sea).
has_access_to_sea(other_uk, indian_ocean).
has_access_to_sea(french_india, indian_ocean).
has_access_to_sea(guangzouwan, indian_ocean).
has_access_to_sea(french_islands, pacific_ocean).
has_access_to_sea(french_islands, atlantic_ocean).
has_access_to_sea(savoy, mediterranean_sea).
has_access_to_sea(other_france, mediterranean_sea).
has_access_to_sea(other_france, atlantic_ocean).
has_access_to_sea(other_france, pacific_ocean).
has_access_to_sea(other_france, indian_ocean).
has_access_to_sea(whole_china, pacific_ocean).
has_access_to_sea(whole_germany, baltic_sea).
has_access_to_sea(dalmacia, adriatic_sea).
has_access_to_sea(other_italy, adriatic_sea).
has_access_to_sea(other_italy, mediterranean_sea).
has_access_to_sea(other_italy, red_sea).
has_access_to_sea(japanese_china, pacific_ocean).
has_access_to_sea(south_sakhalin, pacific_ocean).
has_access_to_sea(kuril_islands, pacific_ocean).
has_access_to_sea(other_japan, pacific_ocean).
has_access_to_sea(whole_ussr, pacific_ocean).
has_access_to_sea(whole_ussr, black_sea).
has_access_to_sea(whole_ussr, baltic_sea).
has_access_to_sea(danzig, baltic_sea).
has_access_to_sea(west_poland, baltic_sea).
has_access_to_sea(whole_spain, atlantic_ocean).
has_access_to_sea(whole_spain, mediterranean_sea).
has_access_to_sea(goa, indian_ocean).
has_access_to_sea(macau, pacific_ocean).
has_access_to_sea(whole_portugal, atlantic_ocean).
has_access_to_sea(whole_portugal, indian_ocean).
has_access_to_sea(southern_bessarabia, black_sea).
has_access_to_sea(muntenia, black_sea).
has_access_to_sea(dobrudja, black_sea).
has_access_to_sea(whole_turkey, black_sea).
has_access_to_sea(whole_turkey, mediterranean_sea).
has_access_to_sea(istanbul, black_sea).
has_access_to_sea(istanbul, mediterranean_sea).
has_access_to_sea(vlaanderen, atlantic_ocean).
has_access_to_sea(belguim_africa, atlantic_ocean).
has_access_to_sea(serbia, adriatic_sea).
has_access_to_sea(india, indian_ocean).
has_access_to_sea(pakistan, indian_ocean).

% Факты о связи между морями и проливами
connected(adriatic_sea, otranto).
connected(otranto, adriatic_sea).
connected(otranto, mediterranean_sea).
connected(mediterranean_sea, otranto).
connected(black_sea, bosporus_and_dardanelles).
connected(bosporus_and_dardanelles, black_sea).
connected(mediterranean_sea, bosporus_and_dardanelles).
connected(bosporus_and_dardanelles, mediterranean_sea).
connected(atlantic_ocean, gibraltar_strait).
connected(gibraltar_strait, atlantic_ocean).
connected(mediterranean_sea, gibraltar_strait).
connected(gibraltar_strait, mediterranean_sea).
connected(atlantic_ocean, kiel_canal).
connected(kiel_canal, atlantic_ocean).
connected(baltic_sea, kiel_canal).
connected(kiel_canal, baltic_sea).
connected(mediterranean_sea, suez_canal).
connected(suez_canal, mediterranean_sea).
connected(red_sea, suez_canal).
connected(suez_canal, red_sea).
connected(red_sea, bab_el_mandeb).
connected(bab_el_mandeb, red_sea).
connected(indian_ocean, bab_el_mandeb).
connected(bab_el_mandeb, indian_ocean).
connected(atlantic_ocean, panama_canal_strait).
connected(panama_canal_strait, atlantic_ocean).
connected(pacific_ocean, panama_canal_strait).
connected(panama_canal_strait, pacific_ocean).
connected(indian_ocean, singapur_strait).
connected(singapur_strait, indian_ocean).
connected(pacific_ocean, singapur_strait).
connected(singapur_strait, pacific_ocean).
connected(indian_ocean, atlantic_ocean).
connected(atlantic_ocean, indian_ocean).

% Факты об отношениях между странами
relationship(yugoslavia, italy, 20).
relationship(francist_spain, portugal, 25).

% Факты о праве на проход
% country2 не имеет права на проход в country1
right_of_passage(sweden, germany).
right_of_passage(brazil, france).
right_of_passage(china, uk).
right_of_passage(brazil, canada).
right_of_passage(romania, germany).

% Пакты о ненападении
non_aggression_pact(ussr, china).
non_aggression_pact(ussr, communist_china).
non_aggression_pact(china, ussr).
non_aggression_pact(communist_china, ussr).
non_aggression_pact(germany, ussr).
non_aggression_pact(ussr, germany).
non_aggression_pact(italy, bulgaria).
non_aggression_pact(bulgaria, italy).
non_aggression_pact(francist_spain, portugal).
non_aggression_pact(portugal, francist_spain).

% Гарантия независимости
guarantee_independence(uk, poland).
guarantee_independence(uk, yugoslavia).
guarantee_independence(france, romania).
guarantee_independence(hungary, italy).
guarantee_independence(italy, hungary).
guarantee_independence(germany, hungary).


% Факты о состоянии войн
% Japanese-Chinese War
at_offencive_war(japan, china).
at_offencive_war(japan, sinkiang).
at_offencive_war(japan, yunnan).
at_offencive_war(japan, guangxi_clique).
at_offencive_war(japan, communist_china).
at_offencive_war(japan, xibeisanma).
at_offencive_war(manchukuo, china).
at_offencive_war(manchukuo, sinkiang).
at_offencive_war(manchukuo, yunnan).
at_offencive_war(manchukuo, guangxi_clique).
at_offencive_war(manchukuo, communist_china).
at_offencive_war(manchukuo, xibeisanma).
at_offencive_war(mengkukuo, china).
at_offencive_war(mengkukuo, sinkiang).
at_offencive_war(mengkukuo, yunnan).
at_offencive_war(mengkukuo, guangxi_clique).
at_offencive_war(mengkukuo, communist_china).
at_offencive_war(mengkukuo, xibeisanma).

% German-French War
at_offencive_war(germany, poland).
at_offencive_war(germany, uk).
at_offencive_war(germany, france).
at_offencive_war(germany, australia).
at_offencive_war(germany, canada).
at_offencive_war(germany, new_zeland).
at_offencive_war(germany, south_africa).
at_offencive_war(germany, british_raj).
at_offencive_war(germany, british_malaya).
at_offencive_war(slovakia, poland).
at_offencive_war(slovakia, uk).
at_offencive_war(slovakia, france).
at_offencive_war(slovakia, australia).
at_offencive_war(slovakia, canada).
at_offencive_war(slovakia, new_zeland).
at_offencive_war(slovakia, south_africa).
at_offencive_war(slovakia, british_raj).
at_offencive_war(slovakia, british_malaya).
at_offencive_war(hungary, poland).
at_offencive_war(hungary, uk).
at_offencive_war(hungary, france).
at_offencive_war(hungary, australia).
at_offencive_war(hungary, canada).
at_offencive_war(hungary, new_zeland).
at_offencive_war(hungary, south_africa).
at_offencive_war(hungary, british_raj).
at_offencive_war(hungary, british_malaya).



/* Правила */
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Правило для вычисления общего населения страны
total_population(Country, Total) :-
    findall(Pop, population(Country, Pop), Populations),
    sum_list(Populations, Total).

% Правило для вычисления числа рекрутов в стране
recruits(Country, Recruits) :-
    total_population(Country, Pop),
    (   draft_law(Country, Rate)
    ->  DraftRate is Rate / 100
    ;   default_draft_rate(DefaultRate),
        DraftRate is DefaultRate / 100
    ),
    Recruits is Pop * DraftRate.

% Правило для проверки того, что страна воюет в оборонительной войне
at_defensive_war(Country1, Country2) :-
    (at_offencive_war(Country2, Country1)),
    Country1 \= Country2.

at_defensive_war(Country1) :- at_defensive_war(Country1, _).

% Правило для проверки того, что страны воюют
at_war(Country1, Country2) :-
    (at_offencive_war(Country1, Country2); at_defensive_war(Country1, Country2)),
    (\+ (alliance(Country1, Alliance), alliance(Country2, Alliance))),
    Country1 \= Country2.  % Убедиться, что страны разные

at_war(Country) :- at_war(Country, _).

% Правило для определения общего снаряжения всех дивизий
all_division_equipment(Division, EquipmentList) :-
    findall(Equipment, has_equipment(Division, Equipment), EquipmentList).

% Правило для определения снаряжения армии через ее дивизии
all_army_equipment(Army, EquipmentList) :-
    findall(Equipment, (has_division(Army, Division), all_division_equipment(Division, Equipment)), EquipmentList).

% Правило для определения снаряжения страны через ее армии и дивизии
country_equipment(Country, EquipmentList) :-
    findall(Equipment,
        (army(Army, Country),
            all_army_equipment(Army, Equipment)),
        EquipmentList).

% Если страна является мажором и имеет более 10 миллионов населения,
% то она может сформировать альянс (при коммунизме или фашизме min threat 30, иначе 80).
can_form_alliance(Country) :-
    major_country(Country),
    world_tension(Tension),
    total_population(Country, Pop),
    Pop > 10000000,
    (((ideology(Country, fascist); ideology(Country, communist)) -> Tension >= 30; Tension >= 50)).

% Правило для вычисления отношения Country1 к стране Country2
current_relationship(Country1, Country2, Relationship) :-
    ideology(Country1, Ideology1),
    ideology(Country2, Ideology2),
    (relationship(Country1, Country2, Base) -> true; Base = 0),  % Устанавливаем значение по умолчанию для Base

    (alliance(Country1, Alliance), alliance(Country2, Alliance) ->
        AdjustedBase is Base + 100;
        AdjustedBase is Base),

    (puppet(Country1, Country2); puppet(Country2, Country1) ->
        Adjusted1Base is AdjustedBase + 150;
        Adjusted1Base is AdjustedBase),

    (guarantee_independence(Country2, Country1) ->
        Adjusted2Base is Adjusted1Base + 25;
        Adjusted2Base is Adjusted1Base),

    ((Ideology1 \= Ideology2) ->
        Adjusted3Base is Adjusted2Base - 25;
        Adjusted3Base is Adjusted2Base),

    (non_aggression_pact(Country1, Country2) ->
        Adjusted4Base is Adjusted3Base + 40;
        Adjusted4Base is Adjusted3Base),

    (alliance(Country1, Alliance), alliance(Ally, Alliance), at_war(Country2, Ally) ->
        Adjusted5Base is Adjusted4Base - 50;
        Adjusted5Base is Adjusted4Base),

    (alliance_head(_, Country1), alliance_head(_, Country2) ->
        Adjusted6Base is Adjusted5Base - 30;
        Adjusted6Base is Adjusted5Base),

    (at_war(Country1, Country2) ->
        FinalBase is Adjusted6Base - 150;
        FinalBase is Adjusted6Base),

    (claim(Country2, Prov) ->
        (province_of(Country1, Prov) ->
            FinalBase is FinalBase - 70;
            FinalBase is FinalBase);
        FinalBase is FinalBase),

    Relationship = FinalBase.

% Правило для определения возможности создания альянса
% Country1 приглашает в Alliance Country2
can_invite_to_alliance(Country1, Alliance, Country2) :-
    ideology(Country1, Ideology1),
    ideology(Country2, Ideology2),
    current_relationship(Country1, Country2, Relationship),
    world_tension(Tension),
    alliance_head(Alliance, Country1),
    \+alliance(Country2, _),
    (puppet(Country1, Country2) ->
        true;
        (at_war(Country2, _) -> (
            at_war(Country2, Country1) ->
                false;
                Relationship >= 20, ((Ideology1 == Ideology2) -> Tension > 50; Tension > 80)
            ); (
                Ideology1 == Ideology2, Tension > 30; Relationship >= 20, Tension > 30; Relationship >= 0, Tension > 80; Relationship >= 100
            )
        )
    ).

% Country хочет вступить в альянс Alliance
ask_to_join_alliance(Country, Alliance) :-
    alliance_head(Alliance, AllianceHeadCountry),
    ideology(Country, Ideology1),
    ideology(AllianceHeadCountry, Ideology2),
    current_relationship(Country, AllianceHeadCountry, Relationship),
    world_tension(Tension),
    (
        \+alliance(Country, _),
        Ideology1 == Ideology2,
        at_war(Country, _) -> (
            (alliance(Ally, Alliance), at_war(Country, Ally)) -> false;
            Relationship >= 20, Tension > 60
        ); (
            Relationship >= 0, Tension > 20
        )
    ).

% Правило для фабрикации цели войны страной Country1 на страну Country2
can_justify_war_goal(Country1, Country2) :-
    world_tension(Tension),
    ideology(Country1, Ideology1),
    ideology(Country2, Ideology2),
    (\+ puppet(_, Country1)),
    (\+ puppet(Country1, Country2)),
    (\+ (alliance(Country1, Alliance), alliance(Country2, Alliance))),
    % Цель войны может быть фабрикована при высокой напряженности и разных идеологиях
    ((claim(Country1, Prov), province_of(Country2, Prov)); (ideology(Country1, democratic), Ideology1 \= Ideology2); (ideology(Country1, democratic) -> Tension >= 95; Tension >= 75)).

% Правило для объявления войны
can_declare_war(Attacker, Defender) :-
    (\+ non_aggression_pact(Attacker, Defender)),
    (\+ guarantee_independence(Attacker, Defender)),
    (\+ (alliance(Attacker, Alliance), alliance(Defender, Alliance))),

    (puppet(Master, Attacker), at_war(Master, Defender));
    (guarantee_independence(Attacker, Subject), at_war(Subject, Defender));
    (alliance(Attacker, Alliance), alliance(AlliedCountry, Alliance), at_war(AlliedCountry, Defender));
    can_justify_war_goal(Attacker, Defender).

% Если страна имеет врага и находится в состоянии войны,
% то она может призвать своих союзников.
can_call_allies(Country) :-
    at_war(Country, Enemy), alliance(Country, Alliance), (\+ alliance(Enemy, Alliance)).

% Если у страны Country1 есть марионетка,
% то она может использовать ресурсы этой марионетки Country2.
can_use_resources(Country1, Country2) :-
    (puppet(Country1, Country2);
        (current_relationship(Country1, Country2, Relationship),
        (alliance(Country1, Alliance), alliance(Country2, Alliance), Relationship >= 200))).

% Если у страны есть генерал, то она может улучшить свои боевые способности.
can_improve_military(Country) :-
    army(Army, Country), general(Army, _).

% Если у страны есть дивизия, то она может участвовать в войне.
can_participate_in_war(Country) :-
    army(Army, Country),
    has_division(Army, _).


% Может ли Caller призывать к оружию Callee
can_call_to_arms(Caller, Callee) :-
    Caller \= Callee,
    world_tension(Tension),
    at_war(Caller, _),
    (
        puppet(Caller, Callee);
        (alliance_head(Alliance, Caller), alliance(Callee, Alliance));
        (at_defensive_war(Caller, _), guarantee_independence(Callee, Caller));
        ((alliance(Alliance, Caller), alliance(Callee, Alliance)), (at_offencive_war(Caller, _) -> (((\+ideology(Callee, democratic)), Tension >= 60); Tension >= 90); Tension >= 60))
    ).

% Может ли страна Country пройти через пролив/канал
can_cross_the_strait(Country, Strait) :- (
    strait(Strait, Country);
    (
        strait(Strait, StraitOwner),
        current_relationship(Country, StraitOwner, Relationship),
        (\+ at_war(Country, StraitOwner)),
        (\+ (alliance(Country, Alliance), alliance(Ally, Alliance), at_war(Ally, StraitOwner))),
        (
            (alliance(Country, Alliance), alliance(StraitOwner, Alliance));
            (puppet(Country, StraitOwner));
            (ideology(Country, democratic), ideology(StraitOwner, democratic));
            Relationship >= 100
        )
    )
).

is_there_marine_path(X, Z) :-
    X == Z;
    connected(X, Z);
    is_connected(X, Z, []).

is_connected(X, Z, V) :-
    connected(X, Y),
    not(member(Y, V)),
    (Z = Y; is_connected(Y, Z,[X|V])).

is_connected_for_country(Country, X, Z) :- (
    X == Z;
    connected(X, Z);
    (
        connected(X, Y), (
            sea(Y) -> is_there_marine_path(Y, Z);
            (can_cross_the_strait(Country, Y), is_there_marine_path(Y, Z))
        )
    )
).

% Правило для проверки возможности высадки из Country1 в Country2
can_land(Country1, Country2) :-
    province_of(Country1, Prov1),
    province_of(Country2, Prov2),
    has_access_to_sea(Prov1, Sea1),
    has_access_to_sea(Prov2, Sea2),
    is_connected_for_country(Country1, Sea1, Sea2),
    (right_of_passage(Country1, Country2);
     Country1 == Country2;
     puppet(Country1, Country2);
     (alliance(Country1, Alliance), alliance(Country2, Alliance));
     at_war(Country1, Country2)).
