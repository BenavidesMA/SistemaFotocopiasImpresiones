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

    private String maquina;
    private String ficha;
    private String hora;
    private String fechaElaboracion;

    public LiquidacionFinal(int numOrden, String maquina, String ficha,
            String hora, String fechaElaboracion) {
        super(numOrden);
        this.maquina = maquina;
        this.ficha = ficha;
        this.hora = hora;
        this.fechaElaboracion = fechaElaboracion;
    }

    @Override
    public double calcularTotal() {
        return 0.0;
    }

    @Override
    public String mostrarResumen() {
        return "── Liquidación Final ──────────────────────\n"
                + "Máquina:            " + maquina + "\n"
                + "Ficha auxiliar:     " + ficha + "\n"
                + "Hora:               " + hora + "\n"
                + "Fecha elaboración:  " + fechaElaboracion;
    }

    @Override
    public boolean esValido() {
        return getMensajeValidacion().isEmpty();
    }

    @Override
    public String getMensajeValidacion() {
        StringBuilder errores = new StringBuilder();

        if (maquina == null || maquina.trim().length() < 3 || maquina.trim().length() > 10) {
            errores.append("- Máquina debe tener entre 3 y 10 caracteres.\n");
        }

        if (ficha == null || ficha.trim().length() < 3 || ficha.trim().length() > 10) {
            errores.append("- Ficha auxiliar debe tener entre 3 y 10 caracteres.\n");
        }

        if (hora == null || !hora.matches("\\d{2}:\\d{2}")) {
            errores.append("- Hora debe tener formato HH:MM (ej: 14:30).\n");
        }

        if (fechaElaboracion == null || !fechaElaboracion.matches("\\d{4}-\\d{2}-\\d{2}")) {
            errores.append("- Fecha elaboración debe tener formato dd-mm-aaaa (ej: 2026-02-23).\n");
        }

        return errores.toString();
    }

    public String getMaquina() {
        return maquina;
    }

    public String getFicha() {
        return ficha;
    }

    public String getHora() {
        return hora;
    }

    public String getFechaElaboracion() {
        return fechaElaboracion;
    }

    @Override
    public String toString() {
        return "Cierre: Máquina " + maquina + " | Auxiliar " + ficha
                + " | " + hora + " - " + fechaElaboracion;
    }
}
