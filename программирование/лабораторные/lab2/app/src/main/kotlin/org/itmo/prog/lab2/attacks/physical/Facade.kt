package org.itmo.prog.lab2.attacks.physical

import ru.ifmo.se.pokemon.*

class Facade : PhysicalMove(Type.NORMAL, 70.0, 100.0) {
    override fun calcBaseDamage(att: Pokemon, def: Pokemon): Double {
        val damage = super.calcBaseDamage(att, def)

        if (def.condition in arrayOf(Status.BURN, Status.PARALYZE, Status.POISON)) {
            return damage * 2.0
        }

        return damage
    }

    override fun describe() = "использует способность Facade"
}
