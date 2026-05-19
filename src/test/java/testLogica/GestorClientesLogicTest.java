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