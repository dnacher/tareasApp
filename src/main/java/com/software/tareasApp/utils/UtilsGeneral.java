package com.software.tareasApp.utils;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.enums.*;
import com.software.tareasApp.persistence.model.Poliza;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import eu.hansolo.enzo.notification.Notification;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.software.tareasApp.TareaAppApplication.applicationContext;

public class UtilsGeneral {

    public static final Notification.Notifier notifier = Notification.Notifier.INSTANCE;
    private static final LogManagerClass logger = new LogManagerClass(UtilsGeneral.class);

    public static boolean isNotNullEmptyUserInfo(String name, String password){
        if (name == null || name.isEmpty()) {
            error(Errores.ERROR_LOGIN_FALTAN_DATOS_NOMBRE);
            return false;
        } else if (password == null || password.isEmpty()) {
            error(Errores.ERROR_LOGIN_FALTAN_DATOS_PASS);
            return false;
        }
        return true;
    }

    public static void info(String message){
        Notification.Notifier.setWidth(400);
        int milllis = 1000;
        notifier.setPopupLifetime(new Duration(milllis));
        notifier.notifyInfo(ConstantesEtiquetas.INFO, message);
        sleepNotifier(milllis);
    }

    public static void correct(String message){
        Notification.Notifier.setWidth(300);
        int milllis = 1000;
        notifier.setPopupLifetime(new Duration(milllis));
        notifier.notifyInfo(ConstantesEtiquetas.CORRECTO, message);
        sleepNotifier(milllis);
    }

    public static void warning(Errores message){
        Notification.Notifier.setWidth(400);
        int milllis = 3000;
        notifier.setPopupLifetime(new Duration(milllis));
        notifier.notifyWarning(crearEtiquetaWarning(message), message.getError());
        sleepNotifier(milllis);
    }

    public static int error(Errores message){
        Notification.Notifier.setWidth(400);
        int milllis = 4000;
        notifier.setPopupLifetime(new Duration(milllis));
        notifier.notifyError(crearEtiquetaError(message), message.getError());
        sleepNotifier(milllis);
        return message.getCodigo();
    }

    public static void errorEx(Exception message){
        Notification.Notifier.setWidth(400);
        int milllis = 4000;
        notifier.setPopupLifetime(new Duration(milllis));
        notifier.notifyError(ConstantesEtiquetas.ERROR_GENERAL, message.getMessage());
        sleepNotifier(milllis);
    }

    private static String crearEtiquetaError(Errores message){
        return ConstantesEtiquetas.ERROR +  "1000- " + message.getCodigo();
    }

    private static String crearEtiquetaWarning(Errores message){
        return ConstantesEtiquetas.INFO + "- " + message.getCodigo();
    }

