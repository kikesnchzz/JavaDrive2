// src/test/java/testLogica/GestorFlotaLogicTest.java
package testLogica;

import Excepciones.NumeroIncorrectoException;
import app.Main;
import model.Coche;
import model.Furgoneta;
import model.Vehiculo;
import model.tipoCoche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static logica.GestorFlota.*;
import static org.junit.jupiter.api.Assertions.*;

class GestorFlotaLogicTest {

    @BeforeEach
    void setUp() {
        Main.flota.clear();
    }

    @Test
    void buscarVehiculoExistente() throws Exception {
        Vehiculo coche = new Coche(
                "1234ABC",
                "Seat",
                "Ibiza",
                true,
                tipoCoche.Pequeño,
                5
        );

        Main.flota.add(coche);

        Vehiculo resultado = buscarVehiculo("1234ABC");

        assertNotNull(resultado);
        assertEquals("Seat", resultado.getMarca());
    }

    @Test
    void buscarVehiculoNoExistente() {
        Vehiculo resultado = buscarVehiculo("0000XXX");

        assertNull(resultado);
    }

    @Test
    void listarVehiculosDisponiblesSoloDevuelveDisponibles() throws Exception {

        Vehiculo cocheDisponible = new Coche(
                "1111AAA",
                "BMW",
                "X1",
                true,
                tipoCoche.Familiar,
                5
        );

        Vehiculo cocheNoDisponible = new Furgoneta(
                "2222BBB",
                "Ford",
                "Transit",
                true,
                true,
                1000
        );

        cocheNoDisponible.setDisponible(false);

        Main.flota.add(cocheDisponible);
        Main.flota.add(cocheNoDisponible);

        ArrayList<Vehiculo> resultado = listarVehiculosDisponibles();

        assertEquals(1, resultado.size());
        assertEquals("1111AAA", resultado.get(0).getMatricula());
    }

    @Test
    void cocheLanzaExcepcionNumeroPlazasIncorrecto() {
        assertThrows(
                NumeroIncorrectoException.class,
                () -> new Coche(
                        "3333CCC",
                        "Audi",
                        "A3",
                        true,
                        tipoCoche.Deportivo,
                        1
                )
        );
    }
}