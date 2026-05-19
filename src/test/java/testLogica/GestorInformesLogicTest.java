package testLogica;

import logica.GestorInformes;
import model.Cliente;
import model.Coche;
import model.Reserva;
import model.tipoCoche;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

    class GestorInformesLogicTest {

        @Test
        void exportarTicketGeneraArchivo() throws Exception {

            Cliente cliente = new Cliente(
                    "12345678A",
                    "Mario",
                    "677777777"
            );

            Coche coche = new Coche(
                    "7777GGG",
                    "Mercedes",
                    "Clase A",
                    true,
                    tipoCoche.Deportivo,
                    5
            );

            Reserva reserva = new Reserva(
                    cliente,
                    coche,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5)
            );

            GestorInformes.exportarTicket(reserva);

            File archivo = new File(
                    "ticket_" + reserva.getIdReserva() + ".txt"
            );

            assertTrue(archivo.exists());
        }
    }
