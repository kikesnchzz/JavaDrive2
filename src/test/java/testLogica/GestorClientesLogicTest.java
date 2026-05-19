// src/test/java/testLogica/GestorClientesLogicTest.java
package testLogica;

import app.Main;
import model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static logica.GestorClientes.buscarCliente;
import static logica.GestorClientes.crearCliente;
import static org.junit.jupiter.api.Assertions.*;

class GestorClientesLogicTest {

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

    @Test
    void buscarClienteExistente() throws Exception {
        Cliente cliente = new Cliente("12345678A", "Enrique", "666666666");
        Main.clientes.add(cliente);

        Cliente resultado = buscarCliente("12345678A");

        assertNotNull(resultado);
        assertEquals("Enrique", resultado.getNombre());
    }

    @Test
    void buscarClienteNoExistente() {
        Cliente resultado = buscarCliente("00000000X");

        assertNull(resultado);
    }

    @Test
    void buscarClienteIgnoraEspaciosYMayusculas() throws Exception {
        Cliente cliente = new Cliente("12345678A", "Juan", "611111111");
        Main.clientes.add(cliente);

        Cliente resultado = buscarCliente(" 12345678a ");

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }
}