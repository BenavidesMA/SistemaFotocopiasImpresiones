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
 
    private String tipoPapel;               // PK parcial, 4-31 caracteres
    private int    numHojasOImpresiones;    // 1-500
    private int    totalCopiasOImpresiones; // 1-500
    private int    hojasMalasOperario;      // 1-500
    private int    hojasMalasMaquina;       // 1-500
    private int    hojasNoContabMaquina;    // 1-500
    private int    hojasEnBlanco;           // < totalCopiasOImpresiones
    private double valor;                   // precio unitario por hoja/impresión, > 0
 
    public LiquidacionOperativa(int numOrden, String tipoPapel,
                                  int numHojasOImpresiones, int totalCopiasOImpresiones,
                                  int hojasMalasOperario, int hojasMalasMaquina,
                                  int hojasNoContabMaquina, int hojasEnBlanco, double valor) {
        super(numOrden);
        this.tipoPapel               = tipoPapel;
        this.numHojasOImpresiones    = numHojasOImpresiones;
        this.totalCopiasOImpresiones = totalCopiasOImpresiones;
        this.hojasMalasOperario      = hojasMalasOperario;
        this.hojasMalasMaquina       = hojasMalasMaquina;
        this.hojasNoContabMaquina    = hojasNoContabMaquina;
        this.hojasEnBlanco           = hojasEnBlanco;
        this.valor                   = valor;
    }
 
    // ── Sobreescritura de Liquidacion ─────────────────────────────────────────
 
    /** SOBREESCRITURA: total = copias/impresiones producidas × precio unitario. */
    @Override
    public double calcularTotal() {
        return totalCopiasOImpresiones * valor;
    }
 
    /** SOBREESCRITURA: muestra todos los contadores de la grilla operativa. */
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
 
    // ── Validable ─────────────────────────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (tipoPapel == null || tipoPapel.trim().length() < 4 || tipoPapel.trim().length() > 31)
            errores.append("- Tipo de papel debe tener entre 4 y 31 caracteres.\n");
 
        if (numHojasOImpresiones < 1 || numHojasOImpresiones > 500)
            errores.append("- Número de hojas/impresiones debe estar entre 1 y 500.\n");
 
        if (totalCopiasOImpresiones < 1 || totalCopiasOImpresiones > 500)
            errores.append("- Total copias/impresiones debe estar entre 1 y 500.\n");
 
        if (hojasEnBlanco >= totalCopiasOImpresiones)
            errores.append("- Hojas en blanco no puede ser igual o mayor al total de copias/impresiones.\n");
 
        if (valor <= 0)
            errores.append("- Valor unitario debe ser mayor a 0.\n");
 
        return errores.toString();
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public String getTipoPapel()               { return tipoPapel; }
    public int    getNumHojasOImpresiones()    { return numHojasOImpresiones; }
    public int    getTotalCopiasOImpresiones() { return totalCopiasOImpresiones; }
    public int    getHojasMalasOperario()      { return hojasMalasOperario; }
    public int    getHojasMalasMaquina()       { return hojasMalasMaquina; }
    public int    getHojasNoContabMaquina()    { return hojasNoContabMaquina; }
    public int    getHojasEnBlanco()           { return hojasEnBlanco; }
    public double getValor()                   { return valor; }
}