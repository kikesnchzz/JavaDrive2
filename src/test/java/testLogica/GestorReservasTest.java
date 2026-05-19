package testLogica;

import Excepciones.NumeroIncorrectoException;
import logica.GestorReservas;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para GestorReservas.
 */
public class GestorReservasTest {

    @BeforeEach
    void setUp() {
        app.Main.flota.clear();
        app.Main.clientes.clear();
    }

    // ─────────────────────────────────────────────
    // realizarReserva
    // ─────────────────────────────────────────────

    @Test
    void realizarReserva_vehiculoPasaANoDisponible() throws NumeroIncorrectoException {
        Cliente cliente = new Cliente("12345678A", "Ana García", "612345678");
        Coche vehiculo  = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5);

        assertTrue(vehiculo.isDisponible(), "El vehículo debe empezar disponible");

        GestorReservas.realizarReserva(cliente, vehiculo,
                LocalDate.now(), LocalDate.now().plusDays(2));

        assertFalse(vehiculo.isDisponible(), "Tras la reserva el vehículo debe estar no disponible");
    }

    @Test
    void realizarReserva_creaTicketSinExcepcion() throws NumeroIncorrectoException {
        Cliente cliente = new Cliente("12345678A", "Ana García", "612345678");
        Coche vehiculo  = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5);

        // No debe lanzar ninguna excepción
        assertDoesNotThrow(() ->
                GestorReservas.realizarReserva(cliente, vehiculo,
                        LocalDate.now(), LocalDate.now().plusDays(5))
        );
    }

    @Test
    void realizarReserva_conFurgoneta_vehiculoPasaANoDisponible() {
        Cliente cliente  = new Cliente("12345678A", "Ana García", "612345678");
        Furgoneta furgo  = new Furgoneta("9999ZZZ", "Ford", "Transit", true, true, 800);

        GestorReservas.realizarReserva(cliente, furgo,
                LocalDate.now(), LocalDate.now().plusDays(1));

        assertFalse(furgo.isDisponible());
    }

    @Test
    void realizarReserva_mismaFechaInicioYFin_noLanzaExcepcion() throws NumeroIncorrectoException {
        Cliente cliente = new Cliente("12345678A", "Ana García", "612345678");
        Coche vehiculo  = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Familiar, 5);
        LocalDate hoy   = LocalDate.now();

        assertDoesNotThrow(() ->
                GestorReservas.realizarReserva(cliente, vehiculo, hoy, hoy)
        );
    }
}
