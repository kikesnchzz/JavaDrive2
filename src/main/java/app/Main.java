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

public class Main {
    public static List<Vehiculo> flota = new ArrayList<>();
    public static List<Cliente> clientes = new ArrayList<>();

    /**
     * Menú
     * @param args
     */
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
                    System.out.println("Dime la matrícula del vehículo:");// Pedimos la matricula
                    String matricula = sc.nextLine();
                    Vehiculo vehiculo = GestorFlota.buscarVehiculo(matricula);//creamos un objeto para poder imprimirlo
                    System.out.println(vehiculo);
                    if (vehiculo == null) {
                        System.out.println("Vehículo no encontrado.");
                        break;
                    }
                    System.out.println("Dime la fecha de inicio (YYYY-MM-DD):");//Pedimos los  demas datos de una reserva
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


    /**
     * Método para generar el informe en xml
     * @throws IOException
     */
    public static void generarInformeXML() throws IOException {

        PrintWriter pw = new PrintWriter(new FileWriter("reporte_completo.xml", false));

        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pw.println("<?xml-stylesheet type=\"text/xsl\" href=\"estilo_javadrive.xsl\"?>");

        pw.println("<javadrive>");
        pw.println("  <empresa>");
        pw.println("    <nombre>JavaDrive S.L.</nombre>");
        pw.println("    <ubicacion>Sede Central - Madrid</ubicacion>");
        pw.println("  </empresa>");


        pw.println("  <flota>");
        for (Vehiculo v : flota) {
        String tipo;
            if (v instanceof Coche) {
                tipo = "Coche";
            } else {
                tipo = "Furgoneta";
            }

            pw.println("    <vehiculo tipo=\"" + tipo + "\">");
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
    }
}
