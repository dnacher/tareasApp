package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.MonedaService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuEstados;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Moneda;
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
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class MonedaController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Moneda> tableData;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtSimbolo;

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
	private Label lblSimbolo;

	ObservableList<Moneda> monedas;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Autowired
	public MonedaController(MonedaService monedaService, ConstantesPaginas constantesPaginas){
		this.monedaService= monedaService;
		this.constantesPaginas= constantesPaginas;
	}

	private final MonedaService monedaService;
	private final ConstantesPaginas constantesPaginas;

	private Moneda monedaSelected;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.set2Valores(lblNombre, txtNombre, lblSimbolo, txtSimbolo);
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.Companias.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargaTablaMoneda();
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
			monedaSelected = null;
			nuevaMoneda();
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
		if(monedaSelected.getNombre()!=null){
			txtNombre.setText(monedaSelected.getNombre());
		}
		if(monedaSelected.getSimbolo()!=null){
			txtSimbolo.setText(monedaSelected.getSimbolo());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, monedaSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				monedaService.deleteMoneda(monedaSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (DataIntegrityViolationException ex){
				UtilsGeneral.error(Errores.MONEDA_ASOCIADA_OTRO_REGISTRO);
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
			cargaTablaMoneda();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void cargaTablaMoneda() {
		monedas = FXCollections.observableArrayList(monedaService.getMonedas());
		UtilCargaTablas.cargaTablaMoneda(tableData, monedas);
		tableData.setOnMouseClicked(
			event -> {
				Moneda pt = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && pt != null) {
					monedaSelected = pt;
					updateButton();
				}else {
					if (pt != null) {
						if (monedaSelected != null) {
							if (monedaSelected != pt) {
								monedaSelected = pt;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								monedaSelected = null;
								buttonDisable(true);
							}
						} else {
							monedaSelected = pt;
							buttonDisable(false);
						}
					}
				}
			});
	}

	public void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.Moneda.getPagina(), btnActualizar, btnEliminar, null);
	}

	public void nuevaMoneda() {
		mostrarFormulario();
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		Platform.runLater(() -> clear());
	}

	public void mostrarTabla() {
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
		txtSimbolo.setText(ConstantesEtiquetas.VACIO);
	}

	public void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(monedaSelected ==null){
				monedaSelected = new Moneda();
			}
			boolean updateProcess = monedaSelected.getId()!=null;
			monedaSelected.setNombre(txtNombre.getText());
			monedaSelected.setSimbolo(txtSimbolo.getText());
			if(updateProcess){
				monedaService.updateMoneda(monedaSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				monedaService.saveMoneda(monedaSelected);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
			monedaSelected = null;
		}catch(TareasAppException ex){
			errorLog(ex);
		}
	}

	private int validarDatos(){
		if (txtNombre.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_MONEDA);
		} else if((monedaSelected==null || monedaSelected.getId()==null) &&
				monedas.stream().anyMatch(moneda -> moneda.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.NOMBRE_MONEDA_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
