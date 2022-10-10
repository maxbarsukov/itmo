package org.itmo.prog.lab2.pokemons

import ru.ifmo.se.pokemon.Pokemon
import ru.ifmo.se.pokemon.Type

import org.itmo.prog.lab2.attacks.special.MegaDrain
import org.itmo.prog.lab2.attacks.physical.Facade

open class Budew(name: String?, lvl: Int) : Pokemon(name, lvl) {
    init {
        setStats(40.0, 30.0, 35.0, 50.0, 70.0, 55.0)
        setType(Type.GRASS, Type.POISON)
        setMove(MegaDrain(), Facade())
    }
}
