package com.software.tareasApp.utils;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.enums.MenuSeguridad;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import eu.hansolo.enzo.notification.Notification;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

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

    public static String traeNombrePagina(String str, String menuActual) {
        String pagina = MenuPrincipal.Inicio.getPagina();
        switch (menuActual) {
            case Constantes.MENU_SEGURIDAD:
                pagina = getMenuSeguridad(str);
                break;
            case Constantes.MENU_PRINCIPAL:
                pagina = getMenuPrincipal(str);
                break;
          }
        return pagina;
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

}
