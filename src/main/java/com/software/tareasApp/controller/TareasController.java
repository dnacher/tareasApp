package com.software.tareasApp.controller;

import com.google.gson.Gson;
import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.CuentaAhorroService;
import com.software.tareasApp.domain.service.MovimientoService;
import com.software.tareasApp.domain.service.TareaService;
import com.software.tareasApp.domain.service.UsuarioService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.persistence.model.CuentaAhorro;
import com.software.tareasApp.persistence.model.Movimiento;
import com.software.tareasApp.persistence.model.Tarea;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.utils.ExportarExcel;
import com.software.tareasApp.utils.UtilCargaTablas;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.utils.UtilsSeguridad;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import com.software.tareasApp.view.constantes.ConstantesMensajes;
import com.software.tareasApp.view.constantes.ConstantesPaginas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

@Component
public class TareasController implements Initializable {

	@FXML
	private AnchorPane paneCrud;

	@FXML
	private AnchorPane paneTabel;

	@FXML
	private AnchorPane paneReporte;

	@FXML
	private AnchorPane panePagar;

	@FXML
	private ProgressBar bar;

	@FXML
	private TableView<Tarea> tableData;

	@FXML
	private TableView<Tarea> tableReporteMes;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnGuardar;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnReporte;

	@FXML
	private Button btnBack;

	@FXML
	private Text textTitulo;

	@FXML
	private CheckBox chkRopa;

	@FXML
	private CheckBox chkTaza;

	@FXML
	private CheckBox chkSerAmable;

	@FXML
	private CheckBox chkCama;

	@FXML
	private CheckBox chkAgua;

	@FXML
	private CheckBox chkEscritorio;

	@FXML
	private CheckBox chkCamaNoche;

	@FXML
	private DatePicker cmbFecha;

	@FXML
	private ComboBox<String> cmbFechaPagar;

	@FXML
	private ComboBox<Usuario> cmbUsuario;

	@FXML
	private Label lblRopa;

	@FXML
	private Label lblTaza;

	@FXML
	private Label lblSerAmable;

	@FXML
	private Label lblCama;

	@FXML
	private Label lblAgua;

	@FXML
	private Label lblEscritorio;

	@FXML
	private Label lblCamaNoche;

	@FXML
	private Label lblTotal;

	@FXML
	private Button btnBackReporte;

	@FXML
	private Button btnPagar;

	@FXML
	private Label lblInfoReporte;

	@FXML
	private Label lblInfoPieChart;

	@FXML
	private PieChart chartReporte;

	@FXML
	private ImageView imgAmable;

	@FXML
	private ImageView imgAgua;

	@FXML
	private ComboBox<String> cmbFechaReporteDesde;

	@FXML
	private Button btnGenerarReporte;

	@FXML
	private Button btnExportarExcel;

	@FXML
	private Text txtInfoPago;

	@FXML
	private Button btnGenerarPago;

	@FXML
	private Button btnGenerarGasto;

	@FXML
	private DatePicker cmbFechaGasto;

	@FXML
	private ComboBox<Usuario> cmbUsuarioGasto;

	@FXML
	private TextField txtGasto;

	@FXML
	private TextField txtGastoValor;

	@FXML
	private Button btnGenerarNotaCredito;

	@FXML
	private DatePicker cmbFechaNotaCredito;

	@FXML
	private ComboBox<Usuario> cmbUsuarioNotaCredito;

	@FXML
	private TextField txtNotaCredito;

	@FXML
	private TextField txtNotaCreditoValor;

	ObservableList<Tarea> tareas;
	ObservableList<Tarea> tareasReoporte;
	Tarea tareaSelected;

	public TareasController(ConstantesPaginas constantesPaginas, TareaService tareaService, UsuarioService usuarioService, MovimientoService movimientoService, CuentaAhorroService cuentaAhorroService){
		this.constantesPaginas = constantesPaginas;
		this.tareaService = tareaService;
		this.usuarioService = usuarioService;
		this.movimientoService = movimientoService;
		this.cuentaAhorroService = cuentaAhorroService;
	}

