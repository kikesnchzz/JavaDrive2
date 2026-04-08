package model;

public class Furgoneta extends Vehiculo {
    private boolean esDeCarga;
    private int capacidad;

    public Furgoneta(String matricula, String marca, String modelo, boolean disponible, boolean esDeCarga,
                     int capacidad) {
        super(matricula, marca, modelo, disponible);
        this.esDeCarga = esDeCarga;
        this.capacidad = capacidad;
    }

    public boolean isEsDeCarga() {
        return esDeCarga;
    }

    public void setEsDeCarga(boolean esDeCarga) {
        this.esDeCarga = esDeCarga;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String obtenerDetalles() {
        if(esDeCarga) {
            return "Detalles: Furgoneta de Carga: (" + capacidad + " kg)";
        }else {
            return "Detalles: Furgoneta de Pasajeros: (" + capacidad + " personas)";
        }
    }


}
