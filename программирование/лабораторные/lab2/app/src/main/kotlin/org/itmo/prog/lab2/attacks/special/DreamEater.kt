package org.itmo.prog.lab2.attacks.special

import ru.ifmo.se.pokemon.*

class DreamEater : SpecialMove(Type.PSYCHIC, 100.0, 100.0) {
    override fun applyOppDamage(pokemon: Pokemon, power: Double) {
        if (pokemon.condition == Status.SLEEP) {
            super.applyOppDamage(pokemon, power)
        }
    }

    override fun applySelfDamage(pokemon: Pokemon, power: Double) {
        pokemon.setMod(Stat.HP, (-(power / 2)).toInt())
    }

    override fun describe() = "использует способность Dream Eater"
}
