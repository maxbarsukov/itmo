package org.itmo.prog.lab2.attacks.special

import ru.ifmo.se.pokemon.*

class StruggleBug : SpecialMove(Type.BUG, 50.0, 100.0) {
    override fun applyOppEffects(pokemon: Pokemon) {
        pokemon.setMod(Stat.SPECIAL_ATTACK, -1)
    }

    override fun describe() = "использует способность Struggle Bug"
}
