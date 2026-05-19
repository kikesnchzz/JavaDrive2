package logica;

import Excepciones.NumeroIncorrectoException;
import model.Cliente;
import model.Vehiculo;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static app.Main.clientes;
import static app.Main.flota;
import static logica.GestorInformes.*;

public class GestorPersistencia {
    public static void cargarDatos() throws IllegalArgumentException {
        flota.clear();
        clientes.clear();

        try {
            leerDocumento("vehiculos.txt");
            leerDocumento("clientes.txt");
        }catch (FileNotFoundException e){
            System.out.println("Error: El Archivo no se encuentra");
        }catch (IOException e) {
            System.out.println("Error: no se puede leer");
        } catch (NumeroIncorrectoException e) {
            System.out.println("Error, en el número incorrecto de plazas de coche");
        }
        // Ya no necesitas el finally porque leerDocumento cierra cada archivo
    }

    /**
     * Método para guardar todos los datos de los documentos txt
     */
    public static void guardarDatos() {
        try {
            pw = new PrintWriter(new FileWriter("vehiculos.txt"));

            for (Vehiculo v : flota) {
                pw.println(mostrarVehiculos(v));
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error al guardar datos de los vehículos");
        }
        try {
            pw = new PrintWriter(new FileWriter("clientes.txt"));

            for (Cliente c : clientes) {
                pw.println(c.toString());
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error al guardar datos del cliente");
        }

    }
}
