package testLogica;

import Excepciones.NumeroIncorrectoException;
import logica.GestorFlota;
import logica.GestorReservas;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static model.tipoCoche.Pequeño;
import static org.junit.jupiter.api.Assertions.*;

public class GestorReservasTest {

    @BeforeEach
    void setUp() {
        app.Main.flota.clear();
        app.Main.clientes.clear();
    }
    @Test
    void realizarReserva_vehiculoPasaANoDisponible() throws NumeroIncorrectoException {
        Cliente cliente = new Cliente("12345678A", "Ana García", "612345678");
        Coche vehiculo  = new Coche("1234ABC", "Seat", "Ibiza", true, Pequeño, 5);

        assertTrue(vehiculo.isDisponible(), "El vehículo debe empezar disponible");

        GestorReservas.realizarReserva(cliente, vehiculo,
                LocalDate.now(), LocalDate.now().plusDays(2));

        assertFalse(vehiculo.isDisponible(), "Tras la reserva el vehículo debe estar no disponible");
    }
    @Test
    void testReservaVehiculoNoDisponible() throws NumeroIncorrectoException {
        Vehiculo v = new Coche("1111AAA","Seat", "Ibiza", false, Pequeño, 5);
        Cliente c = new Cliente("12345678A","Juan",  "612345678");

        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusDays(2);

        GestorReservas.realizarReserva(c, v, inicio, fin);

        // Verificas que NO cambia disponibilidad
        assertFalse(v.isDisponible());
    }
    @Test
    void testBuscarEnListaVacia() {
        GestorFlota gf = new GestorFlota();

        Vehiculo v = gf.buscarVehiculo("1234ABC");

        assertNull(v);
    }

    @Test
    void realizarReserva_creaTicketSinExcepcion() throws NumeroIncorrectoException {
        Cliente cliente = new Cliente("12345678A", "Ana García", "612345678");
        Coche vehiculo  = new Coche("1234ABC", "Seat", "Ibiza", true, Pequeño, 5);

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
