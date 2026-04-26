package logica;

import app.Main;
import model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static logica.GestorClientes.buscarCliente;
import static org.junit.jupiter.api.Assertions.*;

class GestorClientesTest {
    static ArrayList<Cliente> clientes;

    @BeforeEach
    void setUp() {
        clientes = new ArrayList<>();
        clientes.add(new Cliente("67890123F","Elena Blanco","655666777"));
        clientes.add(new Cliente("56789012E","Luis Fernandez","644555666"));

        // Asignamos la lista a la clase donde están los métodos
        Main.clientes = clientes;
    }

    @Test
    void testCrearClienteCorrecto() {
        String input = "12345678A\nPedro\n698928573\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GestorClientes.crearCliente();

        Cliente c = GestorClientes.buscarCliente("12345678A");
        assertNotNull(c);
        assertEquals("Pedro", c.getNombre());
    }

    @Test
    void testCrearClienteDuplicado() {
        Main.clientes.add(new Cliente("67890123F", "Antonio", "644555666"));

        String input = "67890123F\nOtro\n644555666\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        GestorClientes.crearCliente();

        // Debe seguir habiendo solo 1 cliente con ese DNI
        long count = Main.clientes.stream()
                .filter(c -> c.getDni().equals("67890123F"))
                .count();

        assertEquals(1, count);
    }

    @Test
    void testBuscarClienteExistente() {
        Cliente c = GestorClientes.buscarCliente("67890123F");
        assertNotNull(c);
        assertEquals("Elena Blanco", c.getNombre());
    }

    @Test
    void testBuscarClienteNoExistente() {
        Cliente c = GestorClientes.buscarCliente("999Z");
        assertNull(c);
    }

@Test
    void testBuscarClienteConEspacios() {
        Cliente c = buscarCliente(" 67890123F ");
        assertNotNull(c);
    }

    @Test
    void testBuscarClienteMinuscula() {
        Cliente c = buscarCliente("67890123f");
        assertNotNull(c);
    }
}
