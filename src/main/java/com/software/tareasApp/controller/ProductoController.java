package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.CompaniaService;
import com.software.tareasApp.domain.service.ProductoService;
import com.software.tareasApp.domain.service.TipoProductoService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.Producto;
import com.software.tareasApp.persistence.model.TipoProducto;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductoController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private TableView<Producto> tableProducto;

	@FXML
	private TextField txtProductoBusqueda;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;

	@FXML
	private ComboBox<Compania> cmbCompania;

	@FXML
	private ComboBox<TipoProducto> cmbTipoProducto;

	@FXML
	private TextField txtComisionNueva;

	@FXML
	private TextField txtComisionRenovacion;

	@FXML
	private ProgressBar bar;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnBack;

	@FXML
	private DatePicker cmbFechaComienzo;

	@FXML
	private DatePicker cmbFechaFinal;

	@FXML
	private Label lblInfo;

	ObservableList<Producto> productos;

	@Autowired
	public ProductoController(ProductoService productoService, TipoProductoService tipoProductoService, CompaniaService companiaService, ConstantesPaginas constantesPaginas){
		this.productoService = productoService;
		this.tipoProductoService = tipoProductoService;
		this.companiaService = companiaService;
		this.constantesPaginas = constantesPaginas;
	}

	private final ConstantesPaginas constantesPaginas;
	private final CompaniaService companiaService;
	private final TipoProductoService tipoProductoService;
	private final ProductoService productoService;

	private Producto productoSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.Productos.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargaTabla();
				cargarComboProductType();
				cargaComboCompania();
				mostrarTabla();
				btnOnAction();
				buttonDisable(true);
			} catch (Exception ex) {
				errorLog(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> {
			nuevoProducto();
		});
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void btnBackOnAction() {
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(productoSelected.getNombre()!=null){
			txtNombre.setText(productoSelected.getNombre());
		}
		if(productoSelected.getDescripcion()!=null){
			txtDescripcion.setText(productoSelected.getDescripcion());
		}
		if(productoSelected.getCompania()!=null){
			cmbCompania.setValue(productoSelected.getCompania());
		}
		if(productoSelected.getTipoProducto()!=null){
			cmbTipoProducto.setValue(productoSelected.getTipoProducto());
		}
		if(productoSelected.getComisionNueva()!=null){
			txtComisionNueva.setText(productoSelected.getComisionNueva().toString());
		}
		if(productoSelected.getComisionRenovacion()!=null){
			txtComisionRenovacion.setText(productoSelected.getComisionRenovacion().toString());
		}
		if(productoSelected.getFechaComienzo()!=null){
			cmbFechaComienzo.setValue(UtilsGeneral.getLocalDateFromDate(productoSelected.getFechaComienzo()));
		}
		if(productoSelected.getFechaFinal()!=null){
			cmbFechaFinal.setValue(UtilsGeneral.getLocalDateFromDate(productoSelected.getFechaFinal()));
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, productoSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				productoService.deleteProducto(productoSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
				txtProductoBusqueda.setText(ConstantesEtiquetas.VACIO);
				productoSelected = null;
				buttonDisable(true);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.PRODUCTO_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	private void recargarTabla() {
		try {
			buttonDisable(true);
			tableProducto.getColumns().clear();
			cargaTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTabla() {
		productos = FXCollections.observableArrayList(productoService.getProductos());
    UtilCargaTablas.cargaTablaProductoConFiltro(
        tableProducto,
        productos,
        txtProductoBusqueda,
        lblInfo,
        MenuConfiguracion.Productos.getPagina());
		UtilsGeneral.refreshBusqueda(txtProductoBusqueda);
		tableProducto.setOnMouseClicked(
			event -> {
				Producto p = tableProducto.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && p != null) {
					productoSelected = p;
					updateButton();
				}else {
					if (p != null) {
						if (productoSelected != null) {
							if (productoSelected != p) {
								productoSelected = p;
								buttonDisable(false);
							} else {
								tableProducto.getSelectionModel().clearSelection();
								productoSelected = null;
								buttonDisable(true);
							}
						} else {
							productoSelected = p;
							buttonDisable(false);
						}
					}else{
						buttonDisable(true);
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.Productos.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void cargarComboProductType() {
		ObservableList<TipoProducto> options
			= FXCollections.observableArrayList(tipoProductoService.getTipoProductos());
		cmbTipoProducto.setItems(options);
	}

	private void cargaComboCompania(){
		ObservableList<Compania> options
				= FXCollections.observableArrayList(companiaService.getCompanias());
		cmbCompania.setItems(options);
	}

	private void nuevoProducto() {
		productoSelected = null;
		Platform.runLater(this::clear);
		LocalDate now = Instant.ofEpochMilli(new Date().getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		cmbFechaComienzo.setValue(now);
		mostrarFormulario();
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
	}

	private void mostrarTabla() {
		clear();
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneCrud.setOpacity(0);
		new FadeInUpTransition(paneTabel).play();
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
	}

	private void clear() {
		txtNombre.setText(ConstantesEtiquetas.VACIO);
		txtDescripcion.setText(ConstantesEtiquetas.VACIO);
		cmbCompania.setValue(null);
		cmbTipoProducto.setValue(null);
		txtComisionNueva.setText(ConstantesEtiquetas.VACIO);
		txtComisionRenovacion.setText(ConstantesEtiquetas.VACIO);
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(productoSelected ==null){
				productoSelected = new Producto();
			}
			boolean updateProcess = productoSelected.getId()!=null;
			productoSelected.setNombre(txtNombre.getText());
			productoSelected.setDescripcion(txtDescripcion.getText());
			productoSelected.setCompania(cmbCompania.getValue());
			productoSelected.setTipoProducto(cmbTipoProducto.getValue());
			productoSelected.setComisionNueva(Double.parseDouble(txtComisionNueva.getText()));
			productoSelected.setComisionRenovacion(Double.parseDouble(txtComisionRenovacion.getText()));
			productoSelected.setFechaComienzo(UtilsGeneral.getDateFromLocalDate(cmbFechaComienzo.getValue()));
			productoSelected.setFechaFinal(UtilsGeneral.getDateFromLocalDate(cmbFechaFinal.getValue()));
			if(updateProcess){
				productoService.updateProducto(productoSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
				buttonDisable(true);
			}else{
				productoService.saveProducto(productoSelected);
				clear();
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
			productoSelected = null;
		}catch(TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty()){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_PRODUCTO);
		} else if((productoSelected==null || productoSelected.getId()==null) &&
				productos.stream().anyMatch(producto ->
						producto.getNombre().equals(txtNombre.getText()) &&
						producto.getCompania().getNombre().equals(cmbCompania.getValue().getNombre()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.NOMBRE_PRODUCTO_EXISTE);
		}else if (!UtilsGeneral.esNumero(txtComisionNueva.getText()) || !UtilsGeneral.esNumero(txtComisionRenovacion.getText())) {
			if(!UtilsGeneral.esNumero(txtComisionNueva.getText())){
				txtComisionNueva.requestFocus();
			}else{
				txtComisionRenovacion.requestFocus();
			}
			return UtilsGeneral.error(Errores.COMISION_NUMERICO);
		}else if (!UtilsGeneral.esPorcentaje(txtComisionNueva.getText()) || !UtilsGeneral.esPorcentaje(txtComisionRenovacion.getText())) {
			if(!UtilsGeneral.esPorcentaje(txtComisionNueva.getText())){
				txtComisionNueva.requestFocus();
			}else{
				txtComisionRenovacion.requestFocus();
			}
			return UtilsGeneral.error(Errores.COMISION_PORCENTAJE);
		} else if(cmbFechaComienzo.getValue()==null || cmbFechaFinal.getValue()==null){
			cmbFechaComienzo.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_FECHA);
		}else if(cmbFechaFinal.getValue().isBefore(cmbFechaComienzo.getValue())){
			cmbFechaComienzo.requestFocus();
			return UtilsGeneral.error(Errores.FECHA_COMIENZO_FINAL_ORDEN);
		}else if(cmbCompania.getValue()==null){
			cmbCompania.requestFocus();
			return UtilsGeneral.error(Errores.SELECCIONAR_COMPANIA);
		}else if(cmbTipoProducto.getValue()==null){
			cmbTipoProducto.requestFocus();
			return UtilsGeneral.error(Errores.SELECCIONAR_TIPO_PRODUCTO);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
