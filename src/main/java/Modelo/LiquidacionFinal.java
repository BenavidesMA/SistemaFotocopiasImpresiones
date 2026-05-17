/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Miguel
 */
public class LiquidacionFinal extends Liquidacion implements Validable {
 
    private String maquina;          // FK, 3-10 caracteres
    private String ficha;            // FK, 3-10 caracteres
    private String hora;             // formato HH:MM
    private String fechaElaboracion; // formato dd-mm-aaaa
 
    public LiquidacionFinal(int numOrden, String maquina, String ficha,
                             String hora, String fechaElaboracion) {
        super(numOrden);
        this.maquina          = maquina;
        this.ficha            = ficha;
        this.hora             = hora;
        this.fechaElaboracion = fechaElaboracion;
    }
 
    // ── Sobreescritura de Liquidacion ─────────────────────────────────────────
 
    /**
     * SOBREESCRITURA: retorna 0 porque LiquidacionFinal solo registra datos operativos,
     * no tiene cálculo monetario propio. El total de la orden viene de las
     * LiquidacionOperativa y LiquidacionAdicional.
     */
    @Override
    public double calcularTotal() {
        return 0.0;
    }
 
    /** SOBREESCRITURA: muestra los datos de cierre de la orden. */
    @Override
    public String mostrarResumen() {
        return "── Liquidación Final ──────────────────────\n"
             + "Máquina:            " + maquina + "\n"
             + "Ficha auxiliar:     " + ficha + "\n"
             + "Hora:               " + hora + "\n"
             + "Fecha elaboración:  " + fechaElaboracion;
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
            errores.append("- Máquina debe tener entre 3 y 10 caracteres.\n");
 
        if (ficha == null || ficha.trim().length() < 3 || ficha.trim().length() > 10)
            errores.append("- Ficha auxiliar debe tener entre 3 y 10 caracteres.\n");
 
        if (hora == null || !hora.matches("\\d{2}:\\d{2}"))
            errores.append("- Hora debe tener formato HH:MM (ej: 14:30).\n");
 
        if (fechaElaboracion == null || !fechaElaboracion.matches("\\d{2}-\\d{2}-\\d{4}"))
            errores.append("- Fecha elaboración debe tener formato dd-mm-aaaa (ej: 10-05-2026).\n");
 
        return errores.toString();
    }
 
    // ── Getters ────────────────────────────────────────────────────────────────
 
    public String getMaquina()          { return maquina; }
    public String getFicha()            { return ficha; }
    public String getHora()             { return hora; }
    public String getFechaElaboracion() { return fechaElaboracion; }
 
    @Override
    public String toString() {
        return "Cierre: Máquina " + maquina + " | Auxiliar " + ficha
             + " | " + hora + " - " + fechaElaboracion;
    }
}
 
