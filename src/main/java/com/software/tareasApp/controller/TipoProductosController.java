package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.TipoProductoService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.TipoProducto;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class TipoProductosController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<TipoProducto> tableData;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;

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
	private Label lblNombre;

	@FXML
	private Label lblDescripcion;

	ObservableList<TipoProducto> tipoProductos;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public TipoProductosController(TipoProductoService tipoProductoService, ConstantesPaginas constantesPaginas){
		this.tipoProductoService= tipoProductoService;
		this.constantesPaginas= constantesPaginas;
	}

	private final TipoProductoService tipoProductoService;
	private final ConstantesPaginas constantesPaginas;

	private TipoProducto tipoProductoSelected;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			constantesPaginas.set2Valores(lblNombre, txtNombre, lblDescripcion, txtDescripcion);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.TipoProductos.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargaTablaTipoProducto();
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
			clear();
			tipoProductoSelected = null;
			nuevoTipoProducto();
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
		if(tipoProductoSelected.getNombre()!=null){
			txtNombre.setText(tipoProductoSelected.getNombre());
		}
		if(tipoProductoSelected.getDescripcion()!=null){
			txtDescripcion.setText(tipoProductoSelected.getDescripcion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, tipoProductoSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				tipoProductoService.deleteTipoProducto(tipoProductoSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.COMPANIA_ASOCIADA_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	private void recargarTabla() {
		try {
			buttonDisable(true);
			tableData.getColumns().clear();
			cargaTablaTipoProducto();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTablaTipoProducto() {
		tipoProductos = FXCollections.observableArrayList(tipoProductoService.getTipoProductos());
		UtilCargaTablas.cargaTablaTipoProducto(tableData, tipoProductos);
		tableData.setOnMouseClicked(
			event -> {
				TipoProducto pt = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && pt != null) {
					tipoProductoSelected = pt;
					updateButton();
				}else {
					if (pt != null) {
						if (tipoProductoSelected != null) {
							if (tipoProductoSelected != pt) {
								tipoProductoSelected = pt;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								tipoProductoSelected = null;
								buttonDisable(true);
							}
						} else {
							tipoProductoSelected = pt;
							buttonDisable(false);
						}
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.TipoProductos.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void nuevoTipoProducto() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
	}

	private void mostrarTabla() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneCrud.setOpacity(0);
		new FadeInUpTransition(paneTabel).play();
	}

	private void clear() {
		txtNombre.setText(ConstantesEtiquetas.VACIO);
		txtDescripcion.setText(ConstantesEtiquetas.VACIO);
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(tipoProductoSelected ==null){
				tipoProductoSelected = new TipoProducto();
			}
			boolean updateProcess = tipoProductoSelected.getId()!=null;
			tipoProductoSelected.setNombre(txtNombre.getText());
			tipoProductoSelected.setDescripcion(txtDescripcion.getText());
			tipoProductoService.saveTipoProducto(tipoProductoSelected);
			if(updateProcess){
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
			tipoProductoSelected = null;
		}catch (TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty()) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_TIPO_PRODUCTO);
		} else if((tipoProductoSelected==null || tipoProductoSelected.getId()!=null) &&
				tipoProductos.stream().anyMatch(tipoProducto -> tipoProducto.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.NOMBRE_TIPO_PRODUCTO_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}


}
