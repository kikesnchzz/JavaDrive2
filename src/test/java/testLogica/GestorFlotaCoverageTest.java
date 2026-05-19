// src/test/java/testLogica/GestorFlotaCoverageTest.java

package testLogica;

import Excepciones.NumeroIncorrectoException;
import app.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static logica.GestorFlota.crearVehiculo;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GestorFlotaCoverageTest {

    @BeforeEach
    void setUp() {
        Main.flota.clear();
    }

    @Test
    void crearCocheCorrectamente() throws NumeroIncorrectoException {

        String input = String.join("\n",
                "1111AAA",
                "Seat",
                "Ibiza",
                "1",
                "Pequeño",
                "5"
        ) + "\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        crearVehiculo();

        assertEquals(1, Main.flota.size());
    }

    @Test
    void crearFurgonetaCorrectamente() throws NumeroIncorrectoException {

        String input = String.join("\n",
                "2222BBB",
                "Ford",
                "Transit",
                "2",
                "true",
                "1200"
        ) + "\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        crearVehiculo();

        assertEquals(1, Main.flota.size());
    }

    @Test
    void opcionMenuIncorrectaNoAñadeVehiculo() throws NumeroIncorrectoException {

        String input = String.join("\n",
                "3333CCC",
                "BMW",
                "X1",
                "9"
        ) + "\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        crearVehiculo();

        assertEquals(0, Main.flota.size());
    }
}