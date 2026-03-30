package logica;

import model.Vehiculo;

import static app.Main.flota;

public class GestorFlota {
    public void crearVehiculo() {
        
    }

    public Vehiculo buscarVehiculo (String matricula) {
        for (Vehiculo v : flota) {
            if(v.getMatricula().equals(matricula)){
                return v;
            }
        }

        return null;
    }

    public void listarVehiculosDisponibles() {
        for (Vehiculo v : flota){
            System.out.println(v);
        }
    }
}
