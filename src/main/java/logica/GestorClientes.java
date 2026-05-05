package logica;

import model.Cliente;
import java.util.List;
import java.util.Scanner;

import static app.Main.clientes;


public class GestorClientes {


    /**
     * Método para crear clientes
     */
    public static void crearCliente() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Introduce DNI: ");
        String dni = sc.nextLine();

        // Comprobamos si ya existe
        if (buscarCliente(dni) != null) {
            System.out.println("Error: El cliente ya existe.");
            return;
        }

        System.out.print("Introduce nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Introduce teléfono: ");
        String telefono = sc.nextLine();

        Cliente nuevo = new Cliente(dni, nombre, telefono);
        if(!clientes.contains(nuevo)) {
            clientes.add(nuevo);
            System.out.println("Cliente creado correctamente.");
        }else{
            System.out.println("Error: El cliente ya existe.");
        }
    }

    /**
     * Metodo para buscar clientes
     * @param dni
     * @return el objeto
     */
    public static Cliente buscarCliente(String dni) {
        for (Cliente c : clientes) {
            if (c.getDni().trim().equalsIgnoreCase(dni.trim())) {
                return c;
            }
        }
        return null;
    }
}
