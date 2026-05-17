/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class Maquina implements Validable {
 
    private String maquina; // PK, 3-10 caracteres
    private String local;   // 3-20 caracteres
 
    public Maquina(String maquina, String local) {
        this.maquina = maquina;
        this.local   = local;
    }
 
    // ── Validable ─────────────────────────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (maquina == null || maquina.trim().length() < 3 || maquina.trim().length() > 10)
            errores.append("- Código de máquina debe tener entre 3 y 10 caracteres.\n");
 
        if (local == null || local.trim().length() < 3 || local.trim().length() > 20)
            errores.append("- Nombre del local debe tener entre 3 y 20 caracteres.\n");
 
        return errores.toString();
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public String getMaquina() { return maquina; }
    public String getLocal()   { return local; }
 
    // ── Setters ────────────────────────────────────────────────────────────────
 
    public void setMaquina(String maquina) { this.maquina = maquina; }
    public void setLocal(String local)     { this.local = local; }
 
    @Override
    public String toString() {
        return maquina + " (" + local + ")";
    }
}
