/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class LiquidacionAdicional extends Liquidacion implements Validable {
 
    private ServicioLiquidacion servicio;   // PK parcial
    private String              referencia; // opcional, 3-30 caracteres
    private int                 cantidad;   // 1-500
    private double              valor;      // precio unitario, > 0
 
    public LiquidacionAdicional(int numOrden, ServicioLiquidacion servicio,
                                 String referencia, int cantidad, double valor) {
        super(numOrden);
        this.servicio   = servicio;
        this.referencia = referencia;
        this.cantidad   = cantidad;
        this.valor      = valor;
    }
 
    // ── Sobreescritura de Liquidacion ─────────────────────────────────────────
 
    /** SOBREESCRITURA: total = cantidad × precio unitario. */
    @Override
    public double calcularTotal() {
        return cantidad * valor;
    }
 
    /** SOBREESCRITURA: muestra el detalle del servicio adicional. */
    @Override
    public String mostrarResumen() {
        String refTexto = (referencia == null || referencia.trim().isEmpty())
                        ? "N/A"
                        : referencia;
        return "── Liquidación Adicional ──────────────────\n"
             + "Servicio:    " + servicio.getDescripcion() + "\n"
             + "Referencia:  " + refTexto + "\n"
             + "Cantidad:    " + cantidad + "\n"
             + "Valor unit:  $" + String.format("%.2f", valor) + "\n"
             + "TOTAL:       $" + String.format("%.2f", calcularTotal());
    }
 
    // ── Validable ─────────────────────────────────────────────────────────────
 
    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }
 
    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();
 
        if (servicio == null)
            errores.append("- Debe seleccionar un servicio de liquidación.\n");
 
        if (referencia != null && !referencia.trim().isEmpty()) {
            if (referencia.trim().length() < 3 || referencia.trim().length() > 30)
                errores.append("- Referencia debe tener entre 3 y 30 caracteres si se ingresa.\n");
        }
 
        if (cantidad < 1 || cantidad > 500)
            errores.append("- Cantidad debe estar entre 1 y 500.\n");
 
        if (valor <= 0)
            errores.append("- Valor unitario debe ser mayor a 0.\n");
 
        return errores.toString();
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public ServicioLiquidacion getServicio()   { return servicio; }
    public String              getReferencia() { return referencia; }
    public int                 getCantidad()   { return cantidad; }
    public double              getValor()      { return valor; }
 
    @Override
    public String toString() {
        return servicio.getDescripcion() + " × " + cantidad
             + " = $" + String.format("%.2f", calcularTotal());
    }
}
