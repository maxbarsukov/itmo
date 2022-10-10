package org.itmo.prog.lab2.attacks.status

import ru.ifmo.se.pokemon.*

class DoubleTeam : StatusMove(Type.NORMAL, 0.0, 100.0) {
    override fun applySelfEffects(pokemon: Pokemon) {
        pokemon.setMod(Stat.EVASION, 1)
    }

    override fun describe() = "использует способность Double Team"
}