    private static void sleepNotifier(Integer seconds){
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(seconds+2000),
                event -> notifier.stop()));
        timeline.play();
    }

    public static void openNewWindow(Stage stage, Class getClass, Class controller, String resource) throws IOException {
        stage.close();
        FXMLLoader loader = new FXMLLoader(getClass.getResource(resource));
        loader.setController(applicationContext.getBean(controller));
        Parent root = loader.load();
        Stage st = new Stage();
        Scene scene = new Scene(root);
        st.initStyle(StageStyle.UNDECORATED);
        st.setResizable(true);
        st.setScene(scene);
        st.getIcons().add(new Image(Objects.requireNonNull(getClass.getResourceAsStream(Constantes.LOGO))));
        st.show();
    }

    public static void loadAnchorPane(Class getClass, AnchorPane ap, String a, Class classObject){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass.getResource(a));
            if(classObject!=null){
                loader.setController(applicationContext.getBean(classObject));
            }
            AnchorPane p = loader.load();
            ap.getChildren().setAll(p);
        } catch (IOException ex) {
            logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
            errorEx(ex);
        }
    }

    public static Date getDateFromLocalDate(LocalDate localDate) {
        if(localDate!=null){
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }else{
            return null;
        }
    }

    public static LocalDate getLocalDateFromDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static boolean esNumero(String s) {
        boolean esNumero;
        try {
            Double.parseDouble(s);
            esNumero = true;
        } catch (Exception ex) {
            esNumero = false;
        }
        return esNumero;
    }

    public static boolean esPorcentaje(String s){
        if(esNumero(s)){
            return Double.valueOf(s) >= 0 && Double.valueOf(s) <= 100;
        } else{
            return false;
        }
    }

    public static Date getLastDayNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getFirstDayNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Integer getFirstDayMonth(int month){
        month--;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.get(Calendar.DATE);
    }

    public static Integer getLastDayMonth(int month) {
        month--;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.get(Calendar.DATE);
    }

    public static Date getFirstDayMonthDate(String monthString){
        int month = getMesInteger(monthString);
        month--;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),0,0);
        return cal.getTime();
    }

    public static Date getLastDayMonthDate(String monthString) {
        int month = getMesInteger(monthString);
        month--;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static String getMesString(Integer mes){
        switch(mes){
            case 1:
                return "Enero";
            case 2:
                return "Febrero";
            case 3:
                return "Marzo";
            case 4:
                return "Abril";
            case 5:
                return "Mayo";
            case 6:
                return "Junio";
            case 7:
                return "Julio";
            case 8:
                return "Agosto";
            case 9:
                return "Setiembre";
            case 10:
                return "Octubre";
            case 11:
                return "Noviembre";
            case 12:
                return "Diciembre";
            default:
                return "";
        }
    }

    public static Integer getMesInteger(String mes){
        switch(mes){
            case "Enero":
                return 1;
            case "Febrero":
                return 2;
            case "Marzo":
                return 3;
            case "Abril":
                return 4;
            case "Mayo":
                return 5;
            case "Junio":
                return 6;
            case "Julio":
                return 7;
            case "Agosto":
                return 8;
            case "Setiembre":
                return 9;
            case "Octubre":
                return 10;
            case "Noviembre":
                return 11;
            case "Diciembre":
                return 12;
            default:
                return -1;
        }
    }

    public static boolean numeroPositivo(String value){
        return esNumero(value) && Integer.valueOf(value)>0;
    }

    public static String getFechaFormato(Date date) {
        if(date!=null){
            String fecha="";
            LocalDate ld = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if(ld.getDayOfMonth()<10){
                fecha+= "0" + ld.getDayOfMonth() + "-";
            }else{
                fecha+= ld.getDayOfMonth() + "-";
            }
            if(ld.getMonthValue()<10){
                fecha+= "0" + ld.getMonthValue() + "-";
            }else{
                fecha+= ld.getMonthValue() + "-";
            }
            return fecha + ld.getYear();
        }else{
            return "";
        }
    }

    public static Task task(){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int max = 50;
                for (int i = 1; i <= max; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, max);
                    Thread.sleep(20);
                }
                return null;
            }
        };
    }

    public static boolean isNullOrEmpty(String str){
        return (str == null || str.isEmpty());
    }

    public static String traeNombrePagina(String str, String menuActual) {
        String pagina = MenuPrincipal.Inicio.getPagina();
        switch (menuActual) {
            case Constantes.MENU_CONFIGURACION:
              pagina = getMenuConfiguracion(str);
              break;
            case Constantes.MENU_SEGURIDAD:
                pagina = getMenuSeguridad(str);
                break;
            case Constantes.MENU_PRINCIPAL:
                pagina = getMenuPrincipal(str);
                break;
            case Constantes.MENU_ESTADOS:
                pagina = getMenuEstados(str);
                break;
          }
        return pagina;
    }

    private static String getMenuEstados(String str){
        Optional<MenuEstados> optional = Arrays.stream(MenuEstados.values()).
                filter(menu -> menu.getPagina().equals(str)).
                findFirst();
        if(optional.isPresent()){
            return optional.get().getPagina();
        }else return ConstantesEtiquetas.VACIO;
    }

    private static String getMenuConfiguracion(String str){
        Optional<MenuConfiguracion> optional = Arrays.stream(MenuConfiguracion.values()).
                filter(menu -> menu.getPagina().equals(str)).
                findFirst();
        if(optional.isPresent()){
            return optional.get().getPagina();
        }else return ConstantesEtiquetas.VACIO;
    }

    private static String getMenuSeguridad(String str){
        Optional<MenuSeguridad> optional = Arrays.stream(MenuSeguridad.values()).
                filter(menu -> menu.getPagina().equals(str)).
                findFirst();
        if(optional.isPresent()){
            return optional.get().getPagina();
        }else return ConstantesEtiquetas.VACIO;
    }

    private static String getMenuPrincipal(String str){
        Optional<MenuPrincipal> optional = Arrays.stream(MenuPrincipal.values()).
                filter(menu -> menu.getPagina().equals(str)).
                findFirst();
        if(optional.isPresent()){
            return optional.get().getPagina();
        }else return ConstantesEtiquetas.VACIO;
    }

    public static ImageView traeEstado(String estado) {
        ImageView iv;
        switch (estado) {
            case "finalizado":
                iv = new ImageView(Constantes.VERDE);
                break;
            case "en proceso":
                iv = new ImageView(Constantes.AMARILLO);
                break;
            default:
                iv = new ImageView(Constantes.ROJO);
                break;
        }
        iv.setFitHeight(18);
        iv.setFitWidth(18);
        return iv;
    }

    public static Poliza creaPoliza(Poliza poliza){
        Poliza newPoliza = new Poliza();
        newPoliza.setId(poliza.getId());
        newPoliza.setCompania(poliza.getCompania());
        newPoliza.setCliente(poliza.getCliente());
        newPoliza.setNumeroPoliza(poliza.getNumeroPoliza());
        newPoliza.setComienzo(poliza.getComienzo());
        newPoliza.setVencimiento(poliza.getVencimiento());
        newPoliza.setProducto(poliza.getProducto());
        newPoliza.setTipoProducto(poliza.getTipoProducto());
        newPoliza.setPremio(poliza.getPremio());
        newPoliza.setPrima(poliza.getPrima());
        newPoliza.setMoneda(poliza.getMoneda());
        newPoliza.setComisionPorcentaje(poliza.getComisionPorcentaje());
        newPoliza.setFormaPago(poliza.getFormaPago());
        newPoliza.setCuotas(poliza.getCuotas());
        newPoliza.setComienzoCuota(poliza.getComienzoCuota());
        newPoliza.setFinCuota(poliza.getFinCuota());
        newPoliza.setImporteCuota(poliza.getImporteCuota());
        newPoliza.setCerradoPor(poliza.getCerradoPor());
        newPoliza.setEsApp(poliza.getEsApp());
        newPoliza.setEstado(poliza.getEstado());
        newPoliza.setVendedor(poliza.getVendedor());
        newPoliza.setPolizaMadre(poliza.getPolizaMadre());
        return newPoliza;
    }

    public static Optional<ButtonType> getDialogoEliminar(AnchorPane pane, String valorAEliminar, Class getClass){
        Stage stage = (Stage) pane.getScene().getWindow();
        Alert.AlertType type = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.getDialogPane().setHeaderText("Se eliminará " + valorAEliminar);
        DialogPane dialog = alert.getDialogPane();
        dialog.getStylesheets().add(Objects.requireNonNull(getClass.getResource("/css/Metro-UI.css")).toString());
        dialog.getStyleClass().add("dialog");
        return alert.showAndWait();
    }

    public static Optional<ButtonType> getDialogoCierreAnio(AnchorPane pane, String valorAEliminar, Class getClass){
        Stage stage = (Stage) pane.getScene().getWindow();
        Alert.AlertType type = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.getDialogPane().setHeaderText("Se cerrara el año " + valorAEliminar);
        DialogPane dialog = alert.getDialogPane();
        dialog.getStylesheets().add(Objects.requireNonNull(getClass.getResource("/css/Metro-UI.css")).toString());
        dialog.getStyleClass().add("dialog");
        return alert.showAndWait();
    }

    public static void textArea(Label lblInfoObservaciones, TextArea txtObservaciones){
        final int MAX_CHARS = 500;
        lblInfoObservaciones.setText(MAX_CHARS + "");
        txtObservaciones.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= MAX_CHARS ? change : null));
        txtObservaciones.textProperty().addListener((observable, oldValue, newValue) -> {
            int characterLeft = MAX_CHARS - txtObservaciones.getText().length();
            if(characterLeft<50){
                lblInfoObservaciones.setTextFill(Paint.valueOf("#b80d07"));
            }else{
                lblInfoObservaciones.setTextFill(Paint.valueOf("#2b579a"));
            }
            lblInfoObservaciones.setText(characterLeft + "");
        });
    }

    public static String creaNumeroPolizaEndoso(String numeroPoliza){
        String nuevoNumeroPoliza;
        String[] parts = numeroPoliza.split("_");
        if(parts.length>1){
            int nuevo = Integer.parseInt(parts[1]);
            nuevo++;
            nuevoNumeroPoliza = parts[0] + "_" + nuevo;
        }else{
            nuevoNumeroPoliza = numeroPoliza + "_1";
        }
        return nuevoNumeroPoliza;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void refreshBusqueda(TextField txtPolizaBusqueda){
        if(!isNullOrEmpty(txtPolizaBusqueda.getText())){
            StringBuffer sb = new StringBuffer(txtPolizaBusqueda.getText());
            String tet = txtPolizaBusqueda.getText();
            sb.deleteCharAt(sb.length()-1);
            txtPolizaBusqueda.setText(sb.toString());
            txtPolizaBusqueda.setText(tet);
        }
    }

    //uuid
    private String generateUuid(){
        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();
        UUID uuid= new UUID(most64SigBits, least64SigBits);
        return uuid.toString();
    }

    private long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }

    private long get64MostSignificantBitsForVersion1() {
        LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
        java.time.Duration duration = java.time.Duration.between(start, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
    }
}
