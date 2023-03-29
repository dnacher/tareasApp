package com.software.tareasApp.enums;

import com.software.tareasApp.controller.*;

/**
 *
 * @author Daniel
 */
public enum MenuConfiguracion {

    Inicio("Inicio","Inicio","Principal",null),
    Banco("Banco", "Banco","Configuracion", BancosController.class),
    Companias("Companias", "Compañias","Configuracion", CompaniaController.class),
    CotizacionVendedores("CotizacionVendedores", "Cotiz. de vendedores","Configuracion", CotizacionVendedoresController.class),
    Estados("Estados", "Estados","Configuracion", null),
    FormaPago("FormaPago", "Forma de Pago","Configuracion", FormaPagoController.class),
    Moneda("Moneda", "Moneda","Configuracion", MonedaController.class),
    Productos("Productos", "Productos","Configuracion", ProductoController.class),
    TipoProductos("TipoProductos", "Tipo de productos","Configuracion", TipoProductosController.class),
    Vendedores("Vendedores", "Vendedores","Configuracion", VendedoresController.class);

    private final String pagina;
    private final String menu;
	private final String carpeta;
	private final Class controller;

    MenuConfiguracion(String pagina, String menu, String carpeta, Class controller) {
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
