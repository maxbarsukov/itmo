package org.itmo.prog.lab2.pokemons

import ru.ifmo.se.pokemon.Pokemon
import ru.ifmo.se.pokemon.Type

import org.itmo.prog.lab2.attacks.physical.Facade
import org.itmo.prog.lab2.attacks.special.StruggleBug
import org.itmo.prog.lab2.attacks.status.Confide

open class Wimpod(name: String?, level: Int) : Pokemon(name, level) {
    init {
        setStats(25.0, 35.0, 40.0, 20.0, 30.0, 80.0)
        setType(Type.BUG, Type.WATER)
        setMove(StruggleBug(), Facade(), Confide())
    }
}
