/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.util.List;
import javax.swing.JOptionPane;
import Modelo.*;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class DependenciaVista {

    private DependenciaRepo dependenciaRepo;

    public DependenciaVista(DependenciaRepo dependenciaRepo) {
        this.dependenciaRepo = dependenciaRepo;
    }

    public void gestionarDependencias() {
        int opcion;
        do {
            String input = JOptionPane.showInputDialog(
                    null,
                    "═══ GESTIÓN DE DEPENDENCIAS ═══\n\n"
                    + "1. Registrar Nueva Dependencia\n"
                    + "2. Listar Todas las Dependencias\n"
                    + "3. Buscar Dependencia por Nombre\n" // NUEVA
                    + "4. Modificar Dependencia\n"
                    + "5. Eliminar Dependencia\n"
                    + "6. Volver al Menú Principal\n\n"
                    + "Elija una opción:",
                    "Dependencias - UAO", JOptionPane.QUESTION_MESSAGE
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
                    registrarDependencia();
                    break;
                case 2:
                    listarDependencias();
                    break;
                case 3:
                    buscarDependencia();
                    break;  // NUEVA
                case 4:
                    modificarDependencia();
                    break;
                case 5:
                    eliminarDependencia();
                    break;
                case 6:
                    break;
                default:
                    error("Opción inválida.");
                    break;
            }
        } while (opcion != 6);
    }

// MÉTODO NUEVO
    private void buscarDependencia() {
        String nombre = JOptionPane.showInputDialog(
                null,
                "Ingrese el nombre de la dependencia a buscar:",
                "Buscar Dependencia", JOptionPane.QUESTION_MESSAGE);

        if (nombre == null) return;

        Dependencia dep = dependenciaRepo.dbConsultarPorNombre(nombre.trim());

        if (dep == null) {
            error("No existe una dependencia con el nombre: " + nombre);
            return;
        }

        JOptionPane.showMessageDialog(null,
                "═══ DEPENDENCIA ENCONTRADA ═══\n\n" + dep.toString(),
                "Dependencia", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Registrar (sin cambios) ────────────────────────────────────────────
    private void registrarDependencia() {
        String nombre = JOptionPane.showInputDialog(null,
                "═══ REGISTRAR DEPENDENCIA ═══\n\n"
                + "Ingrese el nombre de la dependencia (3-40 caracteres):",
                "Registrar Dependencia", JOptionPane.QUESTION_MESSAGE);

        if (nombre == null) {
            return;
        }
        if (nombre.trim().isEmpty()) {
            error("El nombre no puede estar vacío.");
            return;
        }
        if (dependenciaRepo.existe(nombre.trim())) {
            error("Ya existe una dependencia con ese nombre.");
            return;
        }

        String centroCosto = JOptionPane.showInputDialog(null,
                "Nombre: " + nombre + "\n\nIngrese el centro de costo (3-12 caracteres):",
                "Registrar Dependencia", JOptionPane.QUESTION_MESSAGE);

        if (centroCosto == null) {
            return;
        }
        if (centroCosto.trim().isEmpty()) {
            error("El centro de costo no puede estar vacío.");
            return;
        }

        Dependencia dep = new Dependencia(nombre.trim(), centroCosto.trim());
        if (!dep.esValido()) {
            error("Datos inválidos:\n\n" + dep.getMensajeValidacion());
            return;
        }

        if (dependenciaRepo.dbRegistrar(dep)) {
            JOptionPane.showMessageDialog(null,
                    "✓ Dependencia registrada exitosamente.\n\n" + dep.toString(),
                    "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            error("No se pudo registrar la dependencia.");
        }
    }

    // ── Modificar (nuevo) ──────────────────────────────────────────────────
    private void modificarDependencia() {
        List<Dependencia> deps = dependenciaRepo.listarTodas();
        if (deps.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay dependencias registradas.",
                    "Modificar Dependencia", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Seleccionar cuál modificar
        String[] opciones = new String[deps.size()];
        for (int i = 0; i < deps.size(); i++) {
            opciones[i] = deps.get(i).getNombre();
        }

        String seleccion = (String) JOptionPane.showInputDialog(null,
                "Seleccione la dependencia a modificar:",
                "Modificar Dependencia", JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (seleccion == null) {
            return;
        }

        // Solicitar nuevo centro de costo
        String nuevoCentro = JOptionPane.showInputDialog(null,
                "Dependencia: " + seleccion + "\n\n"
                + "Ingrese el nuevo centro de costo (3-12 caracteres):",
                "Modificar Dependencia", JOptionPane.QUESTION_MESSAGE);

        if (nuevoCentro == null) {
            return;
        }
        if (nuevoCentro.trim().isEmpty()) {
            error("El centro de costo no puede estar vacío.");
            return;
        }

        // Validar con objeto temporal
        Dependencia temp = new Dependencia(seleccion, nuevoCentro.trim());
        if (!temp.esValido()) {
            error("Datos inválidos:\n\n" + temp.getMensajeValidacion());
            return;
        }

        // Aplicar cambio en el repo
        Dependencia dep = dependenciaRepo.dbConsultarPorNombre(seleccion);
        dep.setCentroCosto(nuevoCentro.trim());   // asume setter en Dependencia

        JOptionPane.showMessageDialog(null,
                "✓ Dependencia actualizada exitosamente.\n\n" + dep.toString(),
                "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Eliminar (nuevo) ───────────────────────────────────────────────────
    private void eliminarDependencia() {
        List<Dependencia> deps = dependenciaRepo.dbConsultarTodas();
        if (deps.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay dependencias registradas.",
                    "Eliminar Dependencia", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opciones = new String[deps.size()];
        for (int i = 0; i < deps.size(); i++) {
            opciones[i] = deps.get(i).getNombre();
        }

        String seleccion = (String) JOptionPane.showInputDialog(null,
                "Seleccione la dependencia a eliminar:",
                "Eliminar Dependencia", JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (seleccion == null) {
            return;
        }

        int conf = JOptionPane.showConfirmDialog(null,
                "¿Confirma eliminar la dependencia:\n\n  \"" + seleccion + "\"?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf != JOptionPane.YES_OPTION) {
            return;
        }

        if (dependenciaRepo.dbEliminar(seleccion)) {
            JOptionPane.showMessageDialog(null,
                    "✓ Dependencia eliminada exitosamente.",
                    "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            error("No se pudo eliminar la dependencia.");
        }
    }

    private void listarDependencias() {
   
        ArrayList<Dependencia> deps = dependenciaRepo.dbConsultarTodas();

        if (deps.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay dependencias registradas en la base de datos.",
                    "Dependencias", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("═══ DEPENDENCIAS EN BASE DE DATOS ═══\n");
        sb.append("Total: ").append(deps.size()).append("\n\n");
        for (Dependencia d : deps) {
            sb.append("• ").append(d.toString()).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(),
                "Dependencias", JOptionPane.INFORMATION_MESSAGE);
    }

    
    public String seleccionarDependencia() {
        ArrayList<Dependencia> deps = dependenciaRepo.dbConsultarTodas();
        if (deps.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No hay dependencias registradas.\nDebe registrar al menos una dependencia primero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String[] opciones = new String[deps.size()];
        for (int i = 0; i < deps.size(); i++) opciones[i] = deps.get(i).getNombre();

        return (String) JOptionPane.showInputDialog(null,
                "Seleccione una dependencia:", "Seleccionar Dependencia",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
