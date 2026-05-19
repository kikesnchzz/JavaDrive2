package testLogica;

import Excepciones.NumeroIncorrectoException;
import logica.GestorPersistencia;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GestorPersistenciaTest {

    @BeforeEach
    void setUp() {
        // Limpiar listas antes de cada test
        app.Main.flota.clear();
        app.Main.clientes.clear();
    }



    // ─────────────────────────────────────────────
    // guardarDatos — smoke tests
    // ─────────────────────────────────────────────

    @Test
    void guardarDatos_flotaVacia_noLanzaExcepcion() {
        // flota y clientes vacíos → crea ficheros vacíos, no debe lanzar
        assertDoesNotThrow(() -> GestorPersistencia.guardarDatos());
    }

    @Test
    void guardarDatos_conVehiculos_noLanzaExcepcion() throws NumeroIncorrectoException {
        app.Main.flota.add(new Coche("1234ABC", "Seat", "Ibiza", true, tipoCoche.Pequeño, 5));
        app.Main.flota.add(new Furgoneta("9999ZZZ", "Ford", "Transit", true, true, 800));

        assertDoesNotThrow(() -> GestorPersistencia.guardarDatos());
    }

    @Test
    void guardarDatos_conClientes_noLanzaExcepcion() {
        app.Main.clientes.add(new Cliente("12345678A", "Ana García", "612345678"));
        app.Main.clientes.add(new Cliente("87654321B", "Luis Pérez", "699999999"));

        assertDoesNotThrow(() -> GestorPersistencia.guardarDatos());
    }

    // ─────────────────────────────────────────────
    // cargarDatos — cuando los ficheros no existen
    // ─────────────────────────────────────────────

    @Test
    void cargarDatos_sinFicheros_noLanzaExcepcion() {
        // Si vehiculos.txt / clientes.txt no existen se captura FileNotFoundException internamente
        // El método no debe propagar nada
        assertDoesNotThrow(() -> GestorPersistencia.cargarDatos());
    }

    @Test
    void cargarDatos_sinFicheros_listasSigienVacias() {
        GestorPersistencia.cargarDatos();
        // Sin ficheros la flota y clientes deben quedar vacíos
        assertTrue(app.Main.flota.isEmpty());
        assertTrue(app.Main.clientes.isEmpty());
    }

    // ─────────────────────────────────────────────
    // guardar → cargar  (round-trip)
    // ─────────────────────────────────────────────


    @Test
    void guardarYCargar_vehiculos_seRecuperanCorrectamente() throws Exception {
        // 1. Preparar datos en el formato que convierteAVehiculo espera
        String linea1 = "COCHE;1234ABC;Seat;Ibiza;true;Pequeño;5";
        String linea2 = "FURGONETA;9999ZZZ;Ford;Transit;true;true;800";

        // Escribimos directamente el fichero con el formato correcto
        Files.write(Path.of("vehiculos.txt"), java.util.List.of(linea1, linea2));

        // 2. Limpiar memoria y volver a cargar desde fichero
        app.Main.flota.clear();
        assertDoesNotThrow(() -> GestorPersistencia.cargarDatos());

        // 3. Verificar que se recuperaron los 2 vehículos
        assertEquals(2, app.Main.flota.size());

        // Limpiar fichero generado
        Files.deleteIfExists(Path.of("vehiculos.txt"));
    }
}
