package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.CompaniaService;
import com.software.tareasApp.domain.service.CuotasPolizaService;
import com.software.tareasApp.domain.service.PolizaService;
import com.software.tareasApp.domain.service.RegistroCuotasService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.Compania;
import com.software.tareasApp.persistence.model.CuotasPoliza;
import com.software.tareasApp.persistence.model.RegistroCuotas;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantePermisos;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class PagoCuotasController implements Initializable {

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<RegistroCuotas> tableData;

	@FXML
	private ComboBox<Compania> cmbCompania;

	@FXML
	private TextField txtAnio;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnVer;

	@FXML
	private Label lblInfo;

	@FXML
	private Button btnCierreAnio;

	ObservableList<RegistroCuotas> registrosCuotas;

	private final RegistroCuotasService registroCuotasService;
	private final CuotasPolizaService cuotasPolizaService;
	private final LogManagerClass logger = new LogManagerClass(getClass());
	private final ConstantesPaginas constantesPaginas;
	private final CompaniaService companiaService;
	private final PolizaService polizaService;

	@Autowired
	public PagoCuotasController(RegistroCuotasService registroCuotasService, CuotasPolizaService cuotasPolizaService, ConstantesPaginas constantesPaginas, CompaniaService companiaService, PolizaService polizaService){
		this.registroCuotasService = registroCuotasService;
		this.cuotasPolizaService = cuotasPolizaService;
		this.constantesPaginas = constantesPaginas;
		this.companiaService = companiaService;
		this.polizaService = polizaService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			constantesPaginas.setButtons(null, null, null, btnAgregar, null);
			UtilsSeguridad.setPermisoXPermiso(btnAgregar, MenuPrincipal.PagoCuotas.getPagina(), ConstantePermisos.PERMISO_ADMIN);
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task longTask = UtilsGeneral.task();

		longTask.setOnSucceeded((EventHandler<WorkerStateEvent>) t -> {
			try {
				cargaCombo();
				btnOnAction();
				new FadeInUpTransition(paneTabel).play();
			} catch (Exception ex) {
				errorLog(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void btnOnAction(){
		btnAgregarOnAction();
		btnVerOnAction();
		btnCierreAnioOnAction();
	}

	private void cargaCombo(){
		ObservableList<Compania> companias = FXCollections.observableArrayList(companiaService.getCompanias());
		cmbCompania.setItems(companias);
	}

	private void btnVerOnAction() { btnVer.setOnAction(event -> cargaTabla());}

	private void btnAgregarOnAction(){
		btnAgregar.setOnAction((event) -> guardar());
	}

	private void btnCierreAnioOnAction() { btnCierreAnio.setOnAction(event -> procesoCierreAnio());}

	private void cargaList(){
		Integer anio = Integer.valueOf(txtAnio.getText());
		registrosCuotas = FXCollections.observableArrayList(registroCuotasService.getRegsitrosCuotasByAnioAndCompania(cmbCompania.getValue(), anio));
	}

	private void cargaTabla() {
		if(cmbCompania.getValue()!=null){
			if(UtilsGeneral.esNumero(txtAnio.getText())){
				cargaList();
				tableData.getColumns().clear();
				UtilCargaTablas.cargaTablaRegistroCuotas(tableData, registrosCuotas);
				lblInfo.setText("Total Registros: " + registrosCuotas.size());
			}else{
				UtilsGeneral.error(Errores.ANIO_NUMERICO);
				txtAnio.requestFocus();
			}
		}else{
			UtilsGeneral.error(Errores.FALTAN_VALORES);
			cmbCompania.requestFocus();
		}
	}

	private void guardar() {
		if(validarDatos()==0){
			procesoGuardar();
		}
	}

	private void procesoCierreAnio(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoCierreAnio(paneTabel, txtAnio.getText(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			logger.info(TareaAppApplication.usuario, "Proceso Cierre de año: " + txtAnio.getText());
			if(UtilsGeneral.esNumero(txtAnio.getText())){
				List<RegistroCuotas> list= polizaService.getRegistroCuotasByFecha(Integer.parseInt(txtAnio.getText()));
				logger.info(TareaAppApplication.usuario, "Proceso Cierre de año lista " + list);
				list.forEach(registroCuotas -> {
					int totalCuotasDelAnio = 13 - registroCuotas.getPoliza().getComienzoCuota().getMonth();
					if(totalCuotasDelAnio >= registroCuotas.getPoliza().getCuotas()){
						registroCuotas.setNumeroCuotasPagas(registroCuotas.getPoliza().getCuotas());
					}else{
						registroCuotas.setNumeroCuotasPagas(totalCuotasDelAnio);
					}
					registroCuotas.setUltimaFechaActualizacion(new Date());
				});
				registroCuotasService.saveRegistroCuotasList(list);
				logger.info(TareaAppApplication.usuario, "Proceso Cierre de año finalizado lista " + list);
			}else{
				UtilsGeneral.error(Errores.ANIO_NUMERICO);
			}
			UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
		}
	}

	private void procesoGuardar() throws TareasAppException{
		verificarCuotasAPagarVsCuotasRestantes();
		guardarPagos();
		cargaTabla();
		UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
	}

	private void verificarCuotasAPagarVsCuotasRestantes(){
		registrosCuotas.forEach(
			registroCuota -> {
				if (registroCuota.getPagoCuotas().getValue()!=null) {
					Integer cuotasAPagar = (Integer) registroCuota.getPagoCuotas().getValue();
					int cuotasInicio = registroCuota.getNumeroCuotasPagas();
					int total = cuotasInicio + cuotasAPagar;
					boolean sePasaDeCuotas = total > registroCuota.getPoliza().getCuotas();
					if (cuotasAPagar > 0) {
						if (sePasaDeCuotas) {
							registroCuota.getPagoCuotas().requestFocus();
							UtilsGeneral.error(Errores.PASA_CUOTAS);
							throw new TareasAppException(Errores.PASA_CUOTAS.getError());
						}
					}
				}
			});
	}

	private void guardarPagos(){
		registrosCuotas.forEach(
			registroCuota -> {
				if (registroCuota.getPagoCuotas().getValue()!=null) {
					Integer cuotasAPagar = (Integer) registroCuota.getPagoCuotas().getValue();
					int cuotasInicio = registroCuota.getNumeroCuotasPagas();
					int total = cuotasInicio + cuotasAPagar;
					boolean sePasaDeCuotas = total>registroCuota.getPoliza().getCuotas();
					if(cuotasAPagar>0){
						registroCuota.setNumeroCuotasPagas(total);
						registroCuota.setUltimaFechaActualizacion(new Date());
						registroCuotasService.saveRegistroCuotas(registroCuota);

						int finalCuotas = cuotasInicio + cuotasAPagar;
						if(sePasaDeCuotas){
							finalCuotas = registroCuota.getPoliza().getCuotas();
						}
						for(int i= cuotasInicio; i<finalCuotas; i++){
							CuotasPoliza cuotasPoliza = new CuotasPoliza();
							cuotasPoliza.setPoliza(registroCuota.getPoliza());
							cuotasPoliza.setFechaRegistrado(new Date());
							cuotasPoliza.setNumeroCuota(i+1);
							cuotasPolizaService.saveCuotasPoliza(cuotasPoliza);
						}
					}
				}
			});
	}

	private int validarDatos(){
		return 0;
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}
}
