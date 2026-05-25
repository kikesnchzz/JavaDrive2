package model;

import Excepciones.FormatoIncorrectoException;

import java.util.Objects;

public class Cliente {
    private String dni;
    private String nombre;
    private String telefono;

    public Cliente(String dni, String nombre, String telefono) throws FormatoIncorrectoException {
        if(dni.length() != 9){
            throw new FormatoIncorrectoException("Formato incorrecto");
        }
        if(telefono.length() != 9){
            throw new FormatoIncorrectoException("Formato incorrecto");
        }

        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Datos del Cliente: Nombre: " + nombre + " DNI: " + dni + "Teléfono: " + telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        // Comparamos por DNI para saber si es el mismo cliente
        return Objects.equals(dni, cliente.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

}
