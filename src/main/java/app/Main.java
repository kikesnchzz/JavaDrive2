package app;

import Excepciones.NumeroIncorrectoException;
import logica.GestorClientes;
import logica.GestorFlota;
import logica.GestorInformes;
import logica.GestorReservas;
import model.Cliente;
import model.Reserva;
import model.Vehiculo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
        public static List<Vehiculo> flota = new ArrayList<>();
        public static List<Cliente> clientes = new ArrayList<>();

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int opcion;
            do {
                    System.out.println("==== MENU ====");
                    System.out.println("1. Crear cliente");
                    System.out.println("2. Crear vehículo");
                    System.out.println("3. Realizar reserva");
                    System.out.println("4. Generar informe");
                    System.out.println("5. Buscar cliente");
                    System.out.println("6. Buscar vehículo");
                    System.out.println("7. Listar vehiculos disponibles");
                    System.out.println("8. Salir");
                    System.out.print("Selecciona una opción: ");
                    opcion = sc.nextInt();
                    sc.nextLine(); // Consumir el salto de línea

                    switch (opcion) {
                            case 1:
                                    GestorClientes.crearCliente();
                                    break;
                            case 2:
                                    try {
                                            new GestorFlota().crearVehiculo();
                                    } catch (NumeroIncorrectoException e) {
                                            System.out.println("Error: " + e.getMessage());
                                    }
                                    break;
                            case 3:
                                    System.out.println("Dime el nombre del cliente:");
                                    String nombreCliente = sc.nextLine();
                                    Cliente cliente = GestorClientes.buscarCliente(nombreCliente);
                                    if (cliente == null) {
                                            System.out.println("Cliente no encontrado.");
                                            break;
                                    }
                                    System.out.println("Dime la matrícula del vehículo:");
                                    String matricula = sc.nextLine();
                                    Vehiculo vehiculo = GestorFlota.buscarVehiculo(matricula);
                                    if (vehiculo == null) {
                                            System.out.println("Vehículo no encontrado.");
                                            break;
                                    }
                                    System.out.println("Dime la fecha de inicio (YYYY-MM-DD):");
                                    String fechaInicioStr = sc.nextLine();
                                    System.out.println("Dime la fecha de fin (YYYY-MM-DD):");
                                    String fechaFinStr = sc.nextLine();
                                    LocalDate fechaInicio = LocalDate.parse(fechaInicioStr);
                                    LocalDate fechaFin = LocalDate.parse(fechaFinStr);
                                    GestorReservas.realizarReserva(cliente, vehiculo, fechaInicio, fechaFin);
                                    break;
                            case 4:
                                    new GestorInformes().cargarDatos();
                                    break;

                            case 5:
                                    System.out.println("Dime el DNI del cliente:");
                                    String dni = sc.nextLine();
                                    if(GestorClientes.buscarCliente(dni) == null) {
                                            System.out.println("Cliente no encontrado.");
                                    } else {
                            System.out.println("Cliente encontrado: " + GestorClientes.buscarCliente(dni).getNombre());
                                    }

                                    break;

                            case 6:
                                    System.out.println("Dime la matrícula del vehículo:");
                                     matricula = sc.nextLine();
                                    GestorFlota.buscarVehiculo( matricula);
                                    break;

                            case 7:
                                    GestorFlota.listarVehiculosDisponibles();
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
