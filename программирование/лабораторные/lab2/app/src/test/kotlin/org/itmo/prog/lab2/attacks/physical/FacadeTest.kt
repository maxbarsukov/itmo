package org.itmo.prog.lab2.attacks.physical

import kotlin.test.Test
import kotlin.test.*

class FacadeTest {
    @Test fun facadeHasAPriority() {
        assertTrue(Facade().priority == 0, "Facade priority must be 0")
    }
}
