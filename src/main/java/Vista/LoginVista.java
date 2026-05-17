/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import BaseDatos.*;
import Modelo.*;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author Miguel
 */
public class LoginVista {

    private SolicitanteRepo solicitanteRepo;

    public LoginVista(SolicitanteRepo solicitanteRepo) {
        this.solicitanteRepo = solicitanteRepo;
    }
       
       
    /**
     * Muestra selector de tipo de usuario y luego autentica.
     *
     * @return el Solicitante autenticado, o null si canceló o excedió los
     * intentos.
     */
    public Solicitante mostrarLogin() {

        // ── PASO 0: Selección de tipo de usuario ──────────────────────────────
        String tipoInput = JOptionPane.showInputDialog(
                null,
                  "╔═══════════════════════════════════════╗\n"
                + "║  SISTEMA DE FOTOCOPIAS E IMPRESIONES ║\n"
                + "║   Universidad Autónoma de Occidente               ║\n"
                + "╚═══════════════════════════════════════╝\n\n"
                + "Seleccione su tipo de usuario:\n\n"
                + "1. Solicitante\n"
                + "2. Operario\n"
                + "3. Salir\n\n"
                + "Ingrese el número de la opción:",
                "Sistema UAO",
                JOptionPane.QUESTION_MESSAGE
        );

        if (tipoInput == null) {
            return null;
        }

        boolean esOperario;
        switch (tipoInput.trim()) {
            case "1":
                esOperario = false;
                break;
            case "2":
                esOperario = true;
                break;
            case "3":
                return null;  // el main interpreta null como cierre
            default:
                JOptionPane.showMessageDialog(null,
                        "Opción inválida.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
        }

        // ── PASO 1: Login ─────────────────────────────────────────────────────
        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            String extension = JOptionPane.showInputDialog(
                    null,
                    "Tipo de acceso: " + (esOperario ? "Operario" : "Solicitante") + "\n\n"
                    + "Ingrese su extensión telefónica (5 dígitos):",
                    "Login - UAO",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (extension == null) {
                return null;
            }

            if (extension.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "La extensión no puede estar vacía.",
                        "Error de Login", JOptionPane.ERROR_MESSAGE);
                intentos++;
                continue;
            }

            String password = JOptionPane.showInputDialog(
                    null,
                    "Extensión: " + extension + "\n\nIngrese su contraseña:",
                    "Login - UAO",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (password == null) {
                return null;
            }

            if (password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "La contraseña no puede estar vacía.",
                        "Error de Login", JOptionPane.ERROR_MESSAGE);
                intentos++;
                continue;
            }

            Solicitante usuario = solicitanteRepo.autenticar(extension.trim(), password);

            if (usuario != null) {
                // Verificar que el tipo coincida con la opción elegida
                boolean usuarioEsOperario
                        = usuario.getTipoUsuario() == TipoUsuario.OPERARIO_PUBLICACIONES;

                if (esOperario != usuarioEsOperario) {
                    intentos++;
                    int restantes = MAX_INTENTOS - intentos;
                    String msg = "Las credenciales no corresponden al tipo de usuario seleccionado.";
                    if (restantes > 0) {
                        msg += "\n\nIntentos restantes: " + restantes;
                    }
                    JOptionPane.showMessageDialog(null, msg,
                            "Error de Login", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                JOptionPane.showMessageDialog(null,
                        "¡Bienvenido(a), " + usuario.getNombre() + " "
                        + usuario.getApellido() + "!\n\n"
                        + "Rol: " + usuario.getTipoUsuario().getDescripcion(),
                        "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                return usuario;

            } else {
                intentos++;
                int restantes = MAX_INTENTOS - intentos;
                if (restantes > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Extensión o contraseña incorrecta.\n\nIntentos restantes: " + restantes,
                            "Error de Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        JOptionPane.showMessageDialog(null,
                "Ha excedido el número máximo de intentos.\nEl programa se cerrará.",
                "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        return null;
    }
}
