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
        } finally {
            try {

                if(br != null) {
                    br.close();
                    System.out.println("Programa cerrado");
                }
            }catch (IOException e) {
                System.out.println("Error: no se puede cerrar");

            }


    }
    }

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
            }else{
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

    public static void leerDocumento(String ruta) throws IOException, NumeroIncorrectoException {
        if(ruta.equals("vehiculos.txt")){
            br = new BufferedReader(new FileReader(ruta));
            String linea;

            while ((linea = br.readLine()) != null) {
                flota.add(convierteAVehiculo(linea));
            }
        }else if(ruta.equals("clientes.txt")){
            br = new BufferedReader(new FileReader(ruta));
            String linea;

            while ((linea = br.readLine()) != null) {
                clientes.add(convierteACliente(linea));
            }
        }
    }
    public static String mostrarVehiculos(Vehiculo v){
        if(v instanceof Coche){
            Coche c = (Coche) v;
            return "Coche: " + v.toString() + " " + v.obtenerDetalles();
        } else if (v instanceof Furgoneta) {
            Furgoneta f = (Furgoneta) v;
            return "Furgoneta: " + v.toString() + " " + f.toString();
        }else{
            return " ";
        }
    }
}
