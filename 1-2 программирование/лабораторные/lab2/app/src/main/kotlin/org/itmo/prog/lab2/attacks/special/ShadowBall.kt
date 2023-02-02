package org.itmo.prog.lab2.attacks.special

import ru.ifmo.se.pokemon.*

class ShadowBall : SpecialMove(Type.GHOST, 80.0, 100.0) {
    override fun applyOppEffects(pokemon: Pokemon) {
        if (Math.random() < 0.2) {
            pokemon.setMod(Stat.SPECIAL_DEFENSE,  -1)
        }
    }
}
