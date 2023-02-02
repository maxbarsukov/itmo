package org.itmo.prog.lab2

import org.itmo.prog.lab2.pokemons.*
import ru.ifmo.se.pokemon.Battle

fun main() {
    val battle = Battle()

    battle.addAlly(Sigilyph("The World", 73))
    battle.addAlly(Wimpod("Killer Queen", 65))
    battle.addAlly(Golisopod("D4C", 81))

    battle.addFoe(Budew("Star Platinum", 56))
    battle.addFoe(Roselia("Silver Chariot", 76))
    battle.addFoe(Roserade("Tusk Act 4", 79))

    battle.go()
}
