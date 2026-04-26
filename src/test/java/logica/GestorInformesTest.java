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

class GestorInformesTest {

    @BeforeEach
    void setUp() {
        Main.flota = new ArrayList<>();
        Main.clientes = new ArrayList<>();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Limpiamos los archivos temporales creados durante los tests
        Files.deleteIfExists(Path.of("vehiculosTest.txt"));
        Files.deleteIfExists(Path.of("clientesTest.txt"));
        // Borramos tickets generados
        Files.list(Path.of("."))
                .filter(p -> p.getFileName().toString().startsWith("ticket_"))
                .forEach(p -> p.toFile().delete());
    }

    // ─────────────────────────────────────────────
    // convierteAVehiculo
    // ─────────────────────────────────────────────

    @Test
    void convierteVehiculo_coche() throws NumeroIncorrectoException {
        String linea = "COCHE;1234ABC;Toyota;Corolla;true;Deportivo;5";
        Vehiculo v = GestorInformes.convierteAVehiculo(linea);

        assertNotNull(v);
        assertInstanceOf(Coche.class, v);
        assertEquals("Toyota", v.getMarca());
        assertEquals("1234ABC", v.getMatricula());
    }

    @Test
    void convierteAVehiculo_furgoneta() throws NumeroIncorrectoException {
        String linea = "FURGONETA;5678XYZ;Ford;Transit;true;true;1000";
        Vehiculo v = GestorInformes.convierteAVehiculo(linea);

        assertNotNull(v);
        assertInstanceOf(Furgoneta.class, v);
        assertEquals("Ford", v.getMarca());
    }

    @Test
    void convierteAVehiculo_lineaInvalida() throws NumeroIncorrectoException {
        String linea = "COCHE;datos;incompletos";
        Vehiculo v = GestorInformes.convierteAVehiculo(linea);

        assertNull(v);
    }

    // ─────────────────────────────────────────────
    // convierteACliente
    // ─────────────────────────────────────────────

    @Test
    void convierteACliente_correcto() {
        String linea = "12345678A;Juan García;600123456";
        Cliente c = GestorInformes.convierteACliente(linea);

        assertNotNull(c);
        assertEquals("12345678A", c.getDni());
        assertEquals("Juan García", c.getNombre());
        assertEquals("600123456", c.getTelefono());
    }

    @Test
    void convierteACliente_lineaInvalida() {
        String linea = "datos;incompletos";
        Cliente c = GestorInformes.convierteACliente(linea);

        assertNull(c);
    }

    // ─────────────────────────────────────────────
    // mostrarVehiculos
    // ─────────────────────────────────────────────

    @Test
    void mostrarVehiculos_coche() throws NumeroIncorrectoException {
        Vehiculo v = new Coche("9999ZZZ", "BMW", "M3", true, Deportivo, 2);
        String resultado = GestorInformes.mostrarVehiculos(v);

        assertTrue(resultado.startsWith("Coche:"));
        assertTrue(resultado.contains("BMW"));
    }

    @Test
    void mostrarVehiculos_furgoneta() throws NumeroIncorrectoException {
        Vehiculo v = new Furgoneta("8888YYY", "Mercedes", "Sprinter", true, true, 800);
        String resultado = GestorInformes.mostrarVehiculos(v);

        assertTrue(resultado.startsWith("Furgoneta:"));
        assertTrue(resultado.contains("Mercedes"));
    }

    // ─────────────────────────────────────────────
    // guardarDatos y cargarDatos (integración)
    // ─────────────────────────────────────────────

    @Test
    void guardarYCargarClientes() throws IOException {
        // Escribimos directamente en el formato correcto
        Files.writeString(Path.of("clientes.txt"), "11111111A;Ana López;611000001\n22222222B;Luis Pérez;622000002\n");

        GestorInformes.cargarDatos();

        assertEquals(2, Main.clientes.size());
        assertEquals("Ana López", Main.clientes.get(0).getNombre());
    }

    @Test
    void guardarYCargarVehiculos() throws IOException, NumeroIncorrectoException {
        Files.writeString(Path.of("vehiculos.txt"),
                "COCHE;1111AAA;Seat;Ibiza;true;Deportivo;5\nFURGONETA;2222BBB;Renault;Kangoo;true;false;3\n");

        GestorInformes.cargarDatos();

        assertEquals(2, Main.flota.size());
        assertEquals("Seat", Main.flota.get(0).getMarca());
    }

    // ─────────────────────────────────────────────
    // exportarTicket
    // ─────────────────────────────────────────────

    @Test
    void exportarTicket_creaArchivo() throws NumeroIncorrectoException {
        Cliente cliente = new Cliente("33333333C", "Carlos Ruiz", "633000003");
        Vehiculo vehiculo = new Coche("3333CCC", "Honda", "Civic", true, Deportivo, 5);
        Reserva reserva = new Reserva(cliente, vehiculo, LocalDate.now(), LocalDate.now().plusDays(3));

        GestorInformes.exportarTicket(reserva);

        File ticket = new File("ticket_" + reserva.getIdReserva() + ".txt");
        assertTrue(ticket.exists());
        assertTrue(ticket.length() > 0);
    }

    @Test
    void exportarTicket_contienesDatosCliente() throws IOException, NumeroIncorrectoException {
        Cliente cliente = new Cliente("44444444D", "María Torres", "644000004");
        Vehiculo vehiculo = new Coche("4444DDD", "Kia", "Ceed", true, Deportivo, 5);
        Reserva reserva = new Reserva(cliente, vehiculo, LocalDate.now(), LocalDate.now().plusDays(2));

        GestorInformes.exportarTicket(reserva);

        String contenido = Files.readString(Path.of("ticket_" + reserva.getIdReserva() + ".txt"));
        assertTrue(contenido.contains("María Torres"));
        assertTrue(contenido.contains("44444444D"));
        assertTrue(contenido.contains("Kia"));
    }
}