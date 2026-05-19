// src/test/java/testLogica/GestorClientesCoverageTest.java

package testLogica;

import app.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static logica.GestorClientes.crearCliente;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GestorClientesCoverageTest {

    @BeforeEach
    void setUp() {
        Main.clientes.clear();
    }

    @Test
    void crearClienteCorrectamente() {

        String input = "12345678A\nJuan\n666666666\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        crearCliente();

        assertEquals(1, Main.clientes.size());
    }

    @Test
    void noCrearClienteDuplicado() {

        String input1 = "12345678A\nJuan\n666666666\n";
        System.setIn(new ByteArrayInputStream(input1.getBytes()));
        crearCliente();

        String input2 = "12345678A\nPedro\n611111111\n";
        System.setIn(new ByteArrayInputStream(input2.getBytes()));
        crearCliente();

        assertEquals(1, Main.clientes.size());
    }
}
