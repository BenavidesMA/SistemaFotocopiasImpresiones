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

    private String maquina;
    private String local;

    public Maquina(String maquina, String local) {
        this.maquina = maquina;
        this.local = local;
    }

    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }

    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();

        if (maquina == null || maquina.trim().length() < 3 || maquina.trim().length() > 10) {
            errores.append("- Código de máquina debe tener entre 3 y 10 caracteres.\n");
        }

        if (local == null || local.trim().length() < 3 || local.trim().length() > 20) {
            errores.append("- Nombre del local debe tener entre 3 y 20 caracteres.\n");
        }

        return errores.toString();
    }

    public String getMaquina() {
        return maquina;
    }

    public String getLocal() {
        return local;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return maquina + " (" + local + ")";
    }
}
