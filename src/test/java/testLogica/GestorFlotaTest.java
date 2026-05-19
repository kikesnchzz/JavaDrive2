package testLogica;

import Excepciones.NumeroIncorrectoException;
import logica.GestorFlota;
import model.Coche;
import model.Furgoneta;
import model.Vehiculo;
import model.tipoCoche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para GestorFlota.
 */
public class GestorFlotaTest {

    @BeforeEach
    void setUp() {
        app.Main.flota.clear();
    }

    // ─────────────────────────────────────────────
    // buscarVehiculo
    // ─────────────────────────────────────────────

    @Test
    void buscarVehiculo_existente_devuelveVehiculo() throws NumeroIncorrectoException {
        Coche c = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5);
        app.Main.flota.add(c);

        Vehiculo resultado = GestorFlota.buscarVehiculo("1234ABC");

        assertNotNull(resultado);
        assertEquals("1234ABC", resultado.getMatricula());
    }

    @Test
    void buscarVehiculo_noExistente_devuelveNull() {
        Vehiculo resultado = GestorFlota.buscarVehiculo("9999ZZZ");
        assertNull(resultado);
    }

    @Test
    void buscarVehiculo_listaVacia_devuelveNull() {
        assertNull(GestorFlota.buscarVehiculo("1234ABC"));
    }

    @Test
    void buscarVehiculo_variosVehiculos_encuentraElCorrecto() throws NumeroIncorrectoException {
        app.Main.flota.add(new Coche("1111AAA", "Seat",   "Ibiza",  true, tipoCoche.Pequeño,   5));
        app.Main.flota.add(new Coche("2222BBB", "Toyota", "Corolla",true, tipoCoche.Familiar,  5));
        app.Main.flota.add(new Furgoneta("3333CCC", "Ford", "Transit", true, true, 1000));

        Vehiculo resultado = GestorFlota.buscarVehiculo("2222BBB");

        assertNotNull(resultado);
        assertEquals("Toyota", resultado.getMarca());
    }

    @Test
    void buscarVehiculo_furgoneta_devuelveFurgoneta() {
        Furgoneta f = new Furgoneta("4444DDD", "Ford", "Transit", true, true, 800);
        app.Main.flota.add(f);

        Vehiculo resultado = GestorFlota.buscarVehiculo("4444DDD");

        assertNotNull(resultado);
        assertInstanceOf(Furgoneta.class, resultado);
    }

    // ─────────────────────────────────────────────
    // listarVehiculosDisponibles
    // ─────────────────────────────────────────────

    @Test
    void listarVehiculosDisponibles_todosDisponibles_devuelveTodos() throws NumeroIncorrectoException {
        app.Main.flota.add(new Coche("1111AAA", "Seat",   "Ibiza",  true, tipoCoche.Pequeño,  5));
        app.Main.flota.add(new Coche("2222BBB", "Toyota", "Corolla",true, tipoCoche.Familiar, 5));

        ArrayList<Vehiculo> disponibles = GestorFlota.listarVehiculosDisponibles();

        assertEquals(2, disponibles.size());
    }

    @Test
    void listarVehiculosDisponibles_ninguno_devuelveListaVacia() throws NumeroIncorrectoException {
        Coche c = new Coche("1111AAA", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5);
        c.setDisponible(false);
        app.Main.flota.add(c);

        ArrayList<Vehiculo> disponibles = GestorFlota.listarVehiculosDisponibles();

        assertTrue(disponibles.isEmpty());
    }

    @Test
    void listarVehiculosDisponibles_mixto_devuelveSoloDisponibles() throws NumeroIncorrectoException {
        Coche disponible = new Coche("1111AAA", "Seat",   "Ibiza",  true, tipoCoche.Pequeño, 5);
        Coche ocupado    = new Coche("2222BBB", "Toyota", "Corolla",true, tipoCoche.Familiar,5);
        ocupado.setDisponible(false);

        app.Main.flota.add(disponible);
        app.Main.flota.add(ocupado);

        ArrayList<Vehiculo> disponibles = GestorFlota.listarVehiculosDisponibles();

        assertEquals(1, disponibles.size());
        assertEquals("1111AAA", disponibles.get(0).getMatricula());
    }

    @Test
    void listarVehiculosDisponibles_listaVacia_devuelveListaVacia() {
        ArrayList<Vehiculo> disponibles = GestorFlota.listarVehiculosDisponibles();
        assertNotNull(disponibles);
        assertTrue(disponibles.isEmpty());
    }

    @Test
    void listarVehiculosDisponibles_conFurgoneta_incluyeFurgoneta() {
        Furgoneta f = new Furgoneta("5555EEE", "Ford", "Transit", true, false, 8);
        app.Main.flota.add(f);

        ArrayList<Vehiculo> disponibles = GestorFlota.listarVehiculosDisponibles();

        assertEquals(1, disponibles.size());
        assertInstanceOf(Furgoneta.class, disponibles.get(0));
    }
}
