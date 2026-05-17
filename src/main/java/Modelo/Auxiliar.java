/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class Auxiliar implements Validable {
 
    private String ficha;    // PK, 3-10 caracteres
    private String auxiliar; // nombre, 7-40 caracteres
 
    public Auxiliar(String ficha, String auxiliar) {
        this.ficha    = ficha;
        this.auxiliar = auxiliar;
    }
 
    // ── Validable ─────────────────────────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (ficha == null || ficha.trim().length() < 3 || ficha.trim().length() > 10)
            errores.append("- Código de ficha debe tener entre 3 y 10 caracteres.\n");
 
        if (auxiliar == null || auxiliar.trim().length() < 7 || auxiliar.trim().length() > 40)
            errores.append("- Nombre del auxiliar debe tener entre 7 y 40 caracteres.\n");
 
        return errores.toString();
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public String getFicha()    { return ficha; }
    public String getAuxiliar() { return auxiliar; }
 
    // ── Setters ────────────────────────────────────────────────────────────────
 
    public void setFicha(String ficha)       { this.ficha = ficha; }
    public void setAuxiliar(String auxiliar) { this.auxiliar = auxiliar; }
 
    @Override
    public String toString() {
        return auxiliar + " (Ficha: " + ficha + ")";
    }
}
