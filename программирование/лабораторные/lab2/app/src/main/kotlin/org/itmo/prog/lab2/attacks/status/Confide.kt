package org.itmo.prog.lab2.attacks.status

import ru.ifmo.se.pokemon.*

class Confide : StatusMove(Type.NORMAL, 0.0, 0.0) {
    override fun applyOppEffects(pokemon: Pokemon) {
        pokemon.setMod(Stat.SPECIAL_ATTACK, -1)
    }

    override fun describe() = "использует способность Confide"
}
