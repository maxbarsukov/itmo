package org.itmo.prog.lab2.pokemons

import ru.ifmo.se.pokemon.Pokemon
import ru.ifmo.se.pokemon.Type

import org.itmo.prog.lab2.attacks.special.DarkPulse
import org.itmo.prog.lab2.attacks.special.DreamEater
import org.itmo.prog.lab2.attacks.special.ShadowBall
import org.itmo.prog.lab2.attacks.status.Hypnosis

class Sigilyph(name: String?, lvl: Int) : Pokemon(name, lvl) {
    init {
        setStats(72.0, 58.0, 80.0, 103.0, 80.0, 93.0)
        setType(Type.PSYCHIC)
        setMove(DreamEater(), DarkPulse(), Hypnosis(), ShadowBall())
    }
}
