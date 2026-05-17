/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class Dependencia implements Validable {
 
    private String nombre;      // PK, 3-40 caracteres
    private String centroCosto; // 3-12 caracteres
 
    public Dependencia(String nombre, String centroCosto) {
        this.nombre      = nombre;
        this.centroCosto = centroCosto;
    }
 
    // ── Implementación de Validable ──────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (nombre == null || nombre.trim().length() < 3 || nombre.trim().length() > 40)
            errores.append("- Nombre de dependencia debe tener entre 3 y 40 caracteres.\n");
 
        if (centroCosto == null || centroCosto.trim().length() < 3 || centroCosto.trim().length() > 12)
            errores.append("- Centro de costo debe tener entre 3 y 12 caracteres.\n");
 
        return errores.toString();
    }
 
    // ── Getters ─────────────────────────────────────────────────────────────
    public String getNombre()      { return nombre; }
    public String getCentroCosto() { return centroCosto; }
 
    // ── Setters ─────────────────────────────────────────────────────────────
    public void setNombre(String nombre)           { this.nombre = nombre; }
    public void setCentroCosto(String centroCosto) { this.centroCosto = centroCosto; }
 
    @Override
    public String toString() {
        return nombre + "  (CC: " + centroCosto + ")";
    }
}