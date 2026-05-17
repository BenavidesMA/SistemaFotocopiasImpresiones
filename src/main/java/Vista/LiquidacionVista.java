/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Miguel
 */
public class LiquidacionVista {
 
    private OrdenRepo ordenRepo;
    private MaquinaRepo maquinaRepo;
    private AuxiliarRepo auxiliarRepo;
 
    public LiquidacionVista(OrdenRepo ordenRepo,
                             MaquinaRepo maquinaRepo,
                             AuxiliarRepo auxiliarRepo) {
        this.ordenRepo    = ordenRepo;
        this.maquinaRepo  = maquinaRepo;
        this.auxiliarRepo = auxiliarRepo;
    }
 
    /**
     * Menú principal de liquidación.
     */
    public void gestionarLiquidacion() {
        int opcion;
        do {
            String input = JOptionPane.showInputDialog(
                null,
                "═══ MÓDULO DE LIQUIDACIÓN ═══\n"
              + "  (Uso exclusivo de Publicaciones)\n\n"
              + "1. Registrar Liquidación de Orden\n"
              + "2. Ver Órdenes Pendientes de Liquidación\n"
              + "3. Volver\n\n"
              + "Elija una opción:",
                "Liquidación - UAO",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (input == null) return;
 
            try {
                opcion = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
 
            switch (opcion) {
                case 1:
                    registrarLiquidacion();
                    break;
                case 2:
                    verOrdenesPendientes();
                    break;
                case 3:
                    // Volver
                    break;
                default:
                    mostrarError("Opción inválida.");
                    break;
            }
        } while (opcion != 3);
    }
 
    /**
     * Registra la liquidación completa de una orden.
     */
    private void registrarLiquidacion() {
        // Solicitar número de orden
        String input = JOptionPane.showInputDialog(
            null,
            "Ingrese el número de orden a liquidar:",
            "Liquidar Orden",
            JOptionPane.QUESTION_MESSAGE
        );
 
        if (input == null) return;
 
        int numOrden;
        try {
            numOrden = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número válido.");
            return;
        }
 
        // Buscar orden
        OrdenAutorizacion orden = ordenRepo.buscarPorNumero(numOrden);
        if (orden == null) {
            mostrarError("No existe una orden con ese número.");
            return;
        }
 
        // Verificar si ya tiene liquidación final
        if (orden.getLiquidacionFinal() != null) {
            mostrarError("Esta orden ya fue liquidada.");
            return;
        }
 
        JOptionPane.showMessageDialog(
            null,
            "═══ LIQUIDACIÓN DE ORDEN #" + String.format("%05d", numOrden) + " ═══\n\n"
          + "Orden: " + orden.getTipoOrden().getDescripcion() + "\n"
          + "Solicitante: " + orden.getNombreSolicitante() + " " 
          + orden.getApellidoSolicitante() + "\n\n"
          + "A continuación registre:\n"
          + "  1. Liquidación Operativa (tabla de hojas/impresiones)\n"
          + "  2. Liquidación Adicional (servicios extras)\n"
          + "  3. Liquidación Final (máquina, auxiliar, fecha)",
            "Liquidar Orden",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        // PASO 1: Liquidación Operativa
        if (!agregarLiquidacionOperativa(orden)) return;
 
        // PASO 2: Liquidación Adicional (opcional)
        agregarLiquidacionAdicional(orden);
 
        // PASO 3: Liquidación Final
        if (!agregarLiquidacionFinal(orden)) return;
 
        // Mostrar resumen y confirmar
        if (confirmarLiquidacion(orden)) {
            JOptionPane.showMessageDialog(
                null,
                "✓ Liquidación registrada exitosamente.\n\n"
              + "Orden #" + String.format("%05d", numOrden) + " completada.",
                "Liquidación Exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
 
    /**
     * Agrega liquidaciones operativas (tabla de hojas/impresiones).
     */
    private boolean agregarLiquidacionOperativa(OrdenAutorizacion orden) {
        boolean continuar = true;
        while (continuar) {
            String input = JOptionPane.showInputDialog(
                null,
                "═══ LIQUIDACIÓN OPERATIVA ═══\n\n"
              + "¿Desea agregar una fila de liquidación operativa?\n"
              + "Escriba 'si' para agregar o 'no' para terminar:",
                "Liquidación Operativa",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (input == null) return false;
 
            if (input.trim().equalsIgnoreCase("no")) {
                if (orden.getLiquidacionesOperativas().isEmpty()) {
                    mostrarError("Debe registrar al menos una liquidación operativa.");
                    continue;
                }
                continuar = false;
                continue;
            }
 
            if (!input.trim().equalsIgnoreCase("si")) {
                continue;
            }
 
            // Solicitar tipo de papel
            String tipoPapel = JOptionPane.showInputDialog(
                null,
                "Tipo de papel (4-31 caracteres):",
                "Tipo de Papel",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (tipoPapel == null) return false;
            if (tipoPapel.trim().isEmpty()) {
                mostrarError("El tipo de papel no puede estar vacío.");
                continue;
            }
 
            // Solicitar datos numéricos
            int numHojas = solicitarEntero("Número de hojas/impresiones (1-500):");
            if (numHojas == -1) continue;
 
            int totalCopias = solicitarEntero("Total copias/impresiones producidas (1-500):");
            if (totalCopias == -1) continue;
 
            int malasPor = solicitarEntero("Hojas malas por operario (0-500):");
            if (malasPor == -1) continue;
 
            int malasMaq = solicitarEntero("Hojas malas por máquina (0-500):");
            if (malasMaq == -1) continue;
 
            int noContab = solicitarEntero("Hojas no contables por máquina (0-500):");
            if (noContab == -1) continue;
 
            int blanco = solicitarEntero("Hojas en blanco (0-500):");
            if (blanco == -1) continue;
 
            double valor = solicitarDecimal("Valor unitario por hoja/impresión (> 0):");
            if (valor == -1) continue;
 
            // Crear liquidación operativa
            LiquidacionOperativa liqOp = new LiquidacionOperativa(
                orden.getNumOrden(),
                tipoPapel.trim(),
                numHojas,
                totalCopias,
                malasPor,
                malasMaq,
                noContab,
                blanco,
                valor
            );
 
            // Validar
            if (!liqOp.esValido()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Datos inválidos:\n\n" + liqOp.getMensajeValidacion(),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
 
            orden.agregarLiquidacionOperativa(liqOp);
            JOptionPane.showMessageDialog(
                null,
                "✓ Liquidación operativa agregada.\n\n"
              + "Subtotal: $" + String.format("%.2f", liqOp.calcularTotal()),
                "Agregada",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
 
        return true;
    }
 
    /**
     * Agrega liquidaciones adicionales (servicios extras) - OPCIONAL.
     */
    private void agregarLiquidacionAdicional(OrdenAutorizacion orden) {
        int respuesta = JOptionPane.showConfirmDialog(
            null,
            "¿Desea agregar servicios adicionales\n"
          + "(anillado, scanner, quema CD, etc.)?",
            "Liquidación Adicional",
            JOptionPane.YES_NO_OPTION
        );
 
        if (respuesta != JOptionPane.YES_OPTION) return;
 
        boolean continuar = true;
        while (continuar) {
            // Mostrar menú de servicios
            String menu = ServicioLiquidacion.listarServicios()
                        + "\n0. Terminar\n\n"
                        + "Ingrese el número del servicio:";
 
            String input = JOptionPane.showInputDialog(
                null,
                menu,
                "Seleccionar Servicio Adicional",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (input == null) return;
 
            try {
                int opcion = Integer.parseInt(input.trim());
                
                if (opcion == 0) {
                    continuar = false;
                    continue;
                }
 
                ServicioLiquidacion servicio = ServicioLiquidacion.porNumero(opcion);
                if (servicio == null) {
                    mostrarError("Opción inválida.");
                    continue;
                }
 
                // Solicitar referencia (opcional)
                String referencia = JOptionPane.showInputDialog(
                    null,
                    "Servicio: " + servicio.getDescripcion() + "\n\n"
                  + "Referencia (opcional, 3-30 caracteres):\n"
                  + "Deje vacío si no aplica.",
                    "Referencia",
                    JOptionPane.QUESTION_MESSAGE
                );
 
                if (referencia == null) return;
 
                int cantidad = solicitarEntero("Cantidad (1-500):");
                if (cantidad == -1) continue;
 
                double valor = solicitarDecimal("Valor unitario (> 0):");
                if (valor == -1) continue;
 
                // Crear liquidación adicional
                LiquidacionAdicional liqAd = new LiquidacionAdicional(
                    orden.getNumOrden(),
                    servicio,
                    referencia.trim(),
                    cantidad,
                    valor
                );
 
                // Validar
                if (!liqAd.esValido()) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Datos inválidos:\n\n" + liqAd.getMensajeValidacion(),
                        "Error de Validación",
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }
 
                orden.agregarLiquidacionAdicional(liqAd);
                JOptionPane.showMessageDialog(
                    null,
                    "✓ Servicio adicional agregado.\n\n"
                  + "Subtotal: $" + String.format("%.2f", liqAd.calcularTotal()),
                    "Agregado",
                    JOptionPane.INFORMATION_MESSAGE
                );
 
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número válido.");
            }
        }
    }
 
    /**
     * Agrega la liquidación final (máquina, auxiliar, hora, fecha).
     */
    private boolean agregarLiquidacionFinal(OrdenAutorizacion orden) {
        JOptionPane.showMessageDialog(
            null,
            "═══ LIQUIDACIÓN FINAL ═══\n\n"
          + "Registre los datos de cierre de la orden.",
            "Liquidación Final",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        // Seleccionar máquina
        String maquina = seleccionarMaquina();
        if (maquina == null) return false;
 
        // Seleccionar auxiliar
        String ficha = seleccionarAuxiliar();
        if (ficha == null) return false;
 
        // Solicitar hora (HH:MM)
        String hora = JOptionPane.showInputDialog(
            null,
            "Ingrese la hora (HH:MM):\n"
          + "Ejemplo: 14:30",
            "Hora",
            JOptionPane.QUESTION_MESSAGE
        );
 
        if (hora == null) return false;
        if (!hora.matches("\\d{2}:\\d{2}")) {
            mostrarError("Formato de hora inválido. Use HH:MM");
            return false;
        }
 
        // Solicitar fecha elaboración (dd-mm-aaaa)
        String fecha = JOptionPane.showInputDialog(
            null,
            "Ingrese la fecha de elaboración (dd-mm-aaaa):\n"
          + "Ejemplo: 10-05-2026",
            "Fecha Elaboración",
            JOptionPane.QUESTION_MESSAGE
        );
 
        if (fecha == null) return false;
        if (!fecha.matches("\\d{2}-\\d{2}-\\d{4}")) {
            mostrarError("Formato de fecha inválido. Use dd-mm-aaaa");
            return false;
        }
 
        // Crear liquidación final
        LiquidacionFinal liqFin = new LiquidacionFinal(
            orden.getNumOrden(),
            maquina,
            ficha,
            hora.trim(),
            fecha.trim()
        );
 
        // Validar
        if (!liqFin.esValido()) {
            JOptionPane.showMessageDialog(
                null,
                "Datos inválidos:\n\n" + liqFin.getMensajeValidacion(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
 
        orden.setLiquidacionFinal(liqFin);
        return true;
    }
 
    /**
     * Muestra resumen de la liquidación y solicita confirmación.
     */
    private boolean confirmarLiquidacion(OrdenAutorizacion orden) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══ RESUMEN DE LIQUIDACIÓN ═══\n\n");
        sb.append("Orden #").append(String.format("%05d", orden.getNumOrden())).append("\n\n");
 
        // Liquidaciones operativas
        double totalOp = 0;
        sb.append("── Liquidación Operativa ──\n");
        for (LiquidacionOperativa lo : orden.getLiquidacionesOperativas()) {
            sb.append("• ").append(lo.toString()).append("\n");
            totalOp += lo.calcularTotal();
        }
        sb.append("Subtotal Operativo: $").append(String.format("%.2f", totalOp)).append("\n\n");
 
        // Liquidaciones adicionales
        double totalAd = 0;
        if (!orden.getLiquidacionesAdicionales().isEmpty()) {
            sb.append("── Liquidación Adicional ──\n");
            for (LiquidacionAdicional la : orden.getLiquidacionesAdicionales()) {
                sb.append("• ").append(la.toString()).append("\n");
                totalAd += la.calcularTotal();
            }
            sb.append("Subtotal Adicional: $").append(String.format("%.2f", totalAd)).append("\n\n");
        }
 
        // Total general
        double totalGeneral = totalOp + totalAd;
        sb.append("════════════════════════════\n");
        sb.append("TOTAL GENERAL: $").append(String.format("%.2f", totalGeneral)).append("\n");
        sb.append("════════════════════════════\n\n");
 
        // Liquidación final
        sb.append(orden.getLiquidacionFinal().toString()).append("\n\n");
        sb.append("¿Confirmar liquidación?");
 
        int respuesta = JOptionPane.showConfirmDialog(
            null,
            sb.toString(),
            "Confirmar Liquidación",
            JOptionPane.YES_NO_OPTION
        );
 
        return respuesta == JOptionPane.YES_OPTION;
    }
 
    /**
     * Muestra órdenes pendientes de liquidación.
     */
    private void verOrdenesPendientes() {
        List<OrdenAutorizacion> ordenes = ordenRepo.listarTodas();
        
        List<OrdenAutorizacion> pendientes = new ArrayList<>();
        for (OrdenAutorizacion o : ordenes) {
            if (o.getLiquidacionFinal() == null) {
                pendientes.add(o);
            }
        }
 
        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "No hay órdenes pendientes de liquidación.",
                "Órdenes Pendientes",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
 
        StringBuilder sb = new StringBuilder();
        sb.append("═══ ÓRDENES PENDIENTES DE LIQUIDACIÓN ═══\n");
        sb.append("Total: ").append(pendientes.size()).append("\n\n");
 
        for (OrdenAutorizacion o : pendientes) {
            sb.append("• ").append(o.toString()).append("\n");
        }
 
        JOptionPane.showMessageDialog(
            null,
            sb.toString(),
            "Órdenes Pendientes",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
 
    // ── Métodos auxiliares ─────────────────────────────────────────────────
 
    private String seleccionarMaquina() {
        List<Maquina> maquinas = maquinaRepo.listarTodas();
 
        if (maquinas.isEmpty()) {
            mostrarError("No hay máquinas registradas.");
            return null;
        }
 
        String[] opciones = new String[maquinas.size()];
        for (int i = 0; i < maquinas.size(); i++) {
            opciones[i] = maquinas.get(i).toString();
        }
 
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            "Seleccione la máquina utilizada:",
            "Seleccionar Máquina",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
 
        if (seleccion == null) return null;
 
        for (Maquina m : maquinas) {
            if (m.toString().equals(seleccion)) {
                return m.getMaquina();
            }
        }
 
        return null;
    }
 
    private String seleccionarAuxiliar() {
        List<Auxiliar> auxiliares = auxiliarRepo.listarTodos();
 
        if (auxiliares.isEmpty()) {
            mostrarError("No hay auxiliares registrados.");
            return null;
        }
 
        String[] opciones = new String[auxiliares.size()];
        for (int i = 0; i < auxiliares.size(); i++) {
            opciones[i] = auxiliares.get(i).toString();
        }
 
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            "Seleccione el auxiliar que procesó la orden:",
            "Seleccionar Auxiliar",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
 
        if (seleccion == null) return null;
 
        for (Auxiliar a : auxiliares) {
            if (a.toString().equals(seleccion)) {
                return a.getFicha();
            }
        }
 
        return null;
    }
 
    private int solicitarEntero(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje, "Ingresar Número", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return -1;
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número entero válido.");
            return -1;
        }
    }
 
    private double solicitarDecimal(String mensaje) {
        String input = JOptionPane.showInputDialog(null, mensaje, "Ingresar Valor", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return -1;
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número decimal válido.");
            return -1;
        }
    }
 
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
