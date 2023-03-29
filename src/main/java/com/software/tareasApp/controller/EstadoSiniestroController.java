package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.EstadoSiniestroService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuEstados;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.EstadoSiniestro;
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
public class EstadoSiniestroController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<EstadoSiniestro> tableData;

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

	ObservableList<EstadoSiniestro> estadoSiniestros;
	private EstadoSiniestro estadoSiniestroSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public EstadoSiniestroController(EstadoSiniestroService estadoSiniestroService, ConstantesPaginas constantesPaginas){
		this.estadoSiniestroService= estadoSiniestroService;
		this.constantesPaginas= constantesPaginas;
	}

	private final ConstantesPaginas constantesPaginas;
	private final EstadoSiniestroService estadoSiniestroService;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.set2Valores(lblNombre, txtNombre, lblDescripcion, txtDescripcion);
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuEstados.EstadoSiniestro.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargaTablaEstadoSiniestro();
				mostrarTabla();
				btnOnAction();
				buttonDisable(true);
			} catch (Exception ex) {
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
				UtilsGeneral.errorEx(ex);
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
			nuevoEstadoSiniestro();
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
		if(estadoSiniestroSelected.getNombre()!=null){
			txtNombre.setText(estadoSiniestroSelected.getNombre());
		}
		if(estadoSiniestroSelected.getDescripcion()!=null){
			txtDescripcion.setText(estadoSiniestroSelected.getDescripcion());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, estadoSiniestroSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				estadoSiniestroService.deleteEstadoSiniestro(estadoSiniestroSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.ESTADO_SINIESTRO_ASOCIADO_OTRO_REGISTRO);
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
			cargaTablaEstadoSiniestro();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTablaEstadoSiniestro() {
		estadoSiniestros = FXCollections.observableArrayList(estadoSiniestroService.getEstadoSiniestros());
		UtilCargaTablas.cargaTablaEstadoSiniestro(tableData, estadoSiniestros);
		tableData.setOnMouseClicked(
			event -> {
				EstadoSiniestro pt = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && pt != null) {
					estadoSiniestroSelected = pt;
					updateButton();
				}else {
					if (pt != null) {
						if (estadoSiniestroSelected != null) {
							if (estadoSiniestroSelected != pt) {
								estadoSiniestroSelected = pt;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								estadoSiniestroSelected = null;
								buttonDisable(true);
							}
						} else {
							estadoSiniestroSelected = pt;
							buttonDisable(false);
						}
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuEstados.EstadoSiniestro.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void nuevoEstadoSiniestro() {
		clear();
		estadoSiniestroSelected = null;
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
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

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(estadoSiniestroSelected ==null){
				estadoSiniestroSelected = new EstadoSiniestro();
			}
			boolean updateProcess = estadoSiniestroSelected.getId()!=null;
			estadoSiniestroSelected.setNombre(txtNombre.getText());
			estadoSiniestroSelected.setDescripcion(txtDescripcion.getText());
			estadoSiniestroService.saveEstadoSiniestro(estadoSiniestroSelected);
			if(updateProcess){
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
			estadoSiniestroSelected = null;
		}catch (TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_ESTADO);
		} else if((estadoSiniestroSelected==null || estadoSiniestroSelected.getId()==null) &&
				estadoSiniestros.stream().anyMatch(estado -> estado.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.ESTADO_SINIESTRO_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
