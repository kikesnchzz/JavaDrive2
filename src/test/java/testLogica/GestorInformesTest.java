package testLogica;

import Excepciones.NumeroIncorrectoException;
import logica.GestorInformes;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para GestorInformes.
 * Se prueban los métodos puros (sin I/O de fichero):
 *   - convierteAVehiculo
 *   - convierteACliente
 *   - mostrarVehiculos
 *
 * exportarTicket y generarInformeXML escriben ficheros reales;
 * se testea que no lanzan excepción con datos válidos.
 */
public class GestorInformesTest {

    @BeforeEach
    void setUp() {
        app.Main.flota.clear();
        app.Main.clientes.clear();
    }

    // ─────────────────────────────────────────────
    // convierteAVehiculo
    // ─────────────────────────────────────────────

    @Test
    void convierteAVehiculo_lineaCoche_devuelveCoche() throws NumeroIncorrectoException {
        String linea = "COCHE;1234ABC;Seat;Ibiza;true;Pequeño;5";
        Vehiculo v = GestorInformes.convierteAVehiculo(linea);

        assertNotNull(v);
        assertInstanceOf(Coche.class, v);
        assertEquals("1234ABC", v.getMatricula());
        assertEquals("Seat", v.getMarca());
        assertEquals("Ibiza", v.getModelo());
        assertTrue(v.isDisponible());
    }

    @Test
    void convierteAVehiculo_lineaCoche_tipoYPlazasCorrectos() throws NumeroIncorrectoException {
        String linea = "COCHE;5678DEF;Toyota;Corolla;true;Familiar;5";
        Coche c = (Coche) GestorInformes.convierteAVehiculo(linea);

        assertEquals(tipoCoche.Familiar, c.getTipo());
        assertEquals(5, c.getPlazas());
    }

    @Test
    void convierteAVehiculo_lineaFurgoneta_devuelveFurgoneta() throws NumeroIncorrectoException {
        String linea = "FURGONETA;9999ZZZ;Ford;Transit;true;true;1000";
        Vehiculo v = GestorInformes.convierteAVehiculo(linea);

        assertNotNull(v);
        assertInstanceOf(Furgoneta.class, v);
    }

    @Test
    void convierteAVehiculo_lineaFurgoneta_datosCorrectos() throws NumeroIncorrectoException {
        String linea = "FURGONETA;8888YYY;Mercedes;Sprinter;false;false;12";
        Furgoneta f = (Furgoneta) GestorInformes.convierteAVehiculo(linea);

        assertEquals("8888YYY", f.getMatricula());
        assertFalse(f.isEsDeCarga());
        assertEquals(12, f.getCapacidad());
    }

    @Test
    void convierteAVehiculo_formatoInvalido_devuelveNull() throws NumeroIncorrectoException {
        // Solo 3 partes → no cumple longitud 7
        String linea = "COCHE;1234ABC";
        Vehiculo v = GestorInformes.convierteAVehiculo(linea);

        assertNull(v);
    }

    @Test
    void convierteAVehiculo_lineaVacia_devuelveNull() throws NumeroIncorrectoException {
        Vehiculo v = GestorInformes.convierteAVehiculo(";;;");
        assertNull(v);
    }

    // ─────────────────────────────────────────────
    // convierteACliente
    // ─────────────────────────────────────────────

    @Test
    void convierteACliente_lineaValida_devuelveCliente() {
        String linea = "12345678A;Ana García;612345678";
        Cliente c = GestorInformes.convierteACliente(linea);

        assertNotNull(c);
        assertEquals("12345678A", c.getDni());
        assertEquals("Ana García", c.getNombre());
        assertEquals("612345678", c.getTelefono());
    }

    @Test
    void convierteACliente_formatoInvalido_devuelveNull() {
        // Solo 2 partes
        String linea = "12345678A;Ana García";
        Cliente c = GestorInformes.convierteACliente(linea);

        assertNull(c);
    }

    @Test
    void convierteACliente_lineaVacia_devuelveNull() {
        Cliente c = GestorInformes.convierteACliente(";;");
        assertNull(c);
    }

    // ─────────────────────────────────────────────
    // mostrarVehiculos
    // ─────────────────────────────────────────────

    @Test
    void mostrarVehiculos_coche_contieneEtiquetaCoche() throws NumeroIncorrectoException {
        Coche c = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5);
        String resultado = GestorInformes.mostrarVehiculos(c);

        assertTrue(resultado.startsWith("Coche: "));
    }

    @Test
    void mostrarVehiculos_furgoneta_contieneEtiquetaFurgoneta() {
        Furgoneta f = new Furgoneta("9999ZZZ", "Ford", "Transit", true, true, 800);
        String resultado = GestorInformes.mostrarVehiculos(f);

        assertTrue(resultado.startsWith("Furgoneta;"));
    }

    @Test
    void mostrarVehiculos_coche_incluyeDetalles() throws NumeroIncorrectoException {
        Coche c = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Deportivo, 4);
        String resultado = GestorInformes.mostrarVehiculos(c);

        // obtenerDetalles de Coche devuelve "Coche: Deportivo, 4 plazas"
        assertTrue(resultado.contains("Deportivo"));
        assertTrue(resultado.contains("4 plazas"));
    }

    @Test
    void mostrarVehiculos_furgoneta_incluyeDetalles() {
        Furgoneta f = new Furgoneta("9999ZZZ", "Ford", "Transit", true, false, 9);
        String resultado = GestorInformes.mostrarVehiculos(f);

        assertTrue(resultado.contains("Pasajeros"));
        assertTrue(resultado.contains("9"));
    }

    // ─────────────────────────────────────────────
    // exportarTicket  (smoke test — solo verifica que no lanza)
    // ─────────────────────────────────────────────

    @Test
    void exportarTicket_reservaValida_noLanzaExcepcion() throws NumeroIncorrectoException {
        Cliente cliente   = new Cliente("12345678A", "Ana García", "612345678");
        Coche vehiculo    = new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5);
        Reserva reserva   = new Reserva(cliente, vehiculo,
                LocalDate.now(), LocalDate.now().plusDays(3));

        assertDoesNotThrow(() -> GestorInformes.exportarTicket(reserva));
    }
}
