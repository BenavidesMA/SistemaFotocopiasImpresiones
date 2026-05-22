/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class LiquidacionOperativa extends Liquidacion implements Validable {

    private String tipoPapel;
    private int numHojasOImpresiones;
    private int totalCopiasOImpresiones;
    private int hojasMalasOperario;
    private int hojasMalasMaquina;
    private int hojasNoContabMaquina;
    private int hojasEnBlanco;
    private double valor;

    public LiquidacionOperativa(int numOrden, String tipoPapel,
            int numHojasOImpresiones, int totalCopiasOImpresiones,
            int hojasMalasOperario, int hojasMalasMaquina,
            int hojasNoContabMaquina, int hojasEnBlanco, double valor) {
        super(numOrden);
        this.tipoPapel = tipoPapel;
        this.numHojasOImpresiones = numHojasOImpresiones;
        this.totalCopiasOImpresiones = totalCopiasOImpresiones;
        this.hojasMalasOperario = hojasMalasOperario;
        this.hojasMalasMaquina = hojasMalasMaquina;
        this.hojasNoContabMaquina = hojasNoContabMaquina;
        this.hojasEnBlanco = hojasEnBlanco;
        this.valor = valor;
    }

    @Override
    public double calcularTotal() {
        return totalCopiasOImpresiones * valor;
    }

    @Override
    public String mostrarResumen() {
        return "── Liquidación Operativa ──────────────────\n"
                + "Tipo papel:                 " + tipoPapel + "\n"
                + "Hojas/Impresiones:          " + numHojasOImpresiones + "\n"
                + "Total copias/impresiones:   " + totalCopiasOImpresiones + "\n"
                + "Hojas malas operario:       " + hojasMalasOperario + "\n"
                + "Hojas malas máquina:        " + hojasMalasMaquina + "\n"
                + "Hojas no contables máquina: " + hojasNoContabMaquina + "\n"
                + "Hojas en blanco:            " + hojasEnBlanco + "\n"
                + "Valor unitario:             $" + String.format("%.2f", valor) + "\n"
                + "TOTAL:                      $" + String.format("%.2f", calcularTotal());
    }

    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }

    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();

        if (tipoPapel == null || tipoPapel.trim().length() < 4 || tipoPapel.trim().length() > 31) {
            errores.append("- Tipo de papel debe tener entre 4 y 31 caracteres.\n");
        }

        if (numHojasOImpresiones < 1 || numHojasOImpresiones > 500) {
            errores.append("- Número de hojas/impresiones debe estar entre 1 y 500.\n");
        }

        if (totalCopiasOImpresiones < 1 || totalCopiasOImpresiones > 500) {
            errores.append("- Total copias/impresiones debe estar entre 1 y 500.\n");
        }

        if (hojasEnBlanco >= totalCopiasOImpresiones) {
            errores.append("- Hojas en blanco no puede ser igual o mayor al total de copias/impresiones.\n");
        }

        if (valor <= 0) {
            errores.append("- Valor unitario debe ser mayor a 0.\n");
        }

        return errores.toString();
    }

    public String getTipoPapel() {
        return tipoPapel;
    }

    public int getNumHojasOImpresiones() {
        return numHojasOImpresiones;
    }

    public int getTotalCopiasOImpresiones() {
        return totalCopiasOImpresiones;
    }

    public int getHojasMalasOperario() {
        return hojasMalasOperario;
    }

    public int getHojasMalasMaquina() {
        return hojasMalasMaquina;
    }

    public int getHojasNoContabMaquina() {
        return hojasNoContabMaquina;
    }

    public int getHojasEnBlanco() {
        return hojasEnBlanco;
    }

    public double getValor() {
        return valor;
    }
}
