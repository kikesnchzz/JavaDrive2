package model;

import Excepciones.FormatoIncorrectoException;

import java.util.Objects;

public abstract class Vehiculo {
    private String matricula;
    private String marca;
    private String modelo;
    public boolean disponible;

    public Vehiculo(String matricula, String marca, String modelo, boolean disponible) throws FormatoIncorrectoException {
        if(matricula.length() != 7){
            throw new FormatoIncorrectoException("Formato de matricula incorrecto");
        }

        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.disponible = true;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public abstract String obtenerDetalles();

    @Override
    public String toString() {
        return "Datos del Vehículo: Marca y Modelo: " + marca + " " + modelo + ", Matrícula: " + matricula + ", Disponible: " +disponible;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehiculo vehiculo= (Vehiculo) o;
        // Comparamos por DNI para saber si es el mismo cliente
        return Objects.equals(matricula, vehiculo.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

}
