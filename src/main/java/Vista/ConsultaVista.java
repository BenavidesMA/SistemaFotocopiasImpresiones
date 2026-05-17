/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.*;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Miguel
 */
public class ConsultaVista {

    private OrdenRepo ordenRepo;

    public ConsultaVista(OrdenRepo ordenRepo) {
        this.ordenRepo = ordenRepo;
    }

    /**
     * Menú de consulta de órdenes.
     */
    public void consultarOrdenes(Solicitante usuarioActual) {
        int opcion;
        do {
            String input = JOptionPane.showInputDialog(
                    null,
                    "═══ CONSULTAR ÓRDENES ═══\n\n"
                    + "1. Buscar por Número de Orden\n"
                    + "2. Ver Mis Órdenes\n"
                    + "3. Ver Todas las Órdenes (resumen)\n"
                    + "4. Volver\n\n"
                    + "Elija una opción:",
                    "Consulta de Órdenes",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                return;
            }

            try {
                opcion = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    buscarPorNumero();
                    break;
                case 2:
                    verMisOrdenes(usuarioActual);
                    break;
                case 3:
                    verTodasOrdenes();
                    break;
                case 4:
                    // Volver
                    break;
                default:
                    mostrarError("Opción inválida.");
                    break;
            }
        } while (opcion != 4);
    }

    /**
     * Busca una orden por su número y muestra su detalle completo.
     */
    private void buscarPorNumero() {
        String input = JOptionPane.showInputDialog(
                null,
                "Ingrese el número de orden a buscar:",
                "Buscar Orden",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input == null) {
            return;
        }

        try {
            int numOrden = Integer.parseInt(input.trim());
            OrdenAutorizacion orden = ordenRepo.buscarPorNumero(numOrden);

            if (orden == null) {
                mostrarError("No existe una orden con ese número.");
                return;
            }

            mostrarDetalleOrden(orden);

        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número válido.");
        }
    }

