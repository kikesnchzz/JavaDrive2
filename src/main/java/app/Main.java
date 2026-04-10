package app;
import Excepciones.NumeroIncorrectoException;
import logica.GestorClientes;
import logica.GestorFlota;
import logica.GestorInformes;
import logica.GestorReservas;
import model.Cliente;
import model.Coche;
import model.Reserva;
import model.Vehiculo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static List<Vehiculo> flota = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GestorInformes.cargarDatos();
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
                    GestorClientes.crearCliente();
                    break;
                case 2:
                    try {
                        GestorFlota.crearVehiculo();
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
                    System.out.println("Dime el DNI del cliente:");
                    String dni = sc.nextLine();
                    if(GestorClientes.buscarCliente(dni) == null) {
                        System.out.println("Cliente no encontrado.");
                    } else {
                        System.out.println("Cliente encontrado: " + GestorClientes.buscarCliente(dni).getNombre());
                    }

                    break;

                case 5:
                    System.out.println("Dime la matrícula del vehículo:");
                    matricula = sc.nextLine();
                    GestorFlota.buscarVehiculo( matricula);
                    break;

                case 6:
                    GestorFlota.listarVehiculosDisponibles();
                    break;

                case 7:
                    generarInformeXML();
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



    public static void generarInformeXML() {

        try  {
            PrintWriter pw = new PrintWriter(new FileWriter("reporte_completo.xml"));
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<?xml-stylesheet type=\"text/xsl\" href=\"estilo_javadrive.xsl\"?>");

            pw.println("<javadrive>");
            pw.println("  <empresa>");
            pw.println("    <nombre>JavaDrive S.L.</nombre>");
            pw.println("    <ubicacion>Sede Central - Madrid</ubicacion>");
            pw.println("  </empresa>");

            pw.println("  <flota>");

            for (Vehiculo v : flota) {

                if (v instanceof Coche) {
                    pw.println("<vehiculo tipo=\"Coche\">");
                } else {
                    pw.println("<vehiculo tipo=\"Furgoneta\">");
                }
                pw.println("      <matricula>" + v.getMatricula() + "</matricula>");
                pw.println("      <marca>" + v.getMarca() + "</marca>");
                pw.println("      <modelo>" + v.getModelo() + "</modelo>");
                pw.println("      <especifico>" + v.obtenerDetalles() + "</especifico>");

                pw.println("    </vehiculo>");
            }

            pw.println("  </flota>");

            pw.println("  <clientes>");

            for (Cliente c : clientes) {
                pw.println("    <cliente>");
                pw.println("      <dni>" + c.getDni() + "</dni>");
                pw.println("      <nombre>" + c.getNombre() + "</nombre>");
                pw.println("      <telefono>" + c.getTelefono() + "</telefono>");
                pw.println("    </cliente>");
            }

            pw.println("  </clientes>");

            pw.println("</javadrive>");
            pw.close();
        } catch (IOException e) {
            System.out.println("Error al generar XML");
        }
    }
}
