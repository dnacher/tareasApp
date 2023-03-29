package com.software.tareasApp.utils;

import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.view.constantes.ConstantePermisos;
import javafx.scene.control.Button;

public class UtilsSeguridad {

    public static boolean traePermisos(String pagina,ConstantePermisos permiso){
        return TareaAppApplication.permisosUsuario.stream()
                .filter(pu -> pu.getPagina().equals(pagina))
                .anyMatch(pu -> permiso.getPermisoValorSinComision() == pu.getPermiso() ||
                        permiso.getPermisoValorConComision() == pu.getPermiso());
    }

    private static void traePermisosEditar(Button btnAgregar, Button btnGuardar, Button btnActualizar, String pagina){
        boolean active = getEditar(pagina);
        btnAgregar.setDisable(!active);
        btnGuardar.setDisable(!active);
        btnActualizar.setDisable(!active);
    }

    public static boolean get(String pagina, ConstantePermisos permiso){
        switch(permiso) {
            case PERMISO_ADMIN:
                return getAdmin(pagina);
            case PERMISO_OPERADOR:
                return getEditar(pagina);
            default:
                return false;
        }
    }

    public static void setDisable(boolean disable, String pagina, Button btnActualizar, Button btnEliminar, Button btnEndoso){
        boolean permiso = get(pagina, ConstantePermisos.PERMISO_OPERADOR);
        boolean permisoAdmin = get(pagina, ConstantePermisos.PERMISO_ADMIN);
        if(!permiso){
            btnActualizar.setDisable(true);
            if(btnEndoso!=null) btnEndoso.setDisable(true);
        }else{
            btnActualizar.setDisable(disable);
            if(btnEndoso!=null) btnEndoso.setDisable(disable);
        }
        if(!permisoAdmin){
            btnEliminar.setDisable(true);
        }else{
            btnEliminar.setDisable(disable );
        }
    }

    public static boolean getEditar(String pagina){
        return TareaAppApplication.permisosUsuario.stream()
                .filter(pu -> pu.getPagina().equals(pagina))
                .anyMatch(pu -> pu.getPermiso() >= 3 && pu.getPermiso()!=9);
    }

    public static boolean getAdmin(String pagina){
        return TareaAppApplication.permisosUsuario.stream()
                .filter(pu -> pu.getPagina().equals(pagina))
                .anyMatch(pu -> pu.getPermiso() > 6 && pu.getPermiso()!=9);
    }

    private static void traePermisosAdministrador(Button btnEliminar, String pagina){
        boolean active = getAdmin(pagina);
        btnEliminar.setDisable(!active);
    }

    public static boolean traePermisoComision(String pagina){
        return TareaAppApplication.permisosUsuario.stream()
                .filter(pu -> pu.getPagina().equals(pagina))
                .anyMatch(pu -> pu.getPermiso()>=9);
    }

    public static void setPermisoXPermiso(Button button, String pagina, ConstantePermisos permiso){
       button.setDisable(!traePermisos(pagina, permiso));
    }

    public static void setPermisoAdmin(Button button, String pagina){
        button.setDisable(traePermisos(pagina, ConstantePermisos.PERMISO_ADMIN));
    }

    public static boolean hasPermiso(String pagina, ConstantePermisos permiso){
        return traePermisos(pagina, permiso);
    }

    public static void setButtons(Button btnActualizar, Button btnEliminar, boolean disable, String pagina){
        boolean permiso = UtilsSeguridad.hasPermiso(pagina, ConstantePermisos.PERMISO_ADMIN);
        btnActualizar.setDisable(disable);
        btnEliminar.setDisable(disable);
        if(!permiso){
            btnActualizar.setDisable(true);
            btnEliminar.setDisable(true);
        }else{
            btnActualizar.setDisable(disable);
            btnEliminar.setDisable(disable);
        }
    }

    private static int getPermisoValor(String pagina){
        return TareaAppApplication.permisosUsuario.stream()
                .filter(pu -> pu.getPagina().equals(pagina)).findFirst().get().getPermiso();
    }

    public static int seguridad(Button btnAgregar, Button btnGuardar, Button btnActualizar, Button btnEliminar, String pagina){
        traePermisosEditar(btnAgregar, btnGuardar, btnActualizar, pagina);
        traePermisosAdministrador(btnEliminar, pagina);
        return getPermisoValor(pagina);
    }

    public static boolean hasEditorPermisos(Button btnAgregar, Button btnActualizar, Button btnGuardar, String pagina){
        return TareaAppApplication.permisosUsuario.stream()
                .filter(pu -> pu.getPagina().equals(pagina))
                .anyMatch(pu -> pu.getPermiso()>=3 && pu.getPermiso()!=9);
    }

}
