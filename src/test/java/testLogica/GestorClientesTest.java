package testLogica;

import logica.GestorClientes;
import model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para GestorClientes.
 * Usa app.Main.clientes directamente (campo estático público).
 */
public class GestorClientesTest {

    @BeforeEach
    void setUp() {
        // Limpiamos la lista compartida antes de cada test
        app.Main.clientes.clear();
    }

    // ─────────────────────────────────────────────
    // buscarCliente
    // ─────────────────────────────────────────────

    @Test
    void buscarCliente_existente_devuelveCliente() {
        Cliente c = new Cliente("12345678A", "Ana García", "612345678");
        app.Main.clientes.add(c);

        Cliente resultado = GestorClientes.buscarCliente("12345678A");

        assertNotNull(resultado);
        assertEquals("12345678A", resultado.getDni());
    }

    @Test
    void buscarCliente_noExistente_devuelveNull() {
        Cliente resultado = GestorClientes.buscarCliente("99999999Z");
        assertNull(resultado);
    }

    @Test
    void buscarCliente_ignoraMayusculas() {
        Cliente c = new Cliente("12345678A", "Ana García", "612345678");
        app.Main.clientes.add(c);

        // Buscar en minúsculas debe encontrarlo igualmente
        Cliente resultado = GestorClientes.buscarCliente("12345678a");
        assertNotNull(resultado);
    }

    @Test
    void buscarCliente_ignoraEspacios() {
        Cliente c = new Cliente("12345678A", "Ana García", "612345678");
        app.Main.clientes.add(c);

        Cliente resultado = GestorClientes.buscarCliente("  12345678A  ");
        assertNotNull(resultado);
    }

    @Test
    void buscarCliente_listaVacia_devuelveNull() {
        // La lista ya está vacía por el setUp
        assertNull(GestorClientes.buscarCliente("12345678A"));
    }

    @Test
    void buscarCliente_variosClientes_encuentraElCorrecto() {
        app.Main.clientes.add(new Cliente("11111111A", "Luis", "611111111"));
        app.Main.clientes.add(new Cliente("22222222B", "Marta", "622222222"));
        app.Main.clientes.add(new Cliente("33333333C", "Pedro", "633333333"));

        Cliente resultado = GestorClientes.buscarCliente("22222222B");

        assertNotNull(resultado);
        assertEquals("Marta", resultado.getNombre());
    }
}
