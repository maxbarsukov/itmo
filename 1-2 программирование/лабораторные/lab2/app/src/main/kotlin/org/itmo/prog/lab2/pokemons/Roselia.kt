package org.itmo.prog.lab2.pokemons

import org.itmo.prog.lab2.attacks.physical.PinMissile

open class Roselia(name: String?, lvl: Int) : Budew(name, lvl) {
    init {
        setStats(50.0, 60.0, 45.0, 100.0, 80.0, 65.0)
        addMove(PinMissile())
    }
}
