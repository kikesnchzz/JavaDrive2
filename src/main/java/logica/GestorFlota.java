package logica;

import Excepciones.NumeroIncorrectoException;
import model.Coche;
import model.Furgoneta;
import model.Vehiculo;
import model.tipoCoche;

import java.util.Scanner;

import static app.Main.flota;

public class GestorFlota {
    public static void crearVehiculo() throws NumeroIncorrectoException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Datos comunes:");

        System.out.println("Introduce la matrícula:");
        String matricula = sc.nextLine();
        System.out.println("Introduce la marca:");
        String marca = sc.nextLine();
        System.out.println("Introduce el modelo:");
        String modelo = sc.nextLine();

        System.out.println("==========================");
        System.out.println("1. Coche");
        System.out.println("2. Furgoneta");
        int numero;

        do {
            System.out.println("Introduce el número correspondiente si tiene Coche o Furgoneta");
             numero = sc.nextInt();
             sc.nextLine();

            switch (numero) {
                case 1:
                    System.out.println("Introduce el tipo de coche:");
                    String tipoStr = sc.nextLine();
                    System.out.println("Introduce el número de plazas:");
                    int numPlazas = sc.nextInt();

                    tipoCoche tipo = tipoCoche.valueOf(tipoStr.toUpperCase());
                    Coche coche = new Coche(matricula, marca, modelo, true, tipo, numPlazas);
                    flota.add(coche);
                    break;

                case 2:
                    System.out.println("Introduce si es de carga (true/false):");
                    boolean esDeCarga = sc.nextBoolean();
                    sc.nextLine();
                    System.out.println("Introduce la capacidad de la furgoneta:");
                    int capacidad = sc.nextInt();

                    Furgoneta furgo = new Furgoneta(matricula, marca, modelo, true, esDeCarga, capacidad);
                    flota.add(furgo);
                    break;
                default:
                    System.out.println("Ese número no esta en el menú");
                    break;
            }
        }while (numero != 2) ;

    }

    public static Vehiculo buscarVehiculo (String matricula) {
        for (Vehiculo v : flota) {
            if(v.getMatricula().equals(matricula)){
                return v;
            }
        }
        return null;
    }

    public static void listarVehiculosDisponibles() {
        for (Vehiculo v : flota){
            System.out.println(v);
        }
    }
}