    /**
     * Muestra todas las órdenes del solicitante actual.
     */
    private void verMisOrdenes(Solicitante usuarioActual) {
        List<OrdenAutorizacion> ordenes = ordenRepo.buscarPorSolicitante(
                usuarioActual.getNombre(),
                usuarioActual.getApellido()
        );

        if (ordenes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No tiene órdenes registradas.",
                    "Mis Órdenes",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══ MIS ÓRDENES ═══\n");
        sb.append("Total: ").append(ordenes.size()).append("\n\n");

        for (OrdenAutorizacion o : ordenes) {
            sb.append("• ").append(o.toString()).append("\n");
        }

        sb.append("\n¿Desea ver el detalle de alguna orden?");

        int respuesta = JOptionPane.showConfirmDialog(
                null,
                sb.toString(),
                "Mis Órdenes",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            buscarPorNumero();
        }
    }

    /**
     * Muestra un resumen de todas las órdenes del sistema.
     */
    private void verTodasOrdenes() {
        List<OrdenAutorizacion> ordenes = ordenRepo.listarTodas();

        if (ordenes.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay órdenes registradas en el sistema.",
                    "Todas las Órdenes", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══ TODAS LAS ÓRDENES ═══\n");
        sb.append("Total: ").append(ordenes.size()).append("\n\n");

        for (OrdenAutorizacion o : ordenes) {
            sb.append("────────────────────────────────\n");
            sb.append("Orden #").append(String.format("%05d", o.getNumOrden()))
                    .append("  |  ").append(o.getTipoOrden().getDescripcion())
                    .append("  |  ").append(o.getFechaSolicitud()).append("\n");

            // Datos del solicitante
            sb.append("Solicitante: ").append(o.getNombreSolicitante())
                    .append(" ").append(o.getApellidoSolicitante()).append("\n");

            // Descripción de servicios incluidos
            if (!o.getServicios().isEmpty()) {
                sb.append("Servicios:   ");
                for (int i = 0; i < o.getServicios().size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(o.getServicios().get(i).getServicioSeleccionado().getDescripcion());
                }
                sb.append("\n");
            } else {
                sb.append("Servicios:   [ninguno registrado]\n");
            }

            sb.append("Liquidada:   ")
                    .append(o.getLiquidacionFinal() != null ? "Sí" : "Pendiente")
                    .append("\n\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(),
                "Todas las Órdenes", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra el detalle completo de una orden.
     */
    private void mostrarDetalleOrden(OrdenAutorizacion orden) {
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════\n");
        sb.append("     DETALLE DE ORDEN #").append(String.format("%05d", orden.getNumOrden())).append("\n");
        sb.append("════════════════════════════════════════\n\n");

        // Sección 1: Datos básicos
        sb.append("──── DATOS BÁSICOS ────\n");
        sb.append("Tipo:        ").append(orden.getTipoOrden().getDescripcion()).append("\n");
        sb.append("Fecha:       ").append(orden.getFechaSolicitud()).append("\n");
        sb.append("Solicitante: ").append(orden.getNombreSolicitante())
                .append(" ").append(orden.getApellidoSolicitante()).append("\n");
        sb.append("Firma:       ").append(orden.getFirmaAutorizada()).append("\n");

        if (orden.getObservaciones() != null && !orden.getObservaciones().trim().isEmpty()) {
            sb.append("Obs:         ").append(orden.getObservaciones()).append("\n");
        }

        // Sección 2: Servicios marcados
        if (!orden.getServicios().isEmpty()) {
            sb.append("\n──── SERVICIOS SOLICITADOS ────\n");
            for (Servicio s : orden.getServicios()) {
                sb.append("  • ").append(s.toString()).append("\n");
            }
        }

        // Sección 3: Trabajos/Solicitudes
        if (!orden.getSolicitudes().isEmpty()) {
            sb.append("\n──── TRABAJOS SOLICITADOS ────\n");
            for (Solicitud sol : orden.getSolicitudes()) {
                sb.append("  ▪ ").append(sol.toString()).append("\n");
            }
        }

        // Sección 4: Especificaciones
        if (!orden.getEspecificaciones().isEmpty()) {
            sb.append("\n──── ESPECIFICACIONES ────\n");
            for (EspecificacionTrabajo e : orden.getEspecificaciones()) {
                sb.append("  ○ ").append(e.toString()).append("\n");
            }
        }

        // Sección 6: Liquidaciones (si existen)
        boolean tieneLiquidacion = false;

        if (!orden.getLiquidacionesOperativas().isEmpty()) {
            tieneLiquidacion = true;
            sb.append("\n──── LIQUIDACIÓN OPERATIVA ────\n");
            double totalOp = 0;
            for (LiquidacionOperativa lo : orden.getLiquidacionesOperativas()) {
                sb.append("  ").append(lo.toString()).append("\n");
                totalOp += lo.calcularTotal();
            }
            sb.append("  SUBTOTAL OPERATIVO: $").append(String.format("%.2f", totalOp)).append("\n");
        }

        if (!orden.getLiquidacionesAdicionales().isEmpty()) {
            tieneLiquidacion = true;
            sb.append("\n──── LIQUIDACIÓN ADICIONAL ────\n");
            double totalAd = 0;
            for (LiquidacionAdicional la : orden.getLiquidacionesAdicionales()) {
                sb.append("  ").append(la.toString()).append("\n");
                totalAd += la.calcularTotal();
            }
            sb.append("  SUBTOTAL ADICIONAL: $").append(String.format("%.2f", totalAd)).append("\n");
        }

        if (orden.getLiquidacionFinal() != null) {
            tieneLiquidacion = true;
            sb.append("\n──── LIQUIDACIÓN FINAL ────\n");
            sb.append("  ").append(orden.getLiquidacionFinal().toString()).append("\n");
        }

        if (!tieneLiquidacion) {
            sb.append("\n[Esta orden aún no ha sido liquidada]\n");
        }

        sb.append("\n════════════════════════════════════════");

        JOptionPane.showMessageDialog(
                null,
                sb.toString(),
                "Detalle de Orden",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
