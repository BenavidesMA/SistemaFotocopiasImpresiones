/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public abstract class Persona {

    protected String nombre;
    protected String apellido;
    protected String cargo;
    protected String password;

    public Persona(String nombre, String apellido, String cargo, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
        this.password = password;
    }

    public abstract TipoUsuario getTipoUsuario();

    public abstract String mostrarInfo();

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCargo() {
        return cargo;
    }

    public String getPassword() {
        return password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " [" + getTipoUsuario().getDescripcion() + "]";
    }
}
