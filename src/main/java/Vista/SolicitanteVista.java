/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JOptionPane;
import Modelo.*;
import java.util.List;

/**
 *
 * @author Miguel
 */
public class SolicitanteVista {

    private SolicitanteRepo solicitanteRepo;
    private DependenciaRepo dependenciaRepo;
    private DependenciaVista dependenciaVista;

    public SolicitanteVista(SolicitanteRepo solicitanteRepo,
            DependenciaRepo dependenciaRepo) {
        this.solicitanteRepo = solicitanteRepo;
        this.dependenciaRepo = dependenciaRepo;
        this.dependenciaVista = new DependenciaVista(dependenciaRepo);
    }

    /**
     * Menú de gestión de solicitantes.
     */
    public void gestionarSolicitantes() {
        int opcion;
        do {
            String input = JOptionPane.showInputDialog(
                    null,
                    "═══ GESTIÓN DE SOLICITANTES ═══\n\n"
                    + "1. Registrar Nuevo Solicitante\n"
                    + "2. Listar Solicitantes (datos básicos)\n"
                    + "3. Listar Solicitantes (con dependencia)\n" // NUEVA
                    + "4. Volver al Menú Principal\n\n"
                    + "Elija una opción:",
                    "Solicitantes - UAO", JOptionPane.QUESTION_MESSAGE
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
                    registrarSolicitante();
                    break;
                case 2:
                    listarSolicitantes();
                    break;
                case 3:
                    listarSolicitantesCompleto();
                    break;  // NUEVA
                case 4:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (opcion != 4);
    }

// MÉTODO NUEVO — equivalente a SELECT con INNER JOIN sobre dependencia
    private void listarSolicitantesCompleto() {
    List<Solicitante> sols = solicitanteRepo.listarTodos();

    if (sols.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay solicitantes registrados.",
            "Solicitantes", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("═══ SOLICITANTES CON DEPENDENCIA ═══\n");
    sb.append("Total: ").append(sols.size()).append("\n\n");

    for (Solicitante s : sols) {
        sb.append("────────────────────────────────\n");
        sb.append("Solicitante: ").append(s.getNombre()).append(" ")
          .append(s.getApellido()).append("\n");
        sb.append("Extensión:   ").append(s.getExtension()).append("\n");
        sb.append("Cargo:       ").append(s.getCargo()).append("\n");
        sb.append("Rol:         ").append(s.getTipoUsuario().getDescripcion()).append("\n");

        Dependencia dep = dependenciaRepo.buscarPorNombre(s.getNombreDependencia());
        if (dep != null) {
            sb.append("── Dependencia ──\n");
            sb.append("  Nombre:       ").append(dep.getNombre()).append("\n");
            sb.append("  Centro costo: ").append(dep.getCentroCosto()).append("\n");
        } else {
            sb.append("  [Dependencia no encontrada]\n");
        }
        sb.append("\n");
    }

    // ── Componente con scroll ──────────────────────────────────────────────
    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(480, 400)); // ancho x alto visible

    JOptionPane.showMessageDialog(
        null,
        scrollPane,
        "Solicitantes con Dependencia",
        JOptionPane.INFORMATION_MESSAGE
    );
}

    /**
     * Registra un nuevo solicitante (usuario del sistema).
     */
    private void registrarSolicitante() {
        // 1. Solicitar nombre
        String nombre = JOptionPane.showInputDialog(
                null,
                "═══ REGISTRAR SOLICITANTE ═══\n\n"
                + "Ingrese el nombre (7-40 caracteres):",
                "Registrar Solicitante",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nombre == null) {
            return;
        }
        if (nombre.trim().isEmpty()) {
            mostrarError("El nombre no puede estar vacío.");
            return;
        }

        // 2. Solicitar apellido
        String apellido = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + "\n\n"
                + "Ingrese el apellido (7-40 caracteres):",
                "Registrar Solicitante",
                JOptionPane.QUESTION_MESSAGE
        );

        if (apellido == null) {
            return;
        }
        if (apellido.trim().isEmpty()) {
            mostrarError("El apellido no puede estar vacío.");
            return;
        }

        // 3. Solicitar extensión (username único)
        String extension = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + " " + apellido + "\n\n"
                + "Ingrese la extensión telefónica (5 dígitos: 00100-09999):\n"
                + "Esta será su usuario para iniciar sesión.",
                "Registrar Solicitante",
                JOptionPane.QUESTION_MESSAGE
        );

        if (extension == null) {
            return;
        }
        if (extension.trim().isEmpty()) {
            mostrarError("La extensión no puede estar vacía.");
            return;
        }

        // Verificar si ya existe
        if (solicitanteRepo.existe(extension.trim())) {
            mostrarError("Ya existe un solicitante con esa extensión.");
            return;
        }

        // 4. Solicitar cargo
        String cargo = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + " " + apellido + "\n"
                + "Extensión: " + extension + "\n\n"
                + "Ingrese el cargo (7-20 caracteres):",
                "Registrar Solicitante",
                JOptionPane.QUESTION_MESSAGE
        );

        if (cargo == null) {
            return;
        }
        if (cargo.trim().isEmpty()) {
            mostrarError("El cargo no puede estar vacío.");
            return;
        }

        // 5. Solicitar contraseña
        String password = JOptionPane.showInputDialog(
                null,
                "Nombre: " + nombre + " " + apellido + "\n"
                + "Extensión: " + extension + "\n"
                + "Cargo: " + cargo + "\n\n"
                + "Ingrese una contraseña:",
                "Registrar Solicitante",
                JOptionPane.QUESTION_MESSAGE
        );

        if (password == null) {
            return;
        }
        if (password.trim().isEmpty()) {
            mostrarError("La contraseña no puede estar vacía.");
            return;
        }

        // 6. Seleccionar dependencia
        String nombreDependencia = dependenciaVista.seleccionarDependencia();
        if (nombreDependencia == null) {
            return;
        }

        // Crear y validar
        Solicitante sol = new Solicitante(
                nombre.trim(),
                apellido.trim(),
                extension.trim(),
                cargo.trim(),
                password,
                nombreDependencia
        );

        if (!sol.esValido()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Datos inválidos:\n\n" + sol.getMensajeValidacion(),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Agregar
        if (solicitanteRepo.dbRegistrar(sol)) {
            JOptionPane.showMessageDialog(
                    null,
                    "✓ Solicitante registrado exitosamente.\n\n"
                    + sol.toString() + "\n\n"
                    + "Puede iniciar sesión con:\n"
                    + "Usuario: " + extension + "\n"
                    + "Contraseña: [la que acaba de crear]",
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            mostrarError("No se pudo registrar el solicitante.");
        }
    }

    /**
     * Lista todos los solicitantes registrados.
     */
    private void listarSolicitantes() {
        List<Solicitante> sols = solicitanteRepo.dbConsultarBasicosTodos();

        if (sols.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No hay solicitantes registrados.",
                    "Solicitantes",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("═══ SOLICITANTES REGISTRADOS ═══\n");
        sb.append("Total: ").append(sols.size()).append("\n\n");

        for (Solicitante s : sols) {
            sb.append("• ").append(s.toString()).append("\n");
        }

        JOptionPane.showMessageDialog(
                null,
                sb.toString(),
                "Solicitantes",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de error.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
