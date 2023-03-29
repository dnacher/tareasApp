package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.CompaniaService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Compania;
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
public class CompaniaController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Compania> tableData;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtDescripcion;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtPhone;

	@FXML
	private TextField txtWeb;

	@FXML
	private TextField txtNumeroAuxilio;

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

	ObservableList<Compania> companias;

	@Autowired
	public CompaniaController(CompaniaService companiaService, ConstantesPaginas constantesPaginas){
		this.companiaService = companiaService;
		this.constantesPaginas = constantesPaginas;
	}

	private final ConstantesPaginas constantesPaginas;
	private final CompaniaService companiaService;

	private Compania companiaSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.Companias.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.Companias.getPagina(), btnActualizar, btnEliminar, null);
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				companias = FXCollections.observableArrayList(companiaService.getCompanias());
				cargaTabla();
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
			companiaSelected = null;
			nuevaCompania();
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
		if(companiaSelected.getNombre()!=null){
			txtNombre.setText(companiaSelected.getNombre());
		}
		if(companiaSelected.getDescripcion()!=null){
			txtDescripcion.setText(companiaSelected.getDescripcion());
		}
		if(companiaSelected.getEmail()!=null){
			txtEmail.setText(companiaSelected.getEmail());
		}
		if(companiaSelected.getTelefono()!=null){
			txtPhone.setText(companiaSelected.getTelefono());
		}
		if(companiaSelected.getWeb()!=null){
			txtWeb.setText(companiaSelected.getWeb());
		}
		if(companiaSelected.getNumeroAuxilio()!=null){
			txtNumeroAuxilio.setText(companiaSelected.getNumeroAuxilio());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, companiaSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				companiaService.deleteCompany(companiaSelected);
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

	public void recargarTabla() {
		try {
			companias = FXCollections.observableArrayList(companiaService.getCompanias());
			buttonDisable(true);
			tableData.getColumns().clear();
			cargaTabla();
			mostrarTabla();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void cargaTabla() {
		UtilCargaTablas.cargaCompaniaTabla(tableData, companias);
		tableData.setOnMouseClicked(
				event -> {
					Compania c = tableData.getSelectionModel().getSelectedItem();
					if (event.getClickCount() == 2 && c != null) {
						companiaSelected = c;
						updateButton();
					}else {
						if (c != null) {
							if (companiaSelected != null) {
								if (companiaSelected != c) {
									companiaSelected = c;
									buttonDisable(false);
								} else {
									tableData.getSelectionModel().clearSelection();
									companiaSelected = null;
									buttonDisable(true);
								}
							} else {
								companiaSelected = c;
								buttonDisable(false);
							}
						}
					}
				});
	}

	public void nuevaCompania() {
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
		txtEmail.setText(ConstantesEtiquetas.VACIO);
		txtPhone.setText(ConstantesEtiquetas.VACIO);
		txtWeb.setText(ConstantesEtiquetas.VACIO);
		txtNumeroAuxilio.setText(ConstantesEtiquetas.VACIO);
	}

	public void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(companiaSelected ==null){
				companiaSelected = new Compania();
			}
			boolean updateProcess = companiaSelected.getId()!=null;
			companiaSelected.setNombre(txtNombre.getText());
			companiaSelected.setDescripcion(txtDescripcion.getText());
			companiaSelected.setEmail(txtEmail.getText());
			companiaSelected.setTelefono(txtPhone.getText());
			companiaSelected.setWeb(txtWeb.getText());
			companiaSelected.setNumeroAuxilio(txtNumeroAuxilio.getText());
			if(updateProcess){
				companiaService.updateCompany(companiaSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				companiaService.saveCompany(companiaSelected);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
		}catch(TareasAppException ex){
			errorLog(ex);
		}
		companiaSelected = null;
	}

	private int validarDatos(){
		if (txtNombre.getText().equals(ConstantesEtiquetas.VACIO)) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_COMPANIA);
		} else if((companiaSelected==null || companiaSelected.getId()==null) &&
				companias.stream().anyMatch(banco -> banco.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.COMPANIA_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
