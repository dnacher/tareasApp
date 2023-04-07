package com.software.tareasApp.enums;

/**
 *
 * @author Daniel
 */
public enum Errores {

	//Login											000 - 020
	ERROR_LOGIN_GENERAL("Hubo un error General", 0),
	ERROR_LOGIN_FALTAN_DATOS_NOMBRE("Falta Ingresar el nombre de usuario", 1),
	ERROR_LOGIN_FALTAN_DATOS_PASS("Falta Ingresar la contraseña", 2),
	ERROR_LOGIN_DATOS_INCORRECTOS("El nombre o contraseña son incorrectos", 3),
	ERROR_GENERAL("Hubo un error", 0),

	//Pago											040 - 060
	ERROR_CONCEPTO_EXISTE("Para este mes y esta cuenta ya fue pago",040),

	//Reportes 										261 - 280
	ELEGIR_ARCHIVO("Debe elegir un archivo",261),
	SELECCIONE_OPCION("Seleccione una opción",262),
	NO_IMPLEMENTADO("todavia no implementado",263),
	FALTA_COTIZACION("La cotizacion de dolares es campo obligatorio", 265),

	//Seguridad										281 - 300
	MARCAR_VALOR_PERMISO("Debe marcar uno de los valores de permisos",281),
	PAGINA_EXISTE("La pagina ya esta en los permisos",282),
	SELECCIONAR_COMBOS("debe seleccionar los combos",283),
	SELECCIONAR_PERMISO("Debe seleccionar un permiso a borrar",284),

	//Tipo Usuarios									341 - 360
	FALTA_NOMBRE_TIPO_USUARIO("Falta nombre del tipo de usuario", 341),
	NOMBRE_TIPO_USUARIO_EXISTE("Ya existe el nombre del tipo de usuario", 342),
	TIPO_USUARIO_ASOCIADO_OTRO_REGISTRO("El tipo de usuario esta asociado en otro registro", 343),

	//Usuario										361 - 380
	FALTA_NOMBRE_USUARIO("Agregar Nombre al Usuario",361),
	PASS_USUARIO("Agregar contraseña al Usuario",362),
	PASS_USUARIO_MENOR_4("la contraseña debe tener por lo menos 4 caracteres",363),
	PASS_NO_COINCIDE("Deben coincidir ambos passwords.",364),
	USUARIO_EXISTE("Nombre de usuario ya existe", 365),
	USUARIO_ASOCIADO_OTRO_REGISTRO("El usuario esta asociado en otro registro", 366),

	SIN_PERMISOS("Sin permisos, contacte al administrador", 403),
	LOGUEO_ERROR ("Credenciales incorrectas",0),
	VERIFICAR("Verificar", 1),
	ERROR("Error", 8),
	WARNING("Warning", 9);

	private final String error;
	private final int codigo;

	Errores(String error, int codigo) {
		this.error = error;
		this.codigo = codigo;
	}

	public String getError() {
		return error;
	}

	public int getCodigo() {
		return codigo;
	}

}
