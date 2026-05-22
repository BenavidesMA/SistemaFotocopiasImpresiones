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

    private ServicioLiquidacion servicio;
    private String referencia;
    private int cantidad;
    private double valor;

    public LiquidacionAdicional(int numOrden, ServicioLiquidacion servicio,
            String referencia, int cantidad, double valor) {
        super(numOrden);
        this.servicio = servicio;
        this.referencia = referencia;
        this.cantidad = cantidad;
        this.valor = valor;
    }

    @Override
    public double calcularTotal() {
        return cantidad * valor;
    }

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

    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }

    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();

        if (servicio == null) {
            errores.append("- Debe seleccionar un servicio de liquidación.\n");
        }

        if (referencia != null && !referencia.trim().isEmpty()) {
            if (referencia.trim().length() < 3 || referencia.trim().length() > 30) {
                errores.append("- Referencia debe tener entre 3 y 30 caracteres si se ingresa.\n");
            }
        }

        if (cantidad < 1 || cantidad > 500) {
            errores.append("- Cantidad debe estar entre 1 y 500.\n");
        }

        if (valor <= 0) {
            errores.append("- Valor unitario debe ser mayor a 0.\n");
        }

        return errores.toString();
    }

    public ServicioLiquidacion getServicio() {
        return servicio;
    }

    public String getReferencia() {
        return referencia;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return servicio.getDescripcion() + " × " + cantidad
                + " = $" + String.format("%.2f", calcularTotal());
    }
}
