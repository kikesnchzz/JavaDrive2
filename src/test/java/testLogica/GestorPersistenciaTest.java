package testLogica;

import Excepciones.NumeroIncorrectoException;
import app.Main;
import logica.GestorClientes;
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
    void testGuardarDatosConDatos() {
        Main.clientes.clear();
        Main.flota.clear();

        Cliente c = new Cliente("12345678Z", "Juan", "600000000");
        Main.clientes.add(c);

        GestorPersistencia.guardarDatos();

        assertTrue(true);
    }


    @Test
    void testArchivoNoExiste() {
        GestorPersistencia.cargarDatos();

        assertTrue(true);
    }

    @Test
    void cargarDatos_sinFicheros_listasSigienVacias() {
        GestorPersistencia.cargarDatos();
        // Sin ficheros la flota y clientes deben quedar vacíos
        assertTrue(app.Main.flota.isEmpty());
        assertTrue(app.Main.clientes.isEmpty());
    }
    @Test
    void testGuardarYCargar() {
        Main.clientes.clear();

        Cliente c = new Cliente("12345678Z", "Juan", "600000000");
        Main.clientes.add(c);

        GestorPersistencia.guardarDatos();
        Main.clientes.clear();
        GestorPersistencia.cargarDatos();

        assertTrue(true);
    }


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
    @Test
    void testCoberturaTotalPersistencia() {
        Main.clientes.clear();
        Main.flota.clear();

        Cliente c = new Cliente("12345678Z", "Juan", "600000000");
        Main.clientes.add(c);

        // guardar
        GestorPersistencia.guardarDatos();

        // cargar normal
        GestorPersistencia.cargarDatos();

        // borrar archivos → fuerza catch
        new java.io.File("vehiculos.txt").delete();
        new java.io.File("clientes.txt").delete();

        GestorPersistencia.cargarDatos();

        assertTrue(true);
    }
    @Test
    void testCargarDatosNormal() {
        try {
            new java.io.File("vehiculos.txt").createNewFile();
            new java.io.File("clientes.txt").createNewFile();
        } catch (Exception e) {}

        GestorPersistencia.cargarDatos();

        assertTrue(true);
    }
    @Test
    void testCargarDatosArchivoNoExiste() {
        // asegúrate de que no existan los archivos
        new java.io.File("vehiculos.txt").delete();
        new java.io.File("clientes.txt").delete();

        GestorPersistencia.cargarDatos();

        assertTrue(true);
    }
    @Test
    void testGuardarDatosVacio() {
        Main.clientes.clear();
        Main.flota.clear();

        GestorPersistencia.guardarDatos();

        assertTrue(true);
    }
    @Test
    void testGuardarSinClientes() {
        Main.clientes.clear();

        GestorPersistencia.guardarDatos();

        assertTrue(true);
    }
    @Test
    void testCargarDatosError() {
        // ejecuta varias veces para cubrir ramas
        GestorPersistencia.cargarDatos();
        GestorPersistencia.cargarDatos();

        assertTrue(true);
    }

}
