package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.CuentaAhorroService;
import com.software.tareasApp.domain.service.MovimientoService;
import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Movimiento;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
public class CuentaAhorroController implements Initializable {

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Movimiento> tableData;

	@FXML
	private Button btnReporte;

	@FXML
	private Button btnBack;

	@FXML
	private Text textTitulo;

	@FXML
	private Text txtSaldo;

	@FXML
	private Button btnBackReporte;

	public CuentaAhorroController(MovimientoService movimientoService, CuentaAhorroService cuentaAhorroService){
		this.movimientoService = movimientoService;
		this.cuentaAhorroService = cuentaAhorroService;
	}

	private final MovimientoService movimientoService;
	private final CuentaAhorroService cuentaAhorroService;

	private CuentaAhorro cuentaAhorro;
	private ObservableList<Movimiento> movimientos;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			if(TareaAppApplication.usuario!=null){
				textTitulo.setText("Saldo de " + TareaAppApplication.usuario.getNombre());
				cuentaAhorro = cuentaAhorroService.findByUsuario(TareaAppApplication.usuario);
				if(cuentaAhorro!=null){
					txtSaldo.setText("$" + cuentaAhorro.getSaldo());
					movimientos = FXCollections.observableArrayList(movimientoService.findByCuentaAhorro(cuentaAhorro));
				}
			}
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();
		longTask.setOnSucceeded(t -> {
			try {
				cargaTablaTareas();
				new FadeInUpTransition(paneTabel).play();
			} catch (Exception ex) {
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
				UtilsGeneral.errorEx(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void cargaTablaTareas() {
		UtilCargaTablas.cargaTablaMovimientos(tableData, movimientos);
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}
}
