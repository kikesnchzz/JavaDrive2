
package logica;

import Excepciones.NumeroIncorrectoException;
import app.Main;
import model.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static model.tipoCoche.Deportivo;
import static org.junit.jupiter.api.Assertions.*;

    class GestorReservasTest {

        @BeforeEach
        void setUp() throws Exception {
            Main.flota = new ArrayList<>();
            Main.clientes = new ArrayList<>();

            // Reseteamos el contador de reservas
            java.lang.reflect.Field campo = Reserva.class.getDeclaredField("contadorId");
            campo.setAccessible(true);
            campo.setInt(null, 0);
        }

        @AfterEach
        void tearDown() throws IOException {
            // Cerramos streams estáticos de GestorInformes
            if (GestorInformes.br != null) { GestorInformes.br.close(); GestorInformes.br = null; }
            if (GestorInformes.pw != null) { GestorInformes.pw.close(); GestorInformes.pw = null; }
            if (GestorInformes.bw != null) { GestorInformes.bw.close(); GestorInformes.bw = null; }
            if (GestorInformes.fw != null) { GestorInformes.fw.close(); GestorInformes.fw = null; }

            // Borramos tickets generados
            Files.list(Path.of("."))
                    .filter(p -> p.getFileName().toString().startsWith("ticket_"))
                    .forEach(p -> p.toFile().delete());
        }

        @Test
        void realizarReserva_vehiculoPasaANoDisponible() throws NumeroIncorrectoException {
            Cliente cliente = new Cliente("11111111A", "Ana López", "611000001");
            Vehiculo vehiculo = new Coche("1234ABC", "Toyota", "Corolla", true, Deportivo, 5);

            assertTrue(vehiculo.isDisponible()); // empieza disponible

            GestorReservas.realizarReserva(cliente, vehiculo, LocalDate.now(), LocalDate.now().plusDays(3));

            assertFalse(vehiculo.isDisponible()); // después de reservar, no disponible
        }

        @Test
        void realizarReserva_creaTicket() throws NumeroIncorrectoException {
            Cliente cliente = new Cliente("22222222B", "Luis Pérez", "622000002");
            Vehiculo vehiculo = new Coche("5678DEF", "Honda", "Civic", true, Deportivo, 5);

            GestorReservas.realizarReserva(cliente, vehiculo, LocalDate.now(), LocalDate.now().plusDays(5));

            // Comprobamos que se ha creado algún ticket
            File[] tickets = new File(".").listFiles(f -> f.getName().startsWith("ticket_"));
            assertNotNull(tickets);
            assertTrue(tickets.length > 0);
        }

        @Test
        void realizarReserva_ticketContienesDatosCorrectos() throws IOException, NumeroIncorrectoException {
            Cliente cliente = new Cliente("33333333C", "María Torres", "633000003");
            Vehiculo vehiculo = new Furgoneta("9999ZZZ", "Ford", "Transit", true, true, 800);

            GestorReservas.realizarReserva(cliente, vehiculo, LocalDate.now(), LocalDate.now().plusDays(2));

            File[] tickets = new File(".").listFiles(f -> f.getName().startsWith("ticket_"));
            assertNotNull(tickets);
            assertTrue(tickets.length > 0);

            String contenido = Files.readString(tickets[0].toPath());
            assertTrue(contenido.contains("María Torres"));
            assertTrue(contenido.contains("33333333C"));
            assertTrue(contenido.contains("Ford"));
            assertTrue(contenido.contains("Transit"));
        }

        @Test
        void realizarReserva_conFurgoneta() throws NumeroIncorrectoException {
            Cliente cliente = new Cliente("44444444D", "Carlos Ruiz", "644000004");
            Vehiculo vehiculo = new Furgoneta("7777GHI", "Mercedes", "Sprinter", true, false, 3);

            assertTrue(vehiculo.isDisponible());

            GestorReservas.realizarReserva(cliente, vehiculo, LocalDate.now(), LocalDate.now().plusDays(7));

            assertFalse(vehiculo.isDisponible());
        }
    }
