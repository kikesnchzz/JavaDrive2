package app;
import Excepciones.FormatoIncorrectoException;
import Excepciones.NumeroIncorrectoException;
import logica.GestorClientes;
import logica.GestorFlota;
import logica.GestorInformes;
import logica.GestorReservas;
import model.Cliente;
import model.Coche;
import model.Vehiculo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static logica.GestorClientes.buscarCliente;
import static logica.GestorInformes.generarInformeXML;
import static logica.GestorPersistencia.cargarDatos;

/**
 * @author Enrique
 * @author  Juan Carlos
 * @version 1.1.0
 */
public class Main {
    public static List<Vehiculo> flota = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();

    /**
     * Menú
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        cargarDatos();
        int opcion;
        do {
            System.out.println("==== MENU ====");
            System.out.println("1. Crear cliente");
            System.out.println("2. Crear vehículo");
            System.out.println("3. Realizar reserva");
            System.out.println("4. Buscar cliente");
            System.out.println("5. Buscar vehículo");
            System.out.println("6. Listar vehiculos disponibles");
            System.out.println("7. Generar XML");
            System.out.println("8. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    try{
                        GestorClientes.crearCliente();
                    }catch(FormatoIncorrectoException e){
                        System.out.println("Formato Incorrecto");
                    }

                    break;
                case 2:
                    try {
                        GestorFlota.crearVehiculo();
                    } catch (NumeroIncorrectoException e) {
                        System.out.println("Error: " + e.getMessage());
                    }catch(FormatoIncorrectoException e){
                        System.out.println("Formato Incorrecto");
                    }catch (IllegalArgumentException e){
                        System.out.println("Está mal escrito el tipo de coche");
                    }
                    break;
                case 3:
                    System.out.println("Dime el DNI del cliente:");//Pedimos el dni
                    String dniCliente = sc.nextLine().trim();
                    Cliente cliente = buscarCliente(dniCliente);//creamos un objeto para poder imprimirlo
                    System.out.println(cliente);
                    if (cliente == null) {
                        System.out.println("Cliente no encontrado.");
                        break;
                    }
                    System.out.println("Dime la matrícula del vehículo:");// Pedimos la matrícula
                    String matricula = sc.nextLine();
                    Vehiculo vehiculo = GestorFlota.buscarVehiculo(matricula);//creamos un objeto para poder imprimirlo
                    System.out.println(vehiculo);
                    if (vehiculo == null) {
                        System.out.println("Vehículo no encontrado.");
                        break;
                    }
                    System.out.println("Dime la fecha de inicio (YYYY-MM-DD):");//Pedimos los demás datos de una reserva
                    String fechaInicioStr = sc.nextLine();
                    System.out.println("Dime la fecha de fin (YYYY-MM-DD):");
                    String fechaFinStr = sc.nextLine();
                    LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);//Parseamos las fechas
                    LocalDate fechaFin = LocalDate.parse(fechaFinStr);
                    GestorReservas.realizarReserva(cliente, vehiculo, fechaInicio, fechaFin);
                    break;
                case 4:
                    System.out.println("Dime el DNI del cliente:");//Pedimos el dni para buscar
                    String dni = sc.nextLine().trim();
                    if(buscarCliente(dni) == null) {
                        System.out.println("Cliente no encontrado.");
                    } else {
                        System.out.println("Cliente encontrado: " + buscarCliente(dni).getNombre());
                    }

                    break;

                case 5:
                    System.out.println("Dime la matrícula del vehículo:");//Pedimos la matrícula para buscar
                    matricula = sc.nextLine();
                    GestorFlota.buscarVehiculo( matricula);
                    break;

                case 6:
                    GestorFlota.listarVehiculosDisponibles();//Imprimimos los vehículos disponibles
                    System.out.println("Archivo creado correctamente");
                    break;

                case 7:
                    try{
                        generarInformeXML();// Generamos el informe
                    }catch (IOException e) {
                        System.out.println("Error al generar XML");
                    }

                    break;
                case 8:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }
        } while (opcion != 8) ;
        sc.close();
    }
}
