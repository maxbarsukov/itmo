package org.itmo.prog.lab2.attacks.special

import ru.ifmo.se.pokemon.*

class MegaDrain : SpecialMove(Type.GRASS, 40.0, 100.0) {
    override fun applySelfEffects(pokemon: Pokemon) {
        pokemon.setMod(Stat.HP, -(pokemon.getStat(Stat.ATTACK).toInt() / 2))
    }

    override fun describe() = "использует способность Mega Drain"
}
