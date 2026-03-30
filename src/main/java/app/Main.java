package app;

import logica.GestorClientes;
import model.Cliente;
import model.Reserva;
import model.Vehiculo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
        public static List<Vehiculo> flota=new ArrayList<>();
        public static List<Cliente> clientes=new ArrayList<>();
        public static void main(String[] args) {
                Scanner sc = new Scanner(System.in);
            // TODO Auto-generated method stub
                try{
                        System.out.println("Bienvenido a la aplicación de alquiler de vehículos");
                        int opcion;
                        do{
                                System.out.println("\n ---Menú Principal---");
                                System.out.println("1. Crear cliente");
                                System.out.println("2. Gestión de vehículos");
                                System.out.println("3. Gestión de reservas");
                                System.out.println("4. Salir");
                                System.out.print("Seleccione una opción: ");
                                opcion= sc.nextInt();
                                sc.nextLine(); // Limpiar buffer
                                switch (opcion){
                                        case 1:
                                                GestorClientes gc = new GestorClientes();

                        }
                }
        }

}
