package logica;

import Excepciones.NumeroIncorrectoException;
import model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;

import model.Cliente;


import static app.Main.clientes;
import static app.Main.flota;

public class GestorInformes {
    public static BufferedReader br;
    public static FileWriter fw ;
    public static BufferedWriter bw ;
    public static PrintWriter pw;

    /**
     * Cargamos los datos desde cada documento txt
     * @throws IllegalArgumentException
     */


    /**
     * Método para crear un documento txt con el ticket de una reserva concreta
     * @param r
     */
    public static void exportarTicket (Reserva r) {
        try {
            String nombreArchivo = "ticket_" + r.getIdReserva() + ".txt";

            PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo));
            long totalDias = ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin());

            pw.println("============================================");
            pw.println("===== CONTRATO DE ALQUILER - JAVADRIVE =====");
            pw.println("============================================");
            pw.println("ID RESERVA: " + r.getIdReserva());
            pw.println("FECHA DE EMISIÓN: " + LocalDate.now());
            pw.println("---------------------------------------------");
            pw.println("DATOS DEL CLIENTE:");
            pw.println("Nombre: " + r.getCliente().getNombre());
            pw.println("DNI: " +r.getCliente().getDni());
            pw.println("Teléfono: " +r.getCliente().getTelefono());
            pw.println("----------------------------------------------");
            pw.println("DATOS DEL VEHÍCULO: ");
            pw.println("Marca y Modelo: " + r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo());
            pw.println("Matricula: "+ r.getVehiculo().getMatricula());
            pw.println("Detalles: " + r.getVehiculo().obtenerDetalles());
            pw.println("-----------------------------------------------");
            pw.println("PERIODO DE ALQUILER: ");
            pw.println("Fecha de recogida: " + r.getFechaInicio());
            pw.println("Fecha de devolución: " + r.getFechaFin());
            pw.println("Total de días: " + totalDias +" días");
            pw.println("--------------------------------------------------");
            pw.println("ESTADO: Confirmado y Pendiente de pago");

            pw.println();
            pw.println("Firma del cliente:            Firma JavaDrive:");
            pw.println();
            pw.println("____________________          ____________________");
            pw.println("==================================================");
            pw.close();

        } catch (IOException e) {
            System.out.println("Error al exportar ticket");
        }
    }

    /**
     * Método que convierte una linea del documento de vehiculos a un objeto
     * @param linea
     * @return objeto coche y furgoneta
     * @throws NumeroIncorrectoException
     */
    public static Vehiculo convierteAVehiculo(String linea) throws NumeroIncorrectoException {
        String [] partes = linea.split(";");
        if (partes.length == 7) {
            if(partes[0].equals("COCHE")){
                String matricula = partes[1];
                String marca =partes[2];
                String modelo =partes[3];
                boolean disponible = Boolean.parseBoolean(partes[4]);
                tipoCoche tipo = tipoCoche.valueOf(partes[5]);
                int numPlazas = Integer.parseInt(partes[6]);
                return new Coche(matricula, marca, modelo, disponible,tipo, numPlazas);
            }else if(partes[0].equals("FURGONETA")){
                String matricula = partes[1];
                String marca =partes[2];
                String modelo =partes[3];
                boolean disponible = Boolean.parseBoolean(partes[4]);
                boolean esDeCarga = Boolean.parseBoolean(partes[5]);
                int capacidad = Integer.parseInt(partes[6]);
                return new Furgoneta(matricula, marca, modelo, disponible,esDeCarga, capacidad);
            }
        }
        return null;
    }

    /**
     * El mismo método que el anterior pero para el documento de clientes
     * @param linea
     * @return
     */
    public static Cliente convierteACliente(String linea) {
        String [] partes = linea.split(";");
        if (partes.length == 3) {
            String dni = partes[0];
            String nombre=partes[1];
            String telefono = partes[2];
            return new Cliente(dni, nombre, telefono);
        }
        return null;
    }

    /**
     * Metodo que lee los documentos y los convertimos para después añadirlos a los ArraysList correspondientes
     * @param ruta
     * @throws IOException
     * @throws NumeroIncorrectoException
     */
    public static void leerDocumento(String ruta) throws IOException, NumeroIncorrectoException {
        if (ruta.equals("vehiculos.txt")) {
            try (BufferedReader tempBr = new BufferedReader(new FileReader(ruta))) {
                String linea;
                while ((linea = tempBr.readLine()) != null) {
                    // convierteAVehiculo puede lanzar NumeroIncorrectoException (p. ej. plazas inválidas).
                    // Dejar que esa excepción suba para que cargador la capture (como antes).
                    Vehiculo v = convierteAVehiculo(linea);
                    if (v != null) {
                        flota.add(v);
                    }
                }
            }
        } else if (ruta.equals("clientes.txt")) {
            try (BufferedReader tempBr = new BufferedReader(new FileReader(ruta))) {
                String linea;
                while ((linea = tempBr.readLine()) != null) {
                    // convierteACliente no declara excepciones checked pero el constructor de Cliente puede
                    // lanzar FormatoIncorrectoException (Runtime). Capturamos cualquier excepción y
                    // simplemente omitimos la línea inválida.
                    try {
                        Cliente c = convierteACliente(linea);
                        if (c != null) {
                            clientes.add(c);
                        }
                    } catch (Exception e) {
                        // Línea inválida → omitimos (podrías registrar si quieres)
                        System.out.println("Línea de cliente inválida, se omite: " + linea);
                    }
                }
            }
        }
    }

    /**
     * Metodo paramostrar los vehículos por pantalla
     * @param v
     * @return la informacón de coche y de vehículo
     */
    public static String mostrarVehiculos(Vehiculo v){
        if(v instanceof Coche){
            Coche c = (Coche) v;
            return "Coche: " + c.obtenerDetalles();
        } else if (v instanceof Furgoneta) {
            Furgoneta f = (Furgoneta) v;
            return "Furgoneta;" + f.obtenerDetalles();
        }else{
            return " ";
        }
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
