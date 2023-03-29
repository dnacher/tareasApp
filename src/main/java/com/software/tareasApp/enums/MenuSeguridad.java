package com.software.tareasApp.enums;

import com.software.tareasApp.controller.SeguridadController;
import com.software.tareasApp.controller.TipoUsuariosController;
import com.software.tareasApp.controller.UsuariosController;

/**
 *
 * @author Daniel
 */

public enum MenuSeguridad {

    Inicio("Inicio","Inicio","Principal",null),
    Usuarios("Usuarios", "Usuarios","Seguridad", UsuariosController.class),
    TipoUsuarios("TipoUsuarios", "Tipo de usuarios","Seguridad", TipoUsuariosController.class),
    Permisos("Permisos", "Permisos","Seguridad", SeguridadController.class);

    private final String pagina;
    private final String menu;
	private final String carpeta;
	private final Class controller;

    MenuSeguridad(String pagina, String menu, String carpeta, Class controller) {
        this.pagina = pagina;
        this.menu = menu;
		this.carpeta = carpeta;
		this.controller = controller;
    }

    public String getPagina() {
        return pagina;
    }

    public String getMenu() {
        return menu;
    }

    public Class getController() {
        return controller;
    }
}
