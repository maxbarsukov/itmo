package org.itmo.prog.lab2.attacks.physical

import ru.ifmo.se.pokemon.*

class PinMissile : PhysicalMove(Type.BUG, 25.0, 95.0) {
    override fun applyOppEffects(pokemon: Pokemon) {
        val probability = Math.random()

        val factor = when {
            probability < 0.375 -> 2
            probability < 0.75  -> 3
            probability < 0.875 -> 4
            else                -> 5
        }

        pokemon.addEffect(Effect().turns(factor).stat(Stat.ATTACK, 25 * factor))
    }

    override fun describe() = "использует способность Pin Missile"
}
