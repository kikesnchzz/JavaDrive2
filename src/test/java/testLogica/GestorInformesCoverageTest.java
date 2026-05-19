// src/test/java/testLogica/GestorInformesCoverageTest.java

package testLogica;

import app.Main;
import logica.GestorInformes;
import model.Cliente;
import model.Coche;
import model.Furgoneta;
import model.Reserva;
import model.Vehiculo;
import model.tipoCoche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GestorInformesCoverageTest {

    @BeforeEach
    void setUp() {
        Main.clientes.clear();
        Main.flota.clear();
    }

    @Test
    void convierteLineaACoche() throws Exception {

        Vehiculo v = GestorInformes.convierteAVehiculo(
                "COCHE;1111AAA;Seat;Ibiza;true;Pequeño;5"
        );

        assertNotNull(v);
        assertTrue(v instanceof Coche);
    }

    @Test
    void convierteLineaAFurgoneta() throws Exception {

        Vehiculo v = GestorInformes.convierteAVehiculo(
                "FURGONETA;2222BBB;Ford;Transit;true;true;1000"
        );

        assertNotNull(v);
        assertTrue(v instanceof Furgoneta);
    }

    @Test
    void convertirVehiculoInvalidoDevuelveNull() throws Exception {

        Vehiculo v = GestorInformes.convierteAVehiculo("dato_mal");

        assertNull(v);
    }

    @Test
    void convierteClienteCorrectamente() {

        Cliente c = GestorInformes.convierteACliente(
                "12345678A;Juan;666666666"
        );

        assertNotNull(c);
        assertEquals("Juan", c.getNombre());
    }

    @Test
    void mostrarVehiculosCoche() throws Exception {

        Coche coche = new Coche(
                "1111AAA",
                "Seat",
                "Ibiza",
                true,
                tipoCoche.Pequeño,
                5
        );

        String texto = GestorInformes.mostrarVehiculos(coche);

        assertTrue(texto.contains("Coche"));
    }

    @Test
    void mostrarVehiculosFurgoneta() throws Exception {

        Furgoneta furgo = new Furgoneta(
                "2222BBB",
                "Ford",
                "Transit",
                true,
                true,
                1200
        );

        String texto = GestorInformes.mostrarVehiculos(furgo);

        assertTrue(texto.contains("Furgoneta"));
    }

    @Test
    void leerDocumentoVehiculos() throws Exception {

        FileWriter fw = new FileWriter("vehiculos.txt");
        fw.write("COCHE;1111AAA;Seat;Ibiza;true;Pequeño;5\n");
        fw.close();

        GestorInformes.leerDocumento("vehiculos.txt");

        assertEquals(1, Main.flota.size());
    }

    @Test
    void leerDocumentoClientes() throws Exception {

        FileWriter fw = new FileWriter("clientes.txt");
        fw.write("12345678A;Juan;666666666\n");
        fw.close();

        GestorInformes.leerDocumento("clientes.txt");

        assertEquals(1, Main.clientes.size());
    }

    @Test
    void generarInformeXMLCorrectamente() throws Exception {

        Main.clientes.add(
                new Cliente("12345678A", "Juan", "666666666")
        );

        Main.flota.add(
                new Coche(
                        "1111AAA",
                        "Seat",
                        "Ibiza",
                        true,
                        tipoCoche.Pequeño,
                        5
                )
        );

        GestorInformes.generarInformeXML();

        File f = new File("reporte_completo.xml");

        assertTrue(f.exists());
    }

    @Test
    void exportarTicketGeneraArchivo() throws Exception {

        Cliente cliente = new Cliente(
                "12345678A",
                "Mario",
                "677777777"
        );

        Coche coche = new Coche(
                "7777GGG",
                "Mercedes",
                "Clase A",
                true,
                tipoCoche.Deportivo,
                5
        );

        Reserva reserva = new Reserva(
                cliente,
                coche,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        GestorInformes.exportarTicket(reserva);

        File archivo = new File(
                "ticket_" + reserva.getIdReserva() + ".txt"
        );

        assertTrue(archivo.exists());
    }
}