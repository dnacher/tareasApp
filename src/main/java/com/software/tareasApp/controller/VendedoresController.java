package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.VendedorService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuConfiguracion;
import com.software.tareasApp.enums.MenuSeguridad;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Vendedor;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class VendedoresController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private TableView<Vendedor> tableData;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtApellido;

	@FXML
	private TextField txtDireccion;

	@FXML
	private TextField txtCiudad;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private TextField txtCelular;

	@FXML
	private TextField txtEmail;

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

	ObservableList<Vendedor> vendedores;

	@Autowired
	public VendedoresController(VendedorService vendedorService, ConstantesPaginas constantesPaginas){
		this.vendedorService = vendedorService;
		this.constantesPaginas = constantesPaginas;
	}

	private final ConstantesPaginas constantesPaginas;
	private final VendedorService vendedorService;

	private Vendedor vendedorSelected;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuConfiguracion.Vendedores.getPagina());
			task();
		} catch (Exception ex) {
			logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();

		longTask.setOnSucceeded(t -> {
			try {
				cargaTablaVendedor();
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
			nuevoVendedor();
		});
	}

	private void btnBackOnAction() {
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(vendedorSelected.getNombre()!=null){
			txtNombre.setText(vendedorSelected.getNombre());
		}
		if(vendedorSelected.getApellido()!=null){
			txtApellido.setText(vendedorSelected.getApellido());
		}
		if(vendedorSelected.getDireccion()!=null){
			txtDireccion.setText(vendedorSelected.getDireccion());
		}
		if(vendedorSelected.getCiudad()!=null){
			txtCiudad.setText(vendedorSelected.getCiudad());
		}
		if(vendedorSelected.getDepartamento()!=null){
			txtDepartamento.setText(vendedorSelected.getDepartamento());
		}
		if(vendedorSelected.getCelular()!=null){
			txtCelular.setText(vendedorSelected.getCelular());
		}
		if(vendedorSelected.getEmail()!=null){
			txtEmail.setText(vendedorSelected.getEmail());
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, vendedorSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				vendedorService.deleteVendedor(vendedorSelected);
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
			cargaTablaVendedor();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTablaVendedor() {
		vendedores = FXCollections.observableArrayList(vendedorService.getVendedores());
		UtilCargaTablas.cargaTablaVendedor(tableData, vendedores);
		tableData.setOnMouseClicked(
				event -> {
					Vendedor v = tableData.getSelectionModel().getSelectedItem();
					if (event.getClickCount() == 2 && v != null) {
						vendedorSelected = v;
						updateButton();
					}else {
						if (v != null) {
							if (vendedorSelected != null) {
								if (vendedorSelected != v) {
									vendedorSelected = v;
									buttonDisable(false);
								} else {
									tableData.getSelectionModel().clearSelection();
									vendedorSelected = null;
									buttonDisable(true);
								}
							} else {
								vendedorSelected = v;
								buttonDisable(false);
							}
						}
					}
				});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuConfiguracion.Vendedores.getPagina(), btnActualizar, btnEliminar, null);
	}

	private void nuevoVendedor() {
		clear();
		vendedorSelected = null;
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
		txtApellido.setText(ConstantesEtiquetas.VACIO);
		txtDireccion.setText(ConstantesEtiquetas.VACIO);
		txtCiudad.setText(ConstantesEtiquetas.VACIO);
		txtDepartamento.setText(ConstantesEtiquetas.VACIO);
		txtCelular.setText(ConstantesEtiquetas.VACIO);
		txtEmail.setText(ConstantesEtiquetas.VACIO);
	}


	public void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoGuardar(){
		try{
			if(vendedorSelected ==null){
				vendedorSelected = new Vendedor();
			}
			boolean updateProcess = vendedorSelected.getId()!=null;
			vendedorSelected.setNombre(txtNombre.getText());
			vendedorSelected.setApellido(txtApellido.getText());
			vendedorSelected.setDireccion(txtDireccion.getText());
			vendedorSelected.setCiudad(txtCiudad.getText());
			vendedorSelected.setDepartamento(txtDepartamento.getText());
			vendedorSelected.setCelular(txtCelular.getText());
			vendedorSelected.setEmail(txtEmail.getText());
			vendedorService.saveVendedor(vendedorSelected);
			if(updateProcess){
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			recargarTabla();
			mostrarTabla();
		}catch (TareasAppException ex){
			errorLog(ex);
		}
		vendedorSelected = null;
	}

	private int validarDatos(){
		if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()) {
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.FALTA_NOMBRE_VENDEDOR);
		} else if((vendedorSelected==null || vendedorSelected.getId()!=null) &&
				vendedores.stream().anyMatch(vendedor -> vendedor.getNombre().equals(txtNombre.getText()))){
			txtNombre.requestFocus();
			return UtilsGeneral.error(Errores.NOMBRE_VENDEDOR_EXISTE);
		}
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}

}
