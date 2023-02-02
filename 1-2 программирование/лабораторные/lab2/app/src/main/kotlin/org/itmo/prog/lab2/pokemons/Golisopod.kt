package org.itmo.prog.lab2.pokemons

import org.itmo.prog.lab2.attacks.special.DarkPulse

class Golisopod(name: String?, level: Int) : Wimpod(name, level) {
    init {
        setStats(75.0, 125.0, 140.0, 60.0, 90.0, 40.0)
        addMove(DarkPulse())
    }
}
