package logica;

import model.Cliente;
import java.util.List;
import java.util.Scanner;

import static app.Main.clientes;


public class GestorClientes {


    // Gestión de negocios
    public void crearCliente() {
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
        clientes.add(nuevo);

        System.out.println("Cliente creado correctamente.");
    }

    public static Cliente buscarCliente(String dni) {
        for (Cliente c : clientes) {
            if (c.getDni().equalsIgnoreCase(dni)) {
                return c;
            }
        }
        return null;
    }
}
