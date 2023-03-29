package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.EstadoPolizaService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuEstados;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.EstadoPoliza;
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
public class EstadoPolizaController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<EstadoPoliza> tableData;

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

	ObservableList<EstadoPoliza> estadoPolizas;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public EstadoPolizaController(EstadoPolizaService estadoPolizaService, ConstantesPaginas constantesPaginas){
		this.estadoPolizaService= estadoPolizaService;
		this.constantesPaginas= constantesPaginas;
	}

	private final EstadoPolizaService estadoPolizaService;
	private final ConstantesPaginas constantesPaginas;

	private EstadoPoliza estadoPolizaSelected;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.set2Valores(lblNombre, txtNombre, lblDescripcion, txtDescripcion);
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuEstados.EstadoPoliza.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargaTablaEstadoPoliza();
				mostrarTabla();
				btnOnAction();
				buttonDisable(true);
			} catch (Exception ex) {
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	public void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> {
			clear();
			estadoPolizaSelected = null;
			nuevoEstadoPoliza();
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
		if(estadoPolizaSelected.getNombre()!=null){
			txtNombre.setText(estadoPolizaSelected.getNombre());
		}
		if(estadoPolizaSelected.getDescripcion()!=null){
			txtDescripcion.setText(estadoPolizaSelected.getDescripcion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, estadoPolizaSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				estadoPolizaService.deleteEstadoPoliza(estadoPolizaSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.ESTADO_POLIZA_ASOCIADO_OTRO_REGISTRO);
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	public void recargarTabla() {
		try {
			buttonDisable(true);
			tableData.getColumns().clear();
			cargaTablaEstadoPoliza();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void cargaTablaEstadoPoliza() {
		estadoPolizas = FXCollections.observableArrayList(estadoPolizaService.getEstadoPolizas());
		UtilCargaTablas.cargaTablaEstadoPoliza(tableData, estadoPolizas);
		tableData.setOnMouseClicked(
			event -> {
				EstadoPoliza pt = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && pt != null) {
					estadoPolizaSelected = pt;
					updateButton();
				}else {
					if (pt != null) {
						if (estadoPolizaSelected != null) {
							if (estadoPolizaSelected != pt) {
								estadoPolizaSelected = pt;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								estadoPolizaSelected = null;
								buttonDisable(true);
							}
						} else {
							estadoPolizaSelected = pt;
							buttonDisable(false);
						}
					}
				}
			});
	}

	public void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuEstados.EstadoPoliza.getPagina(), btnActualizar, btnEliminar, null);
	}

	public void nuevoEstadoPoliza() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
		Platform.runLater(this::clear);
	}

	private void mostrarFormulario(){
		buttonDisable(true);
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
	}

	public void mostrarTabla() {
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		paneCrud.setOpacity(0);
		new FadeInUpTransition(paneTabel).play();
	}

	private void clear() {
		txtNombre.setText(ConstantesEtiquetas.VACIO);
		txtDescripcion.setText(ConstantesEtiquetas.VACIO);
	}

	public void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(estadoPolizaSelected ==null){
				estadoPolizaSelected = new EstadoPoliza();
			}
			boolean updateProcess = estadoPolizaSelected.getId()!=null;
			estadoPolizaSelected.setNombre(txtNombre.getText());
			estadoPolizaSelected.setDescripcion(txtDescripcion.getText());
			if(updateProcess){
				estadoPolizaService.updateEstadoPoliza(estadoPolizaSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				estadoPolizaService.saveEstadoPoliza(estadoPolizaSelected);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
		}catch(TareasAppException ex){
			errorLog(ex);
		}
		estadoPolizaSelected = null;
	}

	private int validarDatos(){
		if (txtNombre.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_ESTADO_POLIZA);
		} else if((estadoPolizaSelected==null || estadoPolizaSelected.getId()==null) &&
				estadoPolizas.stream().anyMatch(banco -> banco.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.ESTADO_POLIZA_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
