package logica;

import model.Cliente;
import model.Reserva;
import model.Vehiculo;

import java.time.LocalDate;

import static logica.GestorInformes.exportarTicket;

public class GestorReservas {

    public static void realizarReserva(Cliente c, Vehiculo v, LocalDate fInicio, LocalDate fFin) {
        // 1. Cambiar estado del vehículo
        v.setDisponible(false);

        // 2. Crear la reserva
        Reserva reserva = new Reserva(c, v, fInicio, fFin);

        // 3. Exportar ticket
        exportarTicket(reserva);

        System.out.println("Reserva realizada correctamente.");
    }
}
