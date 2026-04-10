package logica;

import Excepciones.NumeroIncorrectoException;
import model.Coche;
import model.Furgoneta;
import model.Vehiculo;
import model.tipoCoche;

import java.util.Scanner;

import static app.Main.clientes;
import static app.Main.flota;

public class GestorFlota {
    /**
     * Método para crear vehículos
     * @throws NumeroIncorrectoException
     */
    public static void crearVehiculo() throws NumeroIncorrectoException {
       // Generalizamos los el pedir la matrícula, marca o modelo
        Scanner sc = new Scanner(System.in);
        System.out.println("Datos comunes:");

        System.out.println("Introduce la matrícula:");
        String matricula = sc.nextLine();
        System.out.println("Introduce la marca:");
        String marca = sc.nextLine();
        System.out.println("Introduce el modelo:");
        String modelo = sc.nextLine();

        //Preguntamos si coche o furgoneta
        System.out.println("==========================");
        System.out.println("1. Coche");
        System.out.println("2. Furgoneta");
        int numero;


            System.out.println("Introduce el número correspondiente si tiene Coche o Furgoneta");
             numero = sc.nextInt();
             sc.nextLine();

            switch (numero) {
                case 1:
                    // creamos el coche
                    System.out.println("Introduce el tipo de coche:");
                    String tipoStr = sc.nextLine();
                    System.out.println("Introduce el número de plazas:");
                    int numPlazas = sc.nextInt();

                    tipoCoche tipo = tipoCoche.valueOf(tipoStr);
                    Coche coche = new Coche(matricula, marca, modelo, true, tipo, numPlazas);
                    if(!flota.contains(coche)) {
                        flota.add(coche);
                        System.out.println("Coche creado correctamente.");
                    }else{
                        System.out.println("Error: El coche ya existe.");
                    }
                    break;

                case 2:
                    //creamos la furgoneta
                    System.out.println("Introduce si es de carga (true/false):");
                    boolean esDeCarga = sc.nextBoolean();
                    sc.nextLine();
                    System.out.println("Introduce la capacidad de la furgoneta:");
                    int capacidad = sc.nextInt();

                    Furgoneta furgo = new Furgoneta(matricula, marca, modelo, true, esDeCarga, capacidad);
                    if(!flota.contains(furgo)) {
                        flota.add(furgo);
                        System.out.println("Furgoneta creado correctamente.");
                    }else{
                        System.out.println("Error: La furgoneta ya existe.");
                    }
                    break;
                default:
                    System.out.println("Ese número no esta en el menú");
                    break;
            }

    }

    /**
     * Método para buscar un vehículo
     * @param matricula
     * @return el objeto
     */
    public static Vehiculo buscarVehiculo (String matricula) {
        for (Vehiculo v : flota) {
            if(v.getMatricula().equals(matricula)){
                System.out.println("Vehículo encontrado: " + v.obtenerDetalles() + " " + v.toString());
                return v;

            }
        }
        System.out.println("No se ha encontrado ningún vehículo con esa matrícula.");
        return null;
    }

    /**
     * Método para que devuelve la lista de vehículos que están disponibles
     */
    public static void listarVehiculosDisponibles() {
        for (Vehiculo v : flota){
            if(v.isDisponible() == true){
                System.out.println(v.obtenerDetalles() + " " + v.toString());
            }
        }
    }
}
