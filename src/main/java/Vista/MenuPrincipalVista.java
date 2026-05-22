/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import javax.swing.JOptionPane;
import Modelo.*;

/**
 *
 * @author Miguel
 */
public class MenuPrincipalVista {

    private Solicitante usuarioActual;
    private DependenciaRepo dependenciaRepo;
    private SolicitanteRepo solicitanteRepo;
    private OrdenRepo ordenRepo;
    private MaquinaRepo maquinaRepo;
    private AuxiliarRepo auxiliarRepo;
    private SolicitanteVista solicitanteVista;

    private DependenciaVista dependenciaVista;
    private OrdenAutorizacionVista ordenVista;
    private ConsultaVista consultaVista;
    private LiquidacionVista liquidacionVista;

    public MenuPrincipalVista(Solicitante usuarioActual,
                               DependenciaRepo dependenciaRepo,
                               SolicitanteRepo solicitanteRepo,
                               OrdenRepo ordenRepo,
                               MaquinaRepo maquinaRepo,
                               AuxiliarRepo auxiliarRepo) {
        this.usuarioActual   = usuarioActual;
        this.dependenciaRepo = dependenciaRepo;
        this.solicitanteRepo = solicitanteRepo;
        this.ordenRepo       = ordenRepo;
        this.maquinaRepo     = maquinaRepo;
        this.auxiliarRepo    = auxiliarRepo;
        

        this.dependenciaVista = new DependenciaVista(dependenciaRepo);
        this.ordenVista       = new OrdenAutorizacionVista(ordenRepo, solicitanteRepo, dependenciaRepo);
        this.consultaVista    = new ConsultaVista(ordenRepo);
        this.liquidacionVista = new LiquidacionVista(ordenRepo, maquinaRepo, auxiliarRepo);
        this.solicitanteVista = new SolicitanteVista(solicitanteRepo, dependenciaRepo);
    }

    /** Punto de entrada: delega al menú según rol. */
    public void mostrar() {
        if (usuarioActual.getTipoUsuario() == TipoUsuario.OPERARIO_PUBLICACIONES) {
            mostrarMenuOperario();
        } else {
            mostrarMenuSolicitante();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // MENÚ SOLICITANTE  (opciones 1-3)
    // ══════════════════════════════════════════════════════════════════════

    private void mostrarMenuSolicitante() {
        int opcion;
        do {
            String input = JOptionPane.showInputDialog(
                null,
                encabezado()
              + "1. Crear Nueva Orden\n"
              + "2. Salir\n"
              + separador()
              + "Elija una opción:",
                "Menú Principal - UAO",
                JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) { opcion = 2; continue; }

            try { opcion = Integer.parseInt(input.trim()); }
            catch (NumberFormatException e) { opcion = -1; }

            switch (opcion) {
                case 1: ordenVista.crearOrden(usuarioActual);          break;
                case 2: despedida();                                    break;
                default: mostrarError("Opción inválida.");              break;
            }
        } while (opcion != 2);
    }

    // ══════════════════════════════════════════════════════════════════════
    // MENÚ OPERARIO  (opciones 1-3)
    // ══════════════════════════════════════════════════════════════════════
 

    private void mostrarMenuOperario() {
    int opcion;
    do {
        String input = JOptionPane.showInputDialog(
            null,
            encabezado()
          + "1. Gestionar Dependencias\n"
          + "2. Gestionar Solicitantes\n"   // RESTAURADA
          + "3. Gestionar Solicitudes\n"
          + "4. Salir\n"
          + separador()
          + "Elija una opción:",
            "Menú Operario - UAO", JOptionPane.QUESTION_MESSAGE
        );

        if (input == null) { opcion = 4; continue; }
        try { opcion = Integer.parseInt(input.trim()); }
        catch (NumberFormatException e) { opcion = -1; }

        switch (opcion) {
            case 1: dependenciaVista.gestionarDependencias();    break;
            case 2: solicitanteVista.gestionarSolicitantes();    break;
            case 3: gestionarSolicitudesOperario();              break;
            case 4: despedida();                                 break;
            default: mostrarError("Opción inválida.");           break;
        }
    } while (opcion != 4);
}

    /**
     * Sub-menú "Gestionar Solicitudes" exclusivo para operarios:
     * consultar órdenes + completar liquidaciones pendientes.
     */
    private void gestionarSolicitudesOperario() {
        int opcion;
        do {
            String input = JOptionPane.showInputDialog(
                null,
                "═══ GESTIONAR SOLICITUDES ═══\n\n"
              + "1. Consultar Órdenes\n"
              + "2. Liquidar Órdenes Pendientes\n"
              + "3. Volver\n\n"
              + "Elija una opción:",
                "Gestionar Solicitudes - UAO",
                JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) return;

            try { opcion = Integer.parseInt(input.trim()); }
            catch (NumberFormatException e) { opcion = -1; }

            switch (opcion) {
                case 1: consultaVista.consultarOrdenes(usuarioActual); break;
                case 2: liquidacionVista.gestionarLiquidacion();       break;
                case 3:                                                 break;
                default: mostrarError("Opción inválida.");             break;
            }
        } while (opcion != 3);
    }

    // ══════════════════════════════════════════════════════════════════════
    // Helpers de texto
    // ══════════════════════════════════════════════════════════════════════

    private String encabezado() {
        return "╔════════════════════════════════════════════╗\n"
             + "║   SISTEMA DE FOTOCOPIAS E IMPRESIONES UAO   ║\n"
             + "╚════════════════════════════════════════════╝\n\n"
             + "Usuario: " + usuarioActual.getNombre() + " " + usuarioActual.getApellido() + "\n"
             + "Rol:     " + usuarioActual.getTipoUsuario().getDescripcion() + "\n\n"
             + separador();
    }

    private String separador() {
        return "───────────────────────────────────────────\n";
    }

    private void despedida() {
        JOptionPane.showMessageDialog(null,
            "Cerrando sesión de " + usuarioActual.getNombre() + " "
          + usuarioActual.getApellido() + ".\n\n¡Hasta pronto!",
            "Salir", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
