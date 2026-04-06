package model;

import Excepciones.NumeroIncorrectoException;

public class Coche extends Vehiculo {
    private tipoCoche tipo;
    private int numPlazas;

    public Coche(String matricula, String marca, String modelo, boolean disponible, tipoCoche tipo, int numPlazas) throws NumeroIncorrectoException {
        super(matricula, marca, modelo, disponible);
        if(numPlazas < 2 || numPlazas > 7) {
            throw new NumeroIncorrectoException("Número de plazas incorrecto tiene que ser mayor 2 y menor 7");
        }

        this.tipo = tipo;
        this.numPlazas = numPlazas;
    }


    public tipoCoche getTipo() {
        return tipo;
    }

    public void setTipo(tipoCoche tipo) {
        this.tipo = tipo;
    }

    public int getPlazas() {
        return numPlazas;
    }

    public void setPlazas(int plazas) {
        this.numPlazas = plazas;
    }

    public String obtenerDetllas() {
        return "Coche: [" + tipo + "], Plazas: [" + numPlazas + "]";
    }
}
