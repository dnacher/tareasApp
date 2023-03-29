package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.FormaPagoService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuEstados;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.FormaPago;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
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
public class FormaPagoController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<FormaPago> tableData;

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

	ObservableList<FormaPago> formaPagos;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public FormaPagoController(FormaPagoService formaPagoService, ConstantesPaginas constantesPaginas){
		this.constantesPaginas= constantesPaginas;
		this.formaPagoService= formaPagoService;
	}

	private final ConstantesPaginas constantesPaginas;
	private final FormaPagoService formaPagoService;

	private FormaPago formaPagoSelected;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.set2Valores(lblNombre, txtNombre, lblDescripcion, txtDescripcion);
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.FormaPago.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task longTask = UtilsGeneral.task();

		longTask.setOnSucceeded((EventHandler<WorkerStateEvent>) t -> {
			try {
				cargaTablaFormaPago();
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

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(!UtilsGeneral.isNullOrEmpty(formaPagoSelected.getNombre())){
			txtNombre.setText(formaPagoSelected.getNombre());
		}
		if(!UtilsGeneral.isNullOrEmpty(formaPagoSelected.getDescripcion())){
			txtDescripcion.setText(formaPagoSelected.getDescripcion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, formaPagoSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				formaPagoService.deleteFormaPago(formaPagoSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.FORMA_PAGO_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	private void btnAgregarOnAction(){
		btnAgregar.setOnAction((event) -> {
			agregarFormaPago();
		});
	}

	private void btnBackOnAction(){
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void recargarTabla() {
		try {
			buttonDisable(true);
			tableData.getColumns().clear();
			cargaTablaFormaPago();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTablaFormaPago() {
		formaPagos = FXCollections.observableArrayList(formaPagoService.getFormaPagos());
		UtilCargaTablas.cargaTablaFormaPago(tableData, formaPagos);
		tableData.setOnMouseClicked(
			event -> {
				FormaPago b = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && b != null) {
					formaPagoSelected = b;
					updateButton();
				}else {
					if (b != null) {
						if (formaPagoSelected != null) {
							if (formaPagoSelected != b) {
								formaPagoSelected = b;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								formaPagoSelected = null;
								buttonDisable(true);
							}
						} else {
							formaPagoSelected = b;
							buttonDisable(false);
						}
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.FormaPago.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void agregarFormaPago() {
		clear();
		formaPagoSelected = null;
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
		Platform.runLater(this::clear);
	}

	private void mostrarTabla() {
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
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(formaPagoSelected ==null){
				formaPagoSelected = new FormaPago();
			}
			boolean updateProcess = formaPagoSelected.getId()!=null;
			formaPagoSelected.setNombre(txtNombre.getText());
			formaPagoSelected.setDescripcion(txtDescripcion.getText());
			if(updateProcess){
				formaPagoService.updateFormaPago(formaPagoSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
				formaPagoSelected = null;
			}else{
				formaPagoService.saveFormaPago(formaPagoSelected);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
				formaPagoSelected = null;
			}
			recargarTabla();
			mostrarTabla();
		}catch (TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_FORMA_DE_PAGO);
		} else if((formaPagoSelected==null || formaPagoSelected.getId()==null) &&
				formaPagos.stream().anyMatch(formaPago -> formaPago.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FORMA_DE_PAGO_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
