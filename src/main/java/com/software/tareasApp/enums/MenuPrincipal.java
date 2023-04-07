package com.software.tareasApp.enums;

import com.software.tareasApp.controller.CuentaAhorroController;
import com.software.tareasApp.controller.MainController;
import com.software.tareasApp.controller.TareasController;

/**
 *
 * @author Daniel
 */
public enum MenuPrincipal {

    Inicio("Inicio", "Inicio","Principal", MainController.class),
    Tareas("Tareas", "Tareas","Principal", TareasController.class),
    CuentaAhorro("CuentaAhorro", "Cuenta de Ahorro", "Principal", CuentaAhorroController.class),
    Seguridad("Seguridad", "Seguridad","Principal", null),;

    private final String pagina;
    private final String menu;
	private final String carpeta;
	private final Class controller;

    MenuPrincipal(String pagina, String menu, String carpeta, Class controller) {
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
