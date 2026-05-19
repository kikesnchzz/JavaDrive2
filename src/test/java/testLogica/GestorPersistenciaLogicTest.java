// src/test/java/testLogica/GestorPersistenciaLogicTest.java
package testLogica;

import app.Main;
import model.Cliente;
import model.Coche;
import model.tipoCoche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static logica.GestorPersistencia.guardarDatos;
import static org.junit.jupiter.api.Assertions.*;

class GestorPersistenciaLogicTest {

    @BeforeEach
    void setUp() {
        Main.clientes.clear();
        Main.flota.clear();
    }

    @Test
    void guardarDatosGeneraArchivos() throws Exception {

        Cliente cliente = new Cliente(
                "12345678A",
                "Carlos",
                "699999999"
        );

        Coche coche = new Coche(
                "6666FFF",
                "Renault",
                "Clio",
                true,
                tipoCoche.Pequeño,
                5
        );

        Main.clientes.add(cliente);
        Main.flota.add(coche);

        guardarDatos();

        File archivoClientes = new File("clientes.txt");
        File archivoVehiculos = new File("vehiculos.txt");

        assertTrue(archivoClientes.exists());
        assertTrue(archivoVehiculos.exists());
    }
}