package org.itmo.prog.lab2.attacks.special

import ru.ifmo.se.pokemon.*

class DarkPulse : SpecialMove(Type.DARK, 80.0, 100.0) {
    override fun applyOppEffects(pokemon: Pokemon) {
        if (Math.random() < 0.2) {
            Effect.flinch(pokemon)
        }
    }

    override fun describe() = "использует способность Dark Pulse"
}
