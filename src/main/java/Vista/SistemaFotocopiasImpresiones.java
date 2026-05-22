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
            return;
        }

        DependenciaRepo dependenciaRepo = new DependenciaRepo();
        SolicitanteRepo solicitanteRepo = new SolicitanteRepo();
        OrdenRepo ordenRepo = new OrdenRepo();
        MaquinaRepo maquinaRepo = new MaquinaRepo();
        AuxiliarRepo auxiliarRepo = new AuxiliarRepo();

        precargarSolicitantes(solicitanteRepo);
        precargarMaquinas(maquinaRepo);
        precargarAuxiliares(auxiliarRepo);

        LoginVista loginVista = new LoginVista(solicitanteRepo);

        while (true) {
            Solicitante usuarioActual = loginVista.mostrarLogin();

            if (usuarioActual == null) {
                break;
            }

            MenuPrincipalVista menuPrincipal = new MenuPrincipalVista(
                    usuarioActual,
                    dependenciaRepo,
                    solicitanteRepo,
                    ordenRepo,
                    maquinaRepo,
                    auxiliarRepo
            );

            menuPrincipal.mostrar();

        }

        JOptionPane.showMessageDialog(
                null,
                "Gracias por usar el Sistema de Fotocopias e Impresiones UAO.\n¡Hasta pronto!",
                "Sistema UAO",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private static void precargarSolicitantes(SolicitanteRepo repo) {

        repo.agregar(new OperarioPublicaciones(
                "Luis Fernando", "Sánchez Cruz",
                "09001", "Operario",
                "Publicaciones"
        ));

        repo.agregar(new OperarioPublicaciones(
                "Sandra Milena", "Ramírez López",
                "09002", "Supervisora",
                "Publicaciones"
        ));
    }

    private static void precargarMaquinas(MaquinaRepo repo) {
        repo.agregar(new Maquina("MAQ-001", "Local Principal"));
        repo.agregar(new Maquina("MAQ-002", "Local Edificio B"));
        repo.agregar(new Maquina("MAQ-003", "Local Biblioteca"));
    }

    private static void precargarAuxiliares(AuxiliarRepo repo) {
        repo.agregar(new Auxiliar("AUX-001", "Andrés Felipe Gómez"));
        repo.agregar(new Auxiliar("AUX-002", "Luisa María Torres"));
    }
}
