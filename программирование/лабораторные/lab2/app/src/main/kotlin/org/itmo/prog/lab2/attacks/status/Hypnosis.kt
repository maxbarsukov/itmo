package org.itmo.prog.lab2.attacks.status

import ru.ifmo.se.pokemon.*

class Hypnosis : StatusMove(Type.PSYCHIC, 0.0, 60.0) {
    override fun applyOppEffects(pokemon: Pokemon) {
        pokemon.setMod(Stat.SPEED, -3)
        Effect.sleep(pokemon)
    }

    override fun describe() = "использует способность Hypnosis"
}
