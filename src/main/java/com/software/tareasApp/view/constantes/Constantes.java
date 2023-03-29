package com.software.tareasApp.view.constantes;

import java.util.Arrays;
import java.util.List;

/*
 * @author Dani-Fla-Mathi
 */
public final class Constantes {

	/*
     *                          RUTAS
	 */
	public static final String PAGINA_ROOT = "/fragments/";
	public static final String LOGO = "/images/icono.png";
	public static final String AGUS = "/images/happy.png";
	public static final String MATHI = "/images/model.png";

	/*
     *                          MENUS
	 */
	public static final String EXTENSION_FXML = ".fxml";
	public static final String MENU_SEGURIDAD = "MenuSeguridad";
	public static final String MENU_PRINCIPAL = "MenuPrincipal";

	public static final String PAGINA_INI = Constantes.PAGINA_ROOT + "splash" + Constantes.EXTENSION_FXML;
	public static final String PAGINA_LOGIN = Constantes.PAGINA_ROOT + "login" + Constantes.EXTENSION_FXML;

	/*
     *                         PAGINAS
	 */
	public static final String PAGINA_FORM_MENU = Constantes.PAGINA_ROOT + "formMenu" + Constantes.EXTENSION_FXML;
	public static final String PAGINA_MAIN = Constantes.PAGINA_ROOT + "Inicio" + Constantes.EXTENSION_FXML;

	/*
     *                             LISTAS
	 */
	public static final List<String> LISTA_MESES = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
																"Julio", "Agosto","Setiembre", "Octubre", "Noviembre", "Diciembre");

	public static final List<String> USUARIOS = Arrays.asList("Daniel", "Mathias", "Agustina");

	/*
     *                             OTROS
	 */
	public static final String EXCEL = "Libro Excel 97-2003";
	public static final String EXTENSION_EXCEL = "*.xls";

	public static final String ROPA = "Ropa";
	public static final String TAZA = "Taza";
	public static final String AMABLE = "Amable";
	public static final String CAMA = "Cama";
	public static final String AGUA = "Agua";
	public static final String ESCRITORIO = "Escritorio";
	public static final String NOCHE = "Noche";

}