	private final ConstantesPaginas constantesPaginas;
	private final TareaService tareaService;
	private final UsuarioService usuarioService;
	private final MovimientoService movimientoService;
	private final CuentaAhorroService cuentaAhorroService;

	private int total = 0;
	private int totalReporte = 0;

	private final LogManagerClass logger = new LogManagerClass(getClass());

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			cmbFecha.setValue(UtilsGeneral.getLocalDateFromDate(new Date()));
			chkBoxListener();
			if(TareaAppApplication.usuario!=null){
				textTitulo.setText("Tareas de " + TareaAppApplication.usuario.getNombre());
			}
			constantesPaginas.setButtons(null, btnAgregar, btnActualizar, btnEliminar, btnGuardar);
			UtilsSeguridad.seguridad(btnAgregar, btnGuardar,btnActualizar, btnEliminar, MenuPrincipal.Tareas.getPagina());
			UtilsSeguridad.seguridadAdmin(btnPagar, MenuPrincipal.Tareas.getPagina());
			task();
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void comboListener(){
		cmbUsuario.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null){
				if(cmbFechaPagar.getValue()!=null){
					txtInfoPago.setText("$ " + generarMes(cmbUsuario.getValue()));
				}
			}
		});

		cmbFechaPagar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue!=null){
				if(cmbUsuario.getValue()!=null){
					txtInfoPago.setText("$ " + generarMes(cmbUsuario.getValue()));
				}
			}
		});
	}

	private Integer generarMes(Usuario usuario){
		int mesInt = UtilsGeneral.getMesInteger(cmbFechaPagar.getValue());
		mesInt--;
		Date desde = UtilsGeneral.getLastDayMonthDate(UtilsGeneral.getMesString(mesInt));
		Date hasta = UtilsGeneral.getLastDayMonthDate(cmbFechaPagar.getValue());
		return tareaService
				.getTareasByFechaAndUsuario(desde, hasta, usuario)
				.stream()
				.mapToInt(Tarea::getTotal).sum();
	}

	private void chkBoxListener(){
		chkRopa.selectedProperty()
			.addListener(
			(observable, oldValue, newValue) -> {
				  if (newValue) {
					  total+=3;
					lblRopa.setText("$3");
				  } else {
				  	total-=3;
					lblRopa.setText("$0");
				  }
				  lblTotal.setText("" + total);
				});

		chkTaza.selectedProperty()
				.addListener(
				(observable, oldValue, newValue) -> {
					if (newValue) {
						total+=5;
						lblTaza.setText("$5");
					} else {
						total-=5;
						lblTaza.setText("$0");
					}
					lblTotal.setText("" + total);
				});

		chkSerAmable.selectedProperty()
				.addListener(
				(observable, oldValue, newValue) -> {
					if (newValue) {
						total+=20;
						lblSerAmable.setText("$20");
					} else {
						total-=20;
						lblSerAmable.setText("$0");
					}
					lblTotal.setText("" + total);
				});

		chkCama.selectedProperty()
				.addListener(
				(observable, oldValue, newValue) -> {
					if (newValue) {
						total+=5;
						lblCama.setText("$5");
					} else {
						total-=5;
						lblCama.setText("$0");
					}
					lblTotal.setText("" + total);
				});

		chkAgua.selectedProperty()
				.addListener(
				(observable, oldValue, newValue) -> {
					if (newValue) {
						total+=3;
						lblAgua.setText("$3");
					} else {
						total-=3;
						lblAgua.setText("$0");
					}
					lblTotal.setText("" + total);
				});

		chkEscritorio.selectedProperty()
				.addListener(
				(observable, oldValue, newValue) -> {
					if (newValue) {
						total+=5;
						lblEscritorio.setText("$5");
					} else {
						total-=5;
						lblEscritorio.setText("$0");
					}
					lblTotal.setText("" + total);
				});

		chkCamaNoche.selectedProperty()
				.addListener(
				(observable, oldValue, newValue) -> {
					if (newValue) {
						total+=15;
						lblCamaNoche.setText("$15");
					} else {
						total-=15;
						lblCamaNoche.setText("$0");
					}
					lblTotal.setText("" +total);
				});
	}

	private void btnActualizarOnAction(){
		btnActualizar.setOnAction((event) -> updateButton());
	}

	private void updateButton(){
		if(tareaSelected.getFecha()!=null){
			cmbFecha.setValue(UtilsGeneral.getLocalDateFromDate(tareaSelected.getFecha()));
		}
		if(tareaSelected.getTotal()!=null){
			Map<String, Boolean> map = new Gson().fromJson(tareaSelected.getTareas(), Map.class);
			map.forEach((k, v) -> {
				switch (k){
					case Constantes.ROPA:
						chkRopa.setSelected(v);
						break;
					case Constantes.TAZA:
						chkTaza.setSelected(v);
						break;
					case Constantes.AMABLE:
						chkSerAmable.setSelected(v);
						break;
					case Constantes.CAMA:
						chkCama.setSelected(v);
						break;
					case Constantes.AGUA:
						chkAgua.setSelected(v);
						break;
					case Constantes.ESCRITORIO:
						chkEscritorio.setSelected(v);
						break;
					case Constantes.NOCHE:
						chkCamaNoche.setSelected(v);
				}
			});
		}
		btnGuardar.setText(ConstantesEtiquetas.ACTUALIZAR);
		mostrarFormulario();
	}

	private void btnEliminarOnAction(){
		btnEliminar.setOnAction((event) -> deleteButton());
	}

	private void deleteButton(){
		Optional<ButtonType> result = UtilsGeneral.getDialogoEliminar(paneCrud, tareaSelected.getNombre(), getClass());
		if (result.isPresent() && result.get() == ButtonType.OK) {
			try {
				tareaService.deleteTarea(tareaSelected);
				recargarTabla();
				UtilsGeneral.correct(ConstantesMensajes.ELIMINADO_OK);
			} catch (Exception ex) {
				errorLog(ex);
			}
		}
	}

	public void task() {
		Task<Void> longTask = UtilsGeneral.task();
		longTask.setOnSucceeded(t -> {
			try {
				if(TareaAppApplication.usuario.getNombre().equals("Mathias")){
					imgAmable.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constantes.MATHI))));
					imgAgua.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/drinking-bottle.png"))));
				}else if(TareaAppApplication.usuario.getNombre().equals("Agustina")){
					imgAmable.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constantes.AGUS))));
					imgAgua.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/coconut.png"))));
				}
				total = 0;
				cargarCombos();
				btnOnAction();
				buttonDisable(true);
				cargaTablaTareas();
				comboListener();
				mostrarTabla();
			} catch (Exception ex) {
				logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
				UtilsGeneral.errorEx(ex);
			}
		});
		bar.progressProperty().bind(longTask.progressProperty());
		new Thread(longTask).start();
	}

	private void cargarCombos(){
		cmbFechaReporteDesde.setItems(FXCollections.observableArrayList(Constantes.LISTA_MESES));
		cmbFechaPagar.setItems(FXCollections.observableArrayList(Constantes.LISTA_MESES));
		cmbUsuario.setItems(FXCollections.observableArrayList(usuarioService.getUsuariosNotAdmin()));

		cmbUsuarioGasto.setItems(FXCollections.observableArrayList(usuarioService.getUsuariosNotAdmin()));
		cmbUsuarioNotaCredito.setItems(FXCollections.observableArrayList(usuarioService.getUsuariosNotAdmin()));
	}

	private void btnOnAction(){
		btnActualizarOnAction();
		btnEliminarOnAction();
		btnGuardarOnAction();
		btnBackOnAction();
		btnAgregarOnAction();
		btnReporteOnAction();
		btnPagarOnAction();
		btnBackReporteOnAction();
		btnGenerarReporteOnAction();
		btnExportarExcelOnAction();
		btnGenerarPagoOnAction();
		btnGenerarGastoOnAction();
		btnGenerarNotaCreditoOnAction();
	}

	private void btnGenerarGastoOnAction(){
		btnGenerarGasto.setOnAction((event -> generarGasto()));
	}

	private void btnGenerarNotaCreditoOnAction(){
		btnGenerarNotaCredito.setOnAction((event -> generarNotaCredito()));
	}

	private void generarNotaCredito(){
		if(cmbFechaNotaCredito.getValue()!=null && !txtNotaCredito.getText().isEmpty() && cmbUsuarioNotaCredito.getValue()!=null && UtilsGeneral.isNumero(txtNotaCreditoValor.getText())){
			int nuevaNotaCredito = Integer.valueOf(txtNotaCreditoValor.getText());
			if(nuevaNotaCredito>0){
				CuentaAhorro cuentaAhorro = cuentaAhorroService.findByUsuario(cmbUsuarioNotaCredito.getValue());
				if(cuentaAhorro!=null){
					int nuevoSaldo = cuentaAhorro.getSaldo() + nuevaNotaCredito;
					try{
						cuentaAhorro.setSaldo(nuevoSaldo);
						cuentaAhorro = cuentaAhorroService.updateCuentaAhorro(cuentaAhorro);

						Movimiento movimiento = new Movimiento();
						movimiento.setConcepto(txtNotaCredito.getText());
						movimiento.setCredito(nuevaNotaCredito);
						movimiento.setDebito(0);
						movimiento.setCuentaAhorro(cuentaAhorro);
						movimiento.setSaldo(cuentaAhorro.getSaldo());
						Date fecha = UtilsGeneral.getDateFromLocalDateConHora(cmbFechaNotaCredito.getValue());
						movimiento.setFecha(fecha);
						movimientoService.saveMovimiento(movimiento);
						UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
						clearNotaCredito();
					} catch (TareasAppException ex) {
						UtilsGeneral.errorEx(ex);
					}
				} else {
					UtilsGeneral.error(Errores.CUENTA_AHORRO_INEXISTENTE);
				}
			} else {
				UtilsGeneral.error(Errores.ERROR_NOTA_CREDITO_POSTIVO);
			}
		} else {
			UtilsGeneral.error(Errores.ERROR_FALTAN_DATOS_PAGO_NOTA_CREDITO);
		}
	}

	private void generarGasto(){
		if(cmbFechaGasto.getValue()!=null && !txtGasto.getText().isEmpty() && cmbUsuarioGasto.getValue()!=null && UtilsGeneral.isNumero(txtGastoValor.getText())){
			int nuevoGasto = Integer.valueOf(txtGastoValor.getText());
			if(nuevoGasto>0){
				CuentaAhorro cuentaAhorro = cuentaAhorroService.findByUsuario(cmbUsuarioGasto.getValue());
				if(cuentaAhorro!=null){
					int nuevoSaldo = cuentaAhorro.getSaldo() - nuevoGasto;
					Optional<Movimiento> saldoTest = cuentaAhorro.getMovimientos()
							.stream()
							.filter(movimiento -> UtilsGeneral.getLocalDateFromDate(movimiento.getFecha())
									.isBefore(cmbFechaGasto.getValue()) ||
									UtilsGeneral
											.getLocalDateFromDate(movimiento.getFecha())
											.isEqual(cmbFechaGasto.getValue()))
							.filter(movimiento -> movimiento.getSaldo() - nuevoGasto > 0)
							.findFirst();
					if(saldoTest.isPresent() && nuevoSaldo>=0){
						try{
							cuentaAhorro.setSaldo(nuevoSaldo);
							cuentaAhorro = cuentaAhorroService.updateCuentaAhorro(cuentaAhorro);

							Movimiento movimiento = new Movimiento();
							movimiento.setConcepto(txtGasto.getText());
							movimiento.setCredito(0);
							movimiento.setDebito(nuevoGasto);
							movimiento.setCuentaAhorro(cuentaAhorro);
							movimiento.setSaldo(cuentaAhorro.getSaldo());
							Date fecha = UtilsGeneral.getDateFromLocalDateConHora(cmbFechaGasto.getValue());
							movimiento.setFecha(fecha);
							movimientoService.saveMovimiento(movimiento);
							UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
							clearGasto();
						} catch (TareasAppException ex) {
							UtilsGeneral.errorEx(ex);
						}
					}else {
						UtilsGeneral.error(Errores.SALDO_INSUFICIENTE);
					}
				} else {
					UtilsGeneral.error(Errores.CUENTA_AHORRO_INEXISTENTE);
				}
			}else {
				UtilsGeneral.error(Errores.ERROR_GASTO_POSTIVO);
			}
		} else {
			UtilsGeneral.error(Errores.ERROR_FALTAN_DATOS_PAGO_GASTO);
		}
	}

	private void clearNotaCredito(){
		cmbFechaNotaCredito.setValue(null);
		cmbUsuarioNotaCredito.setValue(null);
		txtNotaCredito.setText("");
		txtNotaCreditoValor.setText("");
	}

	private void clearGasto(){
		cmbFechaGasto.setValue(null);
		cmbUsuarioGasto.setValue(null);
		txtGasto.setText("");
		txtGastoValor.setText("");
	}

	private void btnGenerarPagoOnAction(){
    btnGenerarPago.setOnAction(
        (event -> cargarDinero()));
	}

	private void cargarDinero(){
		CuentaAhorro cuentaAhorro = cuentaAhorroService.findByUsuario(cmbUsuario.getValue());
		String valor = txtInfoPago.getText().substring(2);
		int nuevoSaldo = Integer.parseInt(valor);
		cuentaAhorro.setSaldo(cuentaAhorro.getSaldo() + nuevoSaldo);
		String concepto = "Sueldo " + cmbFechaPagar.getValue();
		Movimiento movimiento = movimientoService.findByCuentaAhorroAndConcepto(cuentaAhorro, concepto);
		if(movimiento!=null){
			UtilsGeneral.error(Errores.ERROR_CONCEPTO_EXISTE);
		} else {
			cuentaAhorro = cuentaAhorroService.saveCuentaAhorro(cuentaAhorro);
			movimiento = new Movimiento();
			movimiento.setConcepto("Sueldo " + cmbFechaPagar.getValue());
			movimiento.setCredito(nuevoSaldo);
			movimiento.setDebito(0);
			movimiento.setCuentaAhorro(cuentaAhorro);
			movimiento.setSaldo(cuentaAhorro.getSaldo());
			movimiento.setFecha(new Date());
			movimientoService.saveMovimiento(movimiento);
			UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
		}
	}

	private void btnGenerarReporteOnAction(){
    btnGenerarReporte.setOnAction(
        (event -> {
          if (cmbFechaReporteDesde.getValue() != null) {
			int mesInt = UtilsGeneral.getMesInteger(cmbFechaReporteDesde.getValue());
			mesInt--;
            Date desde = UtilsGeneral.getLastDayMonthDate(UtilsGeneral.getMesString(mesInt));
            Date hasta = UtilsGeneral.getLastDayMonthDate(cmbFechaReporteDesde.getValue());
            tareasReoporte =
                FXCollections.observableArrayList(
                    tareaService.getTareasByFechaAndUsuario(
                        desde, hasta, TareaAppApplication.usuario));
            tableReporteMes.getColumns().clear();
            UtilCargaTablas.cargaTablaTareas(tableReporteMes, tareasReoporte);
            totalReporte = tareasReoporte.stream().mapToInt(Tarea::getTotal).sum();
            lblInfoReporte.setText("Total del mes: $" + totalReporte);
            cargaGrafica();
          }
        }));
	}

	private void btnBackReporteOnAction(){
		btnBackReporte.setOnAction((event -> mostrarTabla()));
	}

	private void btnAgregarOnAction() {
		btnAgregar.setOnAction((event) -> nuevaTarea());
	}

	private void btnReporteOnAction(){
		btnReporte.setOnAction((event -> reporte()));
	}

	private void btnPagarOnAction(){
		btnPagar.setOnAction((event -> mostrarPagar()));
	}

	private void btnGuardarOnAction() {
		btnGuardar.setOnAction((event) -> guardar());
	}

	private void btnBackOnAction() {
		btnBack.setOnAction((event) -> mostrarTabla());
	}

	private void btnExportarExcelOnAction() { btnExportarExcel.setOnAction((event -> crearExcel()));}

	private void crearExcel(){
		try {
			File file = openFile();
			if (file != null) {
				String nombreReporte = "Tareas mes " + cmbFechaReporteDesde.getValue();
				ExportarExcel.crearExcelTarea(tableReporteMes, file.getAbsolutePath(), cmbFechaReporteDesde.getValue(), nombreReporte, String.valueOf(totalReporte));
				UtilsGeneral.correct("Se creo el archivo " + file.getName());
			} else {
				UtilsGeneral.error(Errores.ELEGIR_ARCHIVO);
			}
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private File openFile(){
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(Constantes.EXCEL, Constantes.EXTENSION_EXCEL));
		Stage stage = (Stage) cmbFechaReporteDesde.getScene().getWindow();
		return fileChooser.showSaveDialog(stage);
	}

	private void cargaTablaXTipoUsuario(){
		if(TareaAppApplication.usuario.getTipoUsuario().getNombre().equals("admin")){
			tareas = FXCollections.observableArrayList(tareaService.getTareas());
		}else{
			tareas = FXCollections.observableArrayList(tareaService.findByUsuario(TareaAppApplication.usuario));
		}
	}

	private void recargarTabla() {
		try {
			cargaTablaXTipoUsuario();
			buttonDisable(true);
			tableData.setItems(tareas);
		} catch (Exception ex) {
			errorLog(ex);
		}
	}

	private void cargaTablaTareas() {
		cargaTablaXTipoUsuario();
		UtilCargaTablas.cargaTablaTareas(tableData, tareas);
		tableData.setOnMouseClicked(
			event -> {
				Tarea t = tableData.getSelectionModel().getSelectedItem();
				if (event.getClickCount() == 2 && t != null) {
					tareaSelected = t;
					updateButton();
				}else {
					if (t != null) {
						if (tareaSelected != null) {
							if (tareaSelected != t) {
								tareaSelected = t;
								buttonDisable(false);
							} else {
								tableData.getSelectionModel().clearSelection();
								tareaSelected = null;
								buttonDisable(true);
							}
						} else {
							tareaSelected = t;
							buttonDisable(false);
						}
					}
				}
			});
	}

	private void buttonDisable(boolean disable) {
		UtilsSeguridad.setDisable(disable, MenuPrincipal.Tareas.getPagina(),btnActualizar, btnEliminar,null);
	}

	private void reporte(){
		mostrarReporte();
	}

	private void cargaGrafica(){
		List<PieChart.Data> dataList = new ArrayList<>();
		double total;
		total = tareasReoporte.stream()
						.map(Tarea::getTotal)
						.reduce(0, Integer::sum);
		Map<String, Integer> map = getMap();
		map.forEach((k, v) -> dataList.add(new PieChart.Data(k, v)));
		procesoChart(dataList, total);
	}

	private Map<String, Integer> getMap(){
		List<Map<String, Integer>> list = new ArrayList<>();
		tareasReoporte.forEach(tarea -> list.add(tarea.getTotalXTarea()));
		 return list.stream()
						.flatMap(m -> m.entrySet().stream())
						.collect(groupingBy(Map.Entry::getKey, summingInt(Map.Entry::getValue)));
	}

	private void procesoChart(List<PieChart.Data> dataList, double total){
		ObservableList<PieChart.Data> lista = FXCollections.observableArrayList(dataList);
		chartReporte.setData(lista);
		chartReporte.getData().forEach(this::installTooltip);
		clickChart(total);
	}

	private void installTooltip(PieChart.Data d) {
		String msg = String.format("%s : %s", d.getName(), d.getPieValue());
		Tooltip tt = new Tooltip(msg);
		tt.setStyle("-fx-background-color: gray; -fx-text-fill: whitesmoke;");
		Tooltip.install(d.getNode(), tt);
	}

	private void clickChart(double total){
		for (PieChart.Data data : chartReporte.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
				int num = (int) data.getPieValue();
				double porc = (((double) num * (double) 100) / total);
				DecimalFormat df = new DecimalFormat("#.#");
				df.setRoundingMode(RoundingMode.CEILING);
				String porcent = df.format(porc);
				lblInfoPieChart.setText(data.getName() + ": " + num + " (" + porcent + " % aprox.)");
			});
		}
	}

	private void nuevaTarea() {
		clear();
		LocalDate now = Instant.ofEpochMilli(new Date().getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
		cmbFecha.setValue(now);
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		mostrarFormulario();
	}

	private void mostrarPagar() {
		paneCrud.setOpacity(0);
		paneTabel.setOpacity(0);
		paneReporte.setOpacity(0);
		new FadeInUpTransition(panePagar).play();
	}

	private void mostrarReporte() {
		panePagar.setOpacity(0);
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		tableData.getSelectionModel().clearSelection();
		paneCrud.setOpacity(0);
		paneTabel.setOpacity(0);
		new FadeInUpTransition(paneReporte).play();
	}

	private void mostrarTabla() {
		panePagar.setOpacity(0);
		btnGuardar.setText(ConstantesEtiquetas.GUARDAR);
		tableData.getSelectionModel().clearSelection();
		paneCrud.setOpacity(0);
		paneReporte.setOpacity(0);
		new FadeInUpTransition(paneTabel).play();
	}

	private void mostrarFormulario(){
		panePagar.setOpacity(0);
		buttonDisable(true);
		paneTabel.setOpacity(0);
		paneReporte.setOpacity(0);
		new FadeInUpTransition(paneCrud).play();
	}

	private void clear() {
		chkRopa.setSelected(false);
		chkTaza.setSelected(false);
		chkSerAmable.setSelected(false);
		chkCama.setSelected(false);
		chkAgua.setSelected(false);
		chkEscritorio.setSelected(false);
		chkCamaNoche.setSelected(false);
	}

	private void guardar() {
		procesoGuardar();
	}

	private void procesoGuardar(){
		try{
			if(tareaSelected ==null){
				tareaSelected = new Tarea();
			}
			boolean updateProcess = tareaSelected.getId()!=null;
			Map<String, Boolean> mapChk = new HashMap<>();
			mapChk.put(Constantes.ROPA, chkRopa.isSelected());
			mapChk.put(Constantes.TAZA, chkTaza.isSelected());
			mapChk.put(Constantes.AMABLE, chkSerAmable.isSelected());
			mapChk.put(Constantes.CAMA, chkCama.isSelected());
			mapChk.put(Constantes.AGUA, chkAgua.isSelected());
			mapChk.put(Constantes.ESCRITORIO, chkEscritorio.isSelected());
			mapChk.put(Constantes.NOCHE, chkCamaNoche.isSelected());
			tareaSelected.setFecha(UtilsGeneral.getDateFromLocalDate(cmbFecha.getValue()));
			tareaSelected.setUsuario(TareaAppApplication.usuario);
			tareaSelected.setTotal(total);
			tareaSelected.setTareas(new Gson().toJson(mapChk));

			if(updateProcess){
				tareaService.updateTarea(tareaSelected);
				UtilsGeneral.correct(ConstantesMensajes.ACTUALIZADO_OK);
			}else{
				tareaService.saveTarea(tareaSelected);
				UtilsGeneral.correct(ConstantesMensajes.GUARDADO_OK);
			}
			mostrarTabla();
			recargarTabla();
			tareaSelected = null;
		}catch(TareasAppException ex){
			errorLog(ex);
		}
	}

	private void errorLog(Exception ex){
		logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
		UtilsGeneral.errorEx(ex);
	}
}
