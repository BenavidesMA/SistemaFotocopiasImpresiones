/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Vista;

import BaseDatos.*;
import javax.swing.*;
import Modelo.*;
/**
 *
 * @author Miguel
 */
public class SistemaFotocopiasImpresiones {
 
    public static void main(String[] args) {
        
    BaseDatos objBaseDatos = new BaseDatos();
    boolean conexion = objBaseDatos.crearConexion();
    if (!conexion) {
        JOptionPane.showMessageDialog(null,
            "No se pudo conectar a la base de datos. El sistema se cerrará.",
            "Error crítico", JOptionPane.ERROR_MESSAGE);
        return; // no tiene sentido continuar sin BD
    }

    // ── Repos se crean UNA sola vez, persisten durante toda la sesión ──
    DependenciaRepo dependenciaRepo = new DependenciaRepo();
    SolicitanteRepo solicitanteRepo = new SolicitanteRepo();
    OrdenRepo       ordenRepo       = new OrdenRepo();
    MaquinaRepo     maquinaRepo     = new MaquinaRepo();
    AuxiliarRepo    auxiliarRepo    = new AuxiliarRepo();

    precargarDependencias(dependenciaRepo);
    precargarSolicitantes(solicitanteRepo);
    precargarMaquinas(maquinaRepo);
    precargarAuxiliares(auxiliarRepo);

    LoginVista loginVista = new LoginVista(solicitanteRepo);
    
    // ── Ciclo principal: vuelve al login cada vez que alguien cierra sesión ──
    while (true) {
        Solicitante usuarioActual = loginVista.mostrarLogin();

        // Si cancela en la pantalla de tipo de usuario o excede intentos → salir
        if (usuarioActual == null) break;

        MenuPrincipalVista menuPrincipal = new MenuPrincipalVista(
            usuarioActual,
            dependenciaRepo,
            solicitanteRepo,
            ordenRepo,
            maquinaRepo,
            auxiliarRepo
        );
        
        
        menuPrincipal.mostrar();
        // Al terminar mostrar(), el ciclo regresa al login automáticamente
    }

    JOptionPane.showMessageDialog(
        null,
        "Gracias por usar el Sistema de Fotocopias e Impresiones UAO.\n¡Hasta pronto!",
        "Sistema UAO",
        JOptionPane.INFORMATION_MESSAGE
    );
}
 
    // ══════════════════════════════════════════════════════════════════════════
    // MÉTODOS PRIVADOS DE PRECARGA DE DATOS
    // ══════════════════════════════════════════════════════════════════════════
 
    /**
     * Precarga 5 dependencias de ejemplo.
     */
    private static void precargarDependencias(DependenciaRepo repo) {
        repo.agregar(new Dependencia("Ingeniería de Sistemas", "CC-IS-001"));
        repo.agregar(new Dependencia("Ciencias Básicas", "CC-CB-002"));
        repo.agregar(new Dependencia("Publicaciones", "CC-PUB-003"));
        repo.agregar(new Dependencia("Administración", "CC-ADM-004"));
        repo.agregar(new Dependencia("Humanidades", "CC-HUM-005"));
    }
 
    /**
     * Precarga 3 solicitantes normales y 2 operarios de publicaciones.
     *
     * POLIMORFISMO: Los operarios se crean con la clase OperarioPublicaciones
     * pero se almacenan en SolicitanteRepositorio (lista de tipo Solicitante).
     * El método getTipoUsuario() ejecuta la versión correcta en runtime.
     */
   private static void precargarSolicitantes(SolicitanteRepo repo) {

    repo.agregar(new Solicitante(
        "María José", "Rodríguez Gómez",
        "00234", "Profesor Asociado",   // 17 ✓ sin cambio
        "Ingeniería de Sistemas"
    ));

    repo.agregar(new Solicitante(
        "Carlos Alberto", "Méndez Torres",
        "01455", "Coordinador",          // 11 ✓ (antes: "Coordinador Académico")
        "Ciencias Básicas"
    ));

    repo.agregar(new Solicitante(
        "Ana Patricia", "Vargas Castro",
        "02876", "Asist. Administrativo", // 20 ✓ (antes: "Asistente Administrativo")
        "Administración"
    ));

    repo.agregar(new OperarioPublicaciones(
        "Luis Fernando", "Sánchez Cruz",
        "09001", "Operario",              // 8 ✓ (antes: "Operario Publicaciones")
        "admin001", "Publicaciones"
    ));

    repo.agregar(new OperarioPublicaciones(
        "Sandra Milena", "Ramírez López",
        "09002", "Supervisora",           // 11 ✓ (antes: "Supervisora Publicaciones")
        "admin002", "Publicaciones"
    ));
}
 
    /**
     * Precarga 3 máquinas de ejemplo.
     */
    private static void precargarMaquinas(MaquinaRepo repo) {
        repo.agregar(new Maquina("MAQ-001", "Local Principal"));
        repo.agregar(new Maquina("MAQ-002", "Local Edificio B"));
        repo.agregar(new Maquina("MAQ-003", "Local Biblioteca"));
    }
 
    /**
     * Precarga 2 auxiliares de ejemplo.
     */
    private static void precargarAuxiliares(AuxiliarRepo repo) {
        repo.agregar(new Auxiliar("AUX-001", "Andrés Felipe Gómez"));
        repo.agregar(new Auxiliar("AUX-002", "Luisa María Torres"));
    }
}
