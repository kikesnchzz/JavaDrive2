// src/test/java/testLogica/GestorReservasLogicTest.java
package testLogica;

import model.Cliente;
import model.Coche;
import model.Reserva;
import model.tipoCoche;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static logica.GestorReservas.realizarReserva;
import static org.junit.jupiter.api.Assertions.*;

class GestorReservasLogicTest {

    @Test
    void realizarReservaMarcaVehiculoNoDisponibleYGeneraTicket() throws Exception {

        Cliente cliente = new Cliente(
                "12345678A",
                "Enrique",
                "666666666"
        );

        Coche coche = new Coche(
                "4444DDD",
                "Seat",
                "León",
                true,
                tipoCoche.Familiar,
                5
        );

        LocalDate inicio = LocalDate.now();
        LocalDate fin = inicio.plusDays(3);

        realizarReserva(cliente, coche, inicio, fin);

        assertFalse(coche.isDisponible());

        File[] tickets = new File(".").listFiles(
                (dir, name) -> name.startsWith("ticket_")
        );

        assertNotNull(tickets);
        assertTrue(tickets.length > 0);
    }

    @Test
    void reservaSeCreaCorrectamente() throws Exception {

        Cliente cliente = new Cliente(
                "87654321B",
                "Laura",
                "611111111"
        );

        Coche coche = new Coche(
                "5555EEE",
                "Toyota",
                "Corolla",
                true,
                tipoCoche.Pequeño,
                5
        );

        Reserva reserva = new Reserva(
                cliente,
                coche,
                LocalDate.now(),
                LocalDate.now().plusDays(2)
        );

        assertEquals(cliente, reserva.getCliente());
        assertEquals(coche, reserva.getVehiculo());
    }
}