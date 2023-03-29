package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.CotizacionVendedorService;
import com.software.tareasApp.domain.service.ProductoService;
import com.software.tareasApp.domain.service.VendedorService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.persistence.model.*;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class CotizacionVendedoresController implements Initializable {

	@FXML
	private AnchorPane paneFormulario;

	@FXML
	private AnchorPane paneProducto;

	@FXML
	private AnchorPane paneCotizaciones;

	@FXML
	private TableView<Producto> tableProducto;

	@FXML
	private TableView<CotizacionVendedor> tableCotizaciones;

	@FXML
	private Label lblProducto;

	@FXML
	private Label lblTipoProducto;

	@FXML
	private Label lblCompania;

	@FXML
	private TextField txtComisionNueva;

	@FXML
	private TextField txtComisionRenovacion;

	@FXML
	private DatePicker cmbFechaInicio;

	@FXML
	private DatePicker cmbFechaFin;

	@FXML
	private ProgressBar bar;

	@FXML
	private Button btnProducto;

	@FXML
	private Button btnTipoProducto;

	@FXML
	private Button btnCompania;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnBackProducto;

	@FXML
	private Button btnBackFormulario;

	@FXML
	private TextField txtClienteCompania;

	@FXML
	private Label lblInfoCotizaciones;

	@FXML
	private Label lblInfoProducto;

	@FXML
	private ComboBox<Vendedor> cmbVendedores;

	ObservableList<Producto> productos;
	ObservableList<Vendedor> vendedores;
	ObservableList<CotizacionVendedor> cotizacionVendedores;
	FilteredList<CotizacionVendedor> filteredData;

	@Autowired
	public CotizacionVendedoresController(ProductoService productoService, VendedorService vendedorService,
										  CotizacionVendedorService cotizacionVendedoresService, ConstantesPaginas constantesPaginas){
		this.vendedorService= vendedorService;
		this.productoService = productoService;
		this.cotizacionVendedoresService = cotizacionVendedoresService;
		this.constantesPaginas = constantesPaginas;
	}


	private final ConstantesPaginas constantesPaginas;
	private final ProductoService productoService;
	private final VendedorService vendedorService;
	private final CotizacionVendedorService cotizacionVendedoresService;

	private Vendedor vendedorSelected;
	private Producto productoSelected;
	private CotizacionVendedor cotizacionVendedorSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.CotizacionVendedores.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargarTablas();
				mostrarTablaCotizacionVendedores();
				btnOnAction();
				buttonDisable(true);
			} catch (Exception ex) {
				errorLog(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void cargarTablas(){
		cargaTablaProducto();
		cargaComboVendedores();
		cargaTablaCotizacionVendedores();
	}

	public void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
		btnBuscarOnAction();
		btnBackFormularioOnAction();
	}

	private void btnBackFormularioOnAction(){
		btnBackFormulario.setOnAction((event) -> mostrarTablaCotizacionVendedores());
	}

	private void btnBuscarOnAction(){
		btnProducto.setOnAction((event) -> mostrarTablaProducto());
		btnTipoProducto.setOnAction((event) -> mostrarTablaProducto());
		btnCompania.setOnAction((event) -> mostrarTablaProducto());
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> {
			clear();
			cotizacionVendedorSelected = null;
			LocalDate now = Instant.ofEpochMilli(new Date().getTime())
					.atZone(ZoneId.systemDefault())
					.toLocalDate();
			cmbFechaInicio.setValue(now);
			mostrarFormulario();
			btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		});
	}

	private void btnBackOnAction() {
		btnBackProducto.setOnAction((event) -> {
			btnAgregar.setDisable(true);
			mostrarFormulario();
		});
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(cotizacionVendedorSelected.getVendedor()!=null){
			vendedorSelected = cotizacionVendedorSelected.getVendedor();
			cmbVendedores.setValue(cotizacionVendedorSelected.getVendedor());
		}
		if(cotizacionVendedorSelected.getProducto()!=null){
			productoSelected = cotizacionVendedorSelected.getProducto();
			lblProducto.setText(cotizacionVendedorSelected.getProducto().getNombre());
			lblTipoProducto.setText(cotizacionVendedorSelected.getProducto().getTipoProducto().getNombre());
		}
		if(cotizacionVendedorSelected.getCompania()!=null){
			lblCompania.setText(cotizacionVendedorSelected.getCompania().getNombre());
		}
		if(cotizacionVendedorSelected.getComisionNueva()!=null){
			txtComisionNueva.setText(cotizacionVendedorSelected.getComisionNueva().toString());
		}
		if(cotizacionVendedorSelected.getComisionRenovacion()!=null){
			txtComisionRenovacion.setText(cotizacionVendedorSelected.getComisionRenovacion().toString());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneFormulario, cotizacionVendedorSelected.getVendedorToString(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				cotizacionVendedoresService.deleteCotizacionVendedor(cotizacionVendedorSelected);
				recargarTablaCotizaciones();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.COMISION_ASOCIADA_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	public void cargaTablaCotizacionVendedores() {
		cotizacionVendedores = FXCollections.observableArrayList(cotizacionVendedoresService.getCotizacionVendedores());
		UtilCargaTablas.cargaTablaCotizacionVendedores(tableCotizaciones, cotizacionVendedores, filteredData, lblInfoCotizaciones);
		tableCotizaciones.setOnMouseClicked(
				event -> {
					CotizacionVendedor c = tableCotizaciones.getSelectionModel().getSelectedItem();
					if (event.getClickCount() == 2 && c != null) {
						cotizacionVendedorSelected = c;
						updateButton();
					}else {
						if (c != null) {
							if (cotizacionVendedorSelected != c) {
								cotizacionVendedorSelected = c;
								buttonDisable(false);
							} else {
								tableCotizaciones.getSelectionModel().clearSelection();
								cotizacionVendedorSelected = null;
								buttonDisable(true);
							}
						} else {
							cotizacionVendedorSelected = null;
							buttonDisable(true);
						}
					}

				});
	}

	public void cargaTablaProducto() {
		productos = FXCollections.observableArrayList(productoService.getProductos());
		UtilCargaTablas.cargaTablaProductoConFiltro(tableProducto, productos,txtClienteCompania, lblInfoProducto, MenuConfiguracion.CotizacionVendedores.getPagina());
		tableProducto.setOnMouseClicked(
				event -> {
					Producto p = tableProducto.getSelectionModel().getSelectedItem();
					if (p != null) {
						if(productoSelected!=null) {
							if(!Objects.equals(productoSelected.getId(), p.getId())){
								productoSelected = p;
								lblProducto.setText(productoSelected.getNombre());
								lblTipoProducto.setText(productoSelected.getTipoProducto().getNombre());
								lblCompania.setText(productoSelected.getCompania().getNombre());
								btnGuardar.setDisable(false);
							}else{
								btnGuardar.setDisable(true);
								productoSelected = null;
								lblProducto.setText("");
								lblTipoProducto.setText("");
								lblCompania.setText("");
							}
						}else {
							productoSelected = p;
							lblProducto.setText(productoSelected.getNombre());
							lblTipoProducto.setText(productoSelected.getTipoProducto().getNombre());
							lblCompania.setText(productoSelected.getCompania().getNombre());
							btnGuardar.setDisable(false);
						}
					}
					mostrarFormulario();
				});
	}

	private void cargaComboVendedores(){
		vendedores = FXCollections.observableArrayList(vendedorService.getVendedores());
		cmbVendedores.setItems(vendedores);
		cmbVendedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null){
				vendedorSelected = newValue;
			}
		});
	}

	public void recargarTablaCotizaciones() {
		try {
			buttonDisable(true);
			tableCotizaciones.getColumns().clear();
			cargaTablaCotizacionVendedores();
			mostrarTablaCotizacionVendedores();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.Companias.getPagina(), btnActualizar, btnEliminar, null);
	}

	public void mostrarTablaProducto() {
		btnAgregar.setDisable(true);
		buttonDisable(true);
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneCotizaciones.setOpacity(0);
		paneFormulario.setOpacity(0);
		new FadeInUpTransition(paneProducto).play();
	}

	public void mostrarTablaCotizacionVendedores(){
		btnAgregar.setDisable(false);
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneProducto.setOpacity(0);
		paneFormulario.setOpacity(0);
		new FadeInUpTransition(paneCotizaciones).play();
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneProducto.setOpacity(0);
		paneCotizaciones.setOpacity(0);
		new FadeInUpTransition(paneFormulario).play();
	}

	private void clear() {
		lblProducto.setText(ConstantesEtiquetas.VACIO);
		lblTipoProducto.setText(ConstantesEtiquetas.VACIO);
		lblCompania.setText(ConstantesEtiquetas.VACIO);
		txtComisionNueva.setText(ConstantesEtiquetas.VACIO);
		txtComisionRenovacion.setText(ConstantesEtiquetas.VACIO);
		cmbFechaInicio.setValue(null);
		cmbFechaFin.setValue(null);
	}


	public void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
    	try {
			if (cotizacionVendedorSelected == null) {
				cotizacionVendedorSelected = new CotizacionVendedor();
			}
			boolean updateProcess = cotizacionVendedorSelected.getId() != null;
		  	cotizacionVendedorSelected.setVendedor(vendedorSelected);
		  	cotizacionVendedorSelected.setProducto(productoSelected);
		  	cotizacionVendedorSelected.setComisionNueva(Integer.valueOf(txtComisionNueva.getText()));
		 	cotizacionVendedorSelected.setComisionRenovacion(
			Integer.valueOf(txtComisionRenovacion.getText()));
		  	cotizacionVendedorSelected.setFechaInicio(
			UtilsGeneral.getDateFromLocalDate(cmbFechaInicio.getValue()));
		  	cotizacionVendedorSelected.setFechaFin(
		 	UtilsGeneral.getDateFromLocalDate(cmbFechaFin.getValue()));
		  	cotizacionVendedoresService.saveCotizacionVendedor(cotizacionVendedorSelected);
		  	if (updateProcess) {
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
				cotizacionVendedorSelected = null;
		  	} else {
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
				productoSelected = null;
				vendedorSelected = null;
				cotizacionVendedorSelected = null;
		  	}
			recargarTablaCotizaciones();
			mostrarTablaProducto();
		}catch(Exception ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (vendedorSelected==null) {
			cmbVendedores.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_VENDEDOR_COTIZACION);
		} else if((cotizacionVendedorSelected==null || cotizacionVendedorSelected.getId()==null) &&
				cotizacionVendedores.stream().anyMatch(cotizacionVendedor -> cotizacionVendedor.getVendedor().getNombreYApellido().equals(cmbVendedores.getValue().getNombreYApellido())
					&& cotizacionVendedor.getProducto().getNombre().equals(productoSelected.getNombre()))){
			cmbVendedores.requestFocus();
			return UtilsGeneral.error(Errores.COTIZACION_EXISTE);
		} else if(!UtilsGeneral.esNumero(txtComisionNueva.getText()) || !UtilsGeneral.esNumero(txtComisionRenovacion.getText())){
			if(!UtilsGeneral.esNumero(txtComisionNueva.getText())){
				txtComisionNueva.requestFocus();
			}else{
				txtComisionRenovacion.requestFocus();
			}
			return UtilsGeneral.error(Errores.COMISION_NUMERO);
		} else if (!UtilsGeneral.esPorcentaje(txtComisionNueva.getText()) || !UtilsGeneral.esPorcentaje(txtComisionRenovacion.getText())) {
			if(!UtilsGeneral.esPorcentaje(txtComisionNueva.getText())){
				txtComisionNueva.requestFocus();
			}else{
				txtComisionRenovacion.requestFocus();
			}
			return UtilsGeneral.error(Errores.COMISION_PORCENTAJE);
		}else if(cmbFechaInicio.getValue()==null || cmbFechaFin.getValue()==null || cmbFechaFin.getValue().isBefore(cmbFechaInicio.getValue())){
			cmbFechaInicio.requestFocus();
			return UtilsGeneral.error(Errores.FECHA_COMIENZO_FINAL_ORDEN);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
