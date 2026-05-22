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
public class ConsultaVista {

    private OrdenRepo ordenRepo;

    public ConsultaVista(OrdenRepo ordenRepo) {
        this.ordenRepo = ordenRepo;
    }

    public void consultarOrdenes(Solicitante usuarioActual) {
        int opcion;

        do {
            String input = JOptionPane.showInputDialog(
                    null,
                    "═══ CONSULTAR ÓRDENES ═══\n\n"
                    + "1. Buscar por Número de Orden\n"
                    + "2. Ver Todas las Órdenes Registradas\n"
                    + "3. Volver\n\n"
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
                    verTodasOrdenes();
                    break;

                case 3:
                    break;

                default:
                    mostrarError("Opción inválida.");
                    break;
            }

        } while (opcion != 3);
    }

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
            ArrayList<String> orden = ordenRepo.dbConsultarPorNumOrden(numOrden);

            if (orden == null) {
                mostrarError("No existe una orden con ese número.");
                return;
            }

            mostrarDetalleOrden(orden);

        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número válido.");
        }
    }

    private void verTodasOrdenes() {
        ArrayList<String> ordenes = ordenRepo.dbConsultarTodasOrdenes();

        if (ordenes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay órdenes registradas.",
                    "Órdenes",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══ TODAS LAS ÓRDENES ═══\n");
        sb.append("Total: ").append(ordenes.size()).append("\n\n");

        for (String orden : ordenes) {
            sb.append("• ").append(orden).append("\n\n");
        }

        JOptionPane.showMessageDialog(
                null,
                sb.toString(),
                "Todas las Órdenes",
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

    private void mostrarDetalleOrden(ArrayList<String> orden) {
        if (orden == null || orden.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No se encontró información para esta orden.",
                    "Sin resultados", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══ DETALLE DE ORDEN ═══\n\n");
        for (String fila : orden) {
            // Cada campo en su propia línea para mejor legibilidad
            String[] partes = fila.split(" \\| ");
            for (String parte : partes) {
                sb.append(parte).append("\n");
            }
            sb.append("\n");
        }

        JOptionPane.showMessageDialog(null,
                sb.toString(),
                "Detalle de Orden", JOptionPane.INFORMATION_MESSAGE);
    }
}
