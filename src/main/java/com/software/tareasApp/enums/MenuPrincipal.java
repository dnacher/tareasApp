package com.software.tareasApp.enums;

import com.software.tareasApp.controller.*;

/**
 *
 * @author Daniel
 */
public enum MenuPrincipal {

    Inicio("Inicio", "Inicio","Principal", MainController.class),
    Clientes("Clientes", "Clientes","Principal", ClienteController.class),
    Polizas("Polizas", "Polizas","Principal", PolizaController.class),
    PagoCuotas("PagoCuotas", "Pago de cuotas","Principal", PagoCuotasController.class),
    Siniestros("Siniestros", "Siniestros","Principal", SiniestroController.class),
    Tareas("Tareas", "Tareas","Principal", TareasController.class),
    Reportes("Reportes", "Reportes","Principal", ReportesController.class),
    Configuracion("Configuracion", "Configuracion","Principal", null),
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

	public String getCarpeta() {
		return carpeta;
	}

    public Class getController() {
        return controller;
    }
}
