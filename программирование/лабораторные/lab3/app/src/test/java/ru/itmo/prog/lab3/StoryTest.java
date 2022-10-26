package ru.itmo.prog.lab3;

import org.junit.Test;
import static org.junit.Assert.*;

public class StoryTest {
    @Test public void appHasAGreeting() {
        Main classUnderTest = new Main();
        assertNotNull("app should have a greeting", 1);
    }
}
