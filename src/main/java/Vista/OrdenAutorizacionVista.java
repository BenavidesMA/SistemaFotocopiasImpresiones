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
public class OrdenAutorizacionVista {
 
    private OrdenRepo ordenRepo;
    private SolicitanteRepo solicitanteRepo;
    private DependenciaRepo dependenciaRepo;
 
    public OrdenAutorizacionVista(OrdenRepo ordenRepo,
                                   SolicitanteRepo solicitanteRepo,
                                   DependenciaRepo dependenciaRepo) {
        this.ordenRepo        = ordenRepo;
        this.solicitanteRepo  = solicitanteRepo;
        this.dependenciaRepo  = dependenciaRepo;
    }
 
    /**
     * Flujo completo de creación de una orden.
     * Recibe el usuario actual (quien está creando la orden).
     */
    public void crearOrden(Solicitante usuarioActual) {
        JOptionPane.showMessageDialog(
            null,
            "═══ CREAR NUEVA ORDEN ═══\n\n"
          + "A continuación se guiará por las secciones del formulario:\n"
          + "  1. Datos básicos\n"
          + "  2. Servicios\n"
          + "  3. Trabajos solicitados\n"
          + "  4. Especificaciones del papel\n\n"
          + "Puede cancelar en cualquier momento presionando Cancelar.",
            "Crear Orden",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        // PASO 1: Datos básicos (sección 1)
        OrdenAutorizacion orden = crearDatosBasicos(usuarioActual);
        if (orden == null) return; // Canceló
 
        // PASO 2: Servicios (sección 2)
        if (!agregarServicios(orden)) return; // Canceló
 
        // PASO 3: Trabajos/Solicitudes (sección 3)
        if (!agregarSolicitudes(orden)) return; // Canceló
 
        // PASO 4: Especificaciones (sección 4)
        if (!agregarEspecificaciones(orden)) return; // Canceló
 
        // Guardar orden
        if (ordenRepo.agregar(orden)) {
            ordenRepo.dbRegistrarOrden(orden);
            ordenRepo.dbRegistrarServicios(orden);
            JOptionPane.showMessageDialog(
                null,
                "✓ Orden creada exitosamente.\n\n"
              + orden.toString() + "\n\n"
              + "La orden queda pendiente de liquidación.\n"
              + "Un operario de publicaciones deberá registrar\n"
              + "la sección 6 (liquidación) posteriormente.",
                "Orden Creada",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            mostrarError("No se pudo guardar la orden.");
        }
    }
 
    /**
     * PASO 1: Crea la orden con los datos básicos (sección 1).
     */
    private OrdenAutorizacion crearDatosBasicos(Solicitante usuarioActual) {
        // Generar número de orden automático
        int numOrden = ordenRepo.dbObtenerSiguienteNumOrden();
 
        JOptionPane.showMessageDialog(
            null,
            "═══ SECCIÓN 1: DATOS BÁSICOS ═══\n\n"
          + "Número de orden asignado: " + String.format("%05d", numOrden),
            "Crear Orden - Paso 1/4",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        // Seleccionar tipo de orden
        TipoOrden tipoOrden = seleccionarTipoOrden();
        if (tipoOrden == null) return null; // Canceló
 
        // Solicitar fecha (formato dd-mm-aaaa)
        String fecha = JOptionPane.showInputDialog(
            null,
            "Tipo: " + tipoOrden.getDescripcion() + "\n\n"
          + "Ingrese la fecha de solicitud (aaaa-mm-dd):\n"
          + "Ejemplo: 2026-02-21",
            "Fecha de Solicitud",
            JOptionPane.QUESTION_MESSAGE
        );
 
        if (fecha == null) return null;
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            mostrarError("Formato de fecha inválido. Use aaaa-mm-dd");
            return null;
        }
 
        // Solicitar firma autorizada
        String firma = JOptionPane.showInputDialog(
            null,
            "Ingrese el nombre de quien autoriza (firma, 1-20 caracteres):",
            "Firma Autorizada",
            JOptionPane.QUESTION_MESSAGE
        );
 
        if (firma == null) return null;
        if (firma.trim().isEmpty()) {
            mostrarError("La firma no puede estar vacía.");
            return null;
        }
 
        // Solicitar observaciones (opcional)
        String obs = JOptionPane.showInputDialog(
            null,
            "Observaciones (opcional, 10-250 caracteres):\n"
          + "Deje vacío si no tiene observaciones.",
            "Observaciones",
            JOptionPane.QUESTION_MESSAGE
        );
 
        if (obs == null) obs = "";
 
        // Crear la orden
        OrdenAutorizacion orden = new OrdenAutorizacion(
            numOrden,
            tipoOrden,
            fecha.trim(),
            obs.trim(),
            firma.trim(),
            usuarioActual.getNombre(),
            usuarioActual.getApellido()
        );
 
        // Validar
        if (!orden.esValido()) {
            JOptionPane.showMessageDialog(
                null,
                "Datos inválidos:\n\n" + orden.getMensajeValidacion(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
 
        return orden;
    }
 
    /**
     * PASO 2: Agrega servicios a la orden (sección 2).
     */
    private boolean agregarServicios(OrdenAutorizacion orden) {
        JOptionPane.showMessageDialog(
            null,
            "═══ SECCIÓN 2: SERVICIOS ═══\n\n"
          + "Seleccione los servicios que aplicarán a esta orden.\n"
          + "Puede seleccionar múltiples servicios.",
            "Crear Orden - Paso 2/4",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        boolean continuar = true;
        while (continuar) {
            // Mostrar menú de servicios
            String menu = TipoServicio.listarServicios()
                        + "\n0. Terminar selección de servicios\n\n"
                        + "Ingrese el número del servicio:";
 
            String input = JOptionPane.showInputDialog(
                null,
                menu,
                "Seleccionar Servicios",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (input == null) return false; // Canceló
 
            try {
                int opcion = Integer.parseInt(input.trim());
                
                if (opcion == 0) {
                    if (orden.getServicios().isEmpty()) {
                        mostrarError("Debe seleccionar al menos un servicio.");
                        continue;
                    }
                    continuar = false;
                } else {
                    TipoServicio servicio = TipoServicio.porNumero(opcion);
                    if (servicio == null) {
                        mostrarError("Opción inválida.");
                    } else {
                        // Verificar duplicado
                        boolean existe = false;
                        for (Servicio s : orden.getServicios()) {
                            if (s.getServicioSeleccionado() == servicio) {
                                existe = true;
                                break;
                            }
                        }
                        
                        if (existe) {
                            mostrarError("Este servicio ya fue agregado.");
                        } else {
                            orden.agregarServicio(new Servicio(orden.getNumOrden(), servicio));
                            JOptionPane.showMessageDialog(
                                null,
                                "✓ Servicio agregado: " + servicio.getDescripcion(),
                                "Servicio Agregado",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                }
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número válido.");
            }
        }
 
        return true;
    }
 
    /**
     * PASO 3: Agrega trabajos/solicitudes a la orden (sección 3).
     */
    private boolean agregarSolicitudes(OrdenAutorizacion orden) {
        JOptionPane.showMessageDialog(
            null,
            "═══ SECCIÓN 3: TRABAJOS SOLICITADOS ═══\n\n"
          + "Registre cada trabajo que desea procesar.\n"
          + "Cada trabajo tiene un título, número de originales\n"
          + "y número de copias/impresiones.",
            "Crear Orden - Paso 3/4",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        boolean continuar = true;
        while (continuar) {
            // Solicitar título
            String titulo = JOptionPane.showInputDialog(
                null,
                "Ingrese el título del trabajo (3-40 caracteres):\n"
              + "O escriba 'fin' para terminar.",
                "Título del Trabajo",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (titulo == null) return false; // Canceló
            
            if (titulo.trim().equalsIgnoreCase("fin")) {
                if (orden.getSolicitudes().isEmpty()) {
                    mostrarError("Debe registrar al menos un trabajo.");
                    continue;
                }
                continuar = false;
                continue;
            }
 
            if (titulo.trim().isEmpty()) {
                mostrarError("El título no puede estar vacío.");
                continue;
            }
 
            // Solicitar originales
            String inputOrig = JOptionPane.showInputDialog(
                null,
                "Título: " + titulo + "\n\n"
              + "Número de originales (1-500):",
                "Originales",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (inputOrig == null) return false;
 
            int original;
            try {
                original = Integer.parseInt(inputOrig.trim());
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número válido.");
                continue;
            }
 
            // Solicitar reproducciones (copias o impresiones)
            String tipoReprod = orden.getTipoOrden() == TipoOrden.FOTOCOPIAS 
                              ? "copias" 
                              : "impresiones";
            
            String inputReprod = JOptionPane.showInputDialog(
                null,
                "Título: " + titulo + "\n"
              + "Originales: " + original + "\n\n"
              + "Número de " + tipoReprod + " (1-500):",
                "Reproducciones",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (inputReprod == null) return false;
 
            int reproducciones;
            try {
                reproducciones = Integer.parseInt(inputReprod.trim());
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número válido.");
                continue;
            }
 
            // Crear solicitud
            Solicitud sol = new Solicitud(
                titulo.trim(),
                orden.getNumOrden(),
                original,
                reproducciones
            );
 
            // Validar
            if (!sol.esValido()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Datos inválidos:\n\n" + sol.getMensajeValidacion(),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
 
            orden.agregarSolicitud(sol);
            JOptionPane.showMessageDialog(
                null,
                "✓ Trabajo agregado exitosamente.",
                "Trabajo Agregado",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
 
        return true;
    }
 
    /**
     * PASO 4: Agrega especificaciones del papel (sección 4).
     */
    private boolean agregarEspecificaciones(OrdenAutorizacion orden) {
        JOptionPane.showMessageDialog(
            null,
            "═══ SECCIÓN 4: ESPECIFICACIONES DEL PAPEL ═══\n\n"
          + "Registre las especificaciones del papel.\n"
          + "Puede agregar CARTA y/o OFICIO.",
            "Crear Orden - Paso 4/4",
            JOptionPane.INFORMATION_MESSAGE
        );
 
        boolean continuar = true;
        while (continuar) {
            // Seleccionar formato
            String[] formatos = {"CARTA", "OFICIO", "Terminar"};
            String formato = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el formato de papel:",
                "Formato de Papel",
                JOptionPane.QUESTION_MESSAGE,
                null,
                formatos,
                formatos[0]
            );
 
            if (formato == null) return false; // Canceló
 
            if (formato.equals("Terminar")) {
                if (orden.getEspecificaciones().isEmpty()) {
                    mostrarError("Debe registrar al menos una especificación.");
                    continue;
                }
                continuar = false;
                continue;
            }
 
            // Verificar duplicado
            boolean existe = false;
            for (EspecificacionTrabajo e : orden.getEspecificaciones()) {
                if (e.getFormatoPapel().equalsIgnoreCase(formato)) {
                    existe = true;
                    break;
                }
            }
 
            if (existe) {
                mostrarError("Ya registró especificaciones para " + formato);
                continue;
            }
 
            // Preguntar si trae papel
            int traePapelResp = JOptionPane.showConfirmDialog(
                null,
                "Formato: " + formato + "\n\n"
              + "¿Trae el papel?",
                "Trae Papel",
                JOptionPane.YES_NO_OPTION
            );
 
            if (traePapelResp == JOptionPane.CLOSED_OPTION) return false;
 
            boolean traePapel = (traePapelResp == JOptionPane.YES_OPTION);
 
            // Solicitar cantidad
            String inputCant = JOptionPane.showInputDialog(
                null,
                "Formato: " + formato + "\n"
              + "Trae papel: " + (traePapel ? "Sí" : "No") + "\n\n"
              + "Cantidad de hojas (1-500):",
                "Cantidad",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (inputCant == null) return false;
 
            int cantidad;
            try {
                cantidad = Integer.parseInt(inputCant.trim());
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número válido.");
                continue;
            }
 
            // Solicitar tipo de papel
            String tipoPapel = JOptionPane.showInputDialog(
                null,
                "Formato: " + formato + "\n"
              + "Cantidad: " + cantidad + "\n\n"
              + "Tipo de papel (4-31 caracteres):\n"
              + "Ejemplos: Bond 75g, Reciclado, Glossy",
                "Tipo de Papel",
                JOptionPane.QUESTION_MESSAGE
            );
 
            if (tipoPapel == null) return false;
 
            if (tipoPapel.trim().isEmpty()) {
                mostrarError("El tipo de papel no puede estar vacío.");
                continue;
            }
 
            // Crear especificación
            EspecificacionTrabajo esp = new EspecificacionTrabajo(
                orden.getNumOrden(),
                formato,
                traePapel,
                cantidad,
                tipoPapel.trim()
            );
 
            // Validar
            if (!esp.esValido()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Datos inválidos:\n\n" + esp.getMensajeValidacion(),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
 
            orden.agregarEspecificacion(esp);
            JOptionPane.showMessageDialog(
                null,
                "✓ Especificación agregada exitosamente.",
                "Especificación Agregada",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
 
        return true;
    }
 
    /**
     * Muestra un selector de tipo de orden (Fotocopias o Impresiones).
     */
    private TipoOrden seleccionarTipoOrden() {
        String[] opciones = {
            TipoOrden.FOTOCOPIAS.getDescripcion(),
            TipoOrden.IMPRESIONES.getDescripcion()
        };
 
        String seleccion = (String) JOptionPane.showInputDialog(
            null,
            "Seleccione el tipo de orden:",
            "Tipo de Orden",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
 
        if (seleccion == null) return null;
 
        if (seleccion.equals(TipoOrden.FOTOCOPIAS.getDescripcion())) {
            return TipoOrden.FOTOCOPIAS;
        } else {
            return TipoOrden.IMPRESIONES;
        }
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
