package org.itmo.prog.lab2.pokemons

import org.itmo.prog.lab2.attacks.status.DoubleTeam

class Roserade(name: String?, lvl: Int) : Roselia(name, lvl) {
    init {
        setStats(60.0, 70.0, 65.0, 125.0, 105.0, 90.0)
        addMove(DoubleTeam())
    }
}
