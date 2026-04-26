package logica;

import Excepciones.NumeroIncorrectoException;
import app.Main;
import model.Cliente;
import model.Coche;
import model.Furgoneta;
import model.Vehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static model.tipoCoche.Deportivo;
import static org.junit.jupiter.api.Assertions.*;

class GestorFlotaTest {
    static ArrayList<Vehiculo> flota;

    @BeforeEach
    void setUp() throws NumeroIncorrectoException {
        flota = new ArrayList<>();
        flota.add(new Furgoneta("3344MNO", "Volkswagen", "Multivan", true, false, 7));
        flota.add(new Coche("9012GHI","Porsche","911",true,Deportivo,2));

        // Asignamos la lista a la clase donde están los métodos
        Main.flota = flota;
    }
    @Test
    void buscarVehiculoExistente() {
        Vehiculo v = GestorFlota.buscarVehiculo("3344MNO");

        assertNotNull(v);
        assertEquals("Volkswagen", v.getMarca());
    }

    @Test
    void buscarVehiculoNoExistente() {
        Vehiculo v = GestorFlota.buscarVehiculo("0000XXX");

        assertNull(v);
    }

    @Test
    void listarVehiculosDisponibles() {
        // Suponiendo que ambos están disponibles al inicio
        ArrayList<Vehiculo> disponibles = GestorFlota.listarVehiculosDisponibles();

        assertNotNull(disponibles);
        assertEquals(2, disponibles.size());
    }

    @Test
    void crearVehiculoCorrecto() throws NumeroIncorrectoException {
        // Simulamos que no existe ese vehículo
        Vehiculo nuevo = new Coche("5555ZZZ", "BMW", "M3", false, Deportivo, 4);

        if (!Main.flota.contains(nuevo)) {
            Main.flota.add(nuevo);
        }

        Vehiculo v = GestorFlota.buscarVehiculo("5555ZZZ");

        assertNotNull(v);
        assertEquals("BMW", v.getMarca());
    }

    @Test
    void crearVehiculoDuplicado() throws NumeroIncorrectoException {
        Vehiculo duplicado = new Coche("9012GHI","Porsche","911",false,Deportivo,2);

        int sizeAntes = Main.flota.size();

        if (!Main.flota.contains(duplicado)) {
            Main.flota.add(duplicado);
        }

        int sizeDespues = Main.flota.size();

        assertEquals(sizeAntes, sizeDespues);
    }
}