package com.software.tareasApp.controller;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.domain.service.PermisoUsuarioService;
import com.software.tareasApp.domain.service.UsuarioService;
import com.software.tareasApp.enums.Errores;
import com.software.tareasApp.enums.MenuPrincipal;
import com.software.tareasApp.enums.MenuSeguridad;
import com.software.tareasApp.persistence.model.PermisoUsuario;
import com.software.tareasApp.utils.UtilsGeneral;
import com.software.tareasApp.view.animations.FadeInUpTransition;
import com.software.tareasApp.view.constantes.ConstantesEtiquetas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.software.tareasApp.view.constantes.Constantes.EXTENSION_FXML;
import static com.software.tareasApp.view.constantes.Constantes.MENU_PRINCIPAL;
import static com.software.tareasApp.view.constantes.Constantes.MENU_SEGURIDAD;
import static com.software.tareasApp.view.constantes.Constantes.PAGINA_LOGIN;
import static com.software.tareasApp.view.constantes.Constantes.PAGINA_MAIN;
import static com.software.tareasApp.view.constantes.Constantes.PAGINA_ROOT;


@Component
public class MenuController implements Initializable {

    @FXML
    public Button btnMaximize;
    @FXML
    public Button btnMinimize;
    @FXML
    public Button btnResize;
    @FXML
    public Button btnFullscreen;
    @FXML
    public Button btnLogout;
    @FXML
    public Button btnClose;

    @FXML
    public AnchorPane menuPane;

    Stage stage;
    Rectangle2D rec2;
    Double w, h;
    @FXML
    public ListView<String> listMenu;
    @FXML
    public AnchorPane paneData;

    List<String> lista;
    public String menuActual = ConstantesEtiquetas.VACIO;
    List<PermisoUsuario> listaPermisos = new ArrayList<>();

    @Autowired
    public UsuarioService usuarioService;

    @Autowired
    private PermisoUsuarioService permisoUsuarioService;

    private final LogManagerClass logger = new LogManagerClass(getClass());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rec2 = Screen.getPrimary().getVisualBounds();
        w = 0.1;
        h = 0.1;
        listaPermisos = permisoUsuarioService.findAllByUserType(TareaAppApplication.usuario.getTipoUsuario());
        lista = cargaLista(MENU_PRINCIPAL);
        menuActual = MENU_PRINCIPAL;
        listMenu.getItems().addAll(lista);
        Platform.runLater(() -> {
            UtilsGeneral.correct("Se logueo Correctamente");
            stage = (Stage) btnMaximize.getScene().getWindow();
            stage.setMaximized(true);
            stage.setHeight(rec2.getHeight());
            btnMaximize.getStyleClass().add(ConstantesEtiquetas.DECORATION_BUTTON_RESTORE);
            btnResize.setVisible(false);
            btnOnAction();
            UtilsGeneral.loadAnchorPane(getClass(),paneData, PAGINA_MAIN, MainController.class);
            listMenu.requestFocus();
        });
    }

    private void btnOnAction(){
        btnFullscreenOnAction();
        btnMinimizeOnAction();
        btnMaximizeOnAction();
        btnCloseOnAction();
        btnResizeOnAction();
        lisViewOnAction();
        btnLogoutOnAction();
    }

    private void btnLogoutOnAction(){
        btnLogout.setOnAction(this::logout);
    }

    private void btnResizeOnAction() {
        btnResize.setOnAction(this::resize);
    }

    private void lisViewOnAction(){
        listMenu.setOnMouseClicked(this::clicListMenu);
    }

    private void btnCloseOnAction() {
        btnClose.setOnAction(this::close);
    }

    private void btnMaximizeOnAction() {
        btnMaximize.setOnAction(this::maximized);
    }

    private void btnMinimizeOnAction() {
        btnMinimize.setOnAction(this::minimize);
    }

    private void btnFullscreenOnAction() {
        btnFullscreen.setOnAction(this::fullscreen);
    }

    @FXML
    public void maximized(ActionEvent event) {
        stage = (Stage) btnMaximize.getScene().getWindow();
        if (stage.isMaximized()) {
            if (w == rec2.getWidth() && h == rec2.getHeight()) {
                stage.setMaximized(false);
                stage.setHeight(600);
                stage.setWidth(800);
                stage.centerOnScreen();
                btnMaximize.getStyleClass().remove(ConstantesEtiquetas.DECORATION_BUTTON_RESTORE);
                btnResize.setVisible(true);
            } else {
                stage.setMaximized(false);
                btnMaximize.getStyleClass().remove(ConstantesEtiquetas.DECORATION_BUTTON_RESTORE);
                btnResize.setVisible(true);
            }

        } else {
            stage.setMaximized(true);
            stage.setHeight(rec2.getHeight());
            btnMaximize.getStyleClass().add(ConstantesEtiquetas.DECORATION_BUTTON_RESTORE);
            btnResize.setVisible(false);
        }
    }

    @FXML
    public void minimize(ActionEvent event) {
        stage = (Stage) btnMinimize.getScene().getWindow();
        if (stage.isMaximized()) {
            w = rec2.getWidth();
            h = rec2.getHeight();
            stage.setMaximized(false);
            stage.setHeight(h);
            stage.setWidth(w);
            stage.centerOnScreen();
            Platform.runLater(() -> stage.setIconified(true));
        } else {
            stage.setIconified(true);
        }
    }

    @FXML
    public void resize(ActionEvent event) {
    }

    @FXML
    public void fullscreen(ActionEvent event) {
        stage = (Stage) btnFullscreen.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    public void close(ActionEvent event) {
        logger.info(TareaAppApplication.usuario, "Cierra aplicacion");
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void clicListMenu(MouseEvent event) {
        try {
            if(listMenu.getSelectionModel().getSelectedItem()!=null){
                String ruta = UtilsGeneral.traeNombrePagina(listMenu.getSelectionModel().getSelectedItem(), menuActual);
                cargaMenu(ruta);
            }
        } catch (Exception ex) {
            errorLog(ex);
        }
    }

    public void cargaMenu(String ruta) {
        List<String> listaMenu;
        listMenu.getItems().clear();
        Class className;
        switch (ruta) {
            case "Inicio":
                listaMenu = cargaLista(MENU_PRINCIPAL);
                menuActual = MENU_PRINCIPAL;
                className = getController(ruta);
                ruta = creaRuta(ruta);
                UtilsGeneral.loadAnchorPane(getClass(),paneData, ruta, className);
                break;
            case "Seguridad":
                listaMenu = cargaLista(MENU_SEGURIDAD);
                menuActual = MENU_SEGURIDAD;
                break;
            default:
                className = getController(ruta);
                ruta = creaRuta(ruta);
                if(className!=null){
                    UtilsGeneral.loadAnchorPane(getClass(),paneData, ruta, className);
                }else{
                    UtilsGeneral.error(Errores.NO_IMPLEMENTADO);
                }
                listaMenu = cargaLista(MENU_PRINCIPAL);
                menuActual = MENU_PRINCIPAL;
        }
        listMenu.getItems().addAll(listaMenu);
        new FadeInUpTransition(menuPane).play();
    }

    public Class getController(String ruta){
        switch(menuActual){
            case MENU_PRINCIPAL:
                return MenuPrincipal.valueOf(ruta).getController();
            case MENU_SEGURIDAD:
                return MenuSeguridad.valueOf(ruta).getController();
            default:
                return null;
        }
    }

    public void logout(ActionEvent event) {
        try {
            logger.info(TareaAppApplication.usuario, "Cierra Sesion");
            TareaAppApplication.usuario = null;
            UtilsGeneral.openNewWindow(stage,getClass(),LoginController.class, PAGINA_LOGIN);
        } catch (IOException ex) {
            errorLog(ex);
        }
    }

    public String creaRuta(String ruta) {
        String rutaNueva;
        rutaNueva = PAGINA_ROOT + ruta + EXTENSION_FXML;
        return rutaNueva;
    }

    public List<String> cargaLista(String str) {
        List<String> listaCarga = new ArrayList<>();
        switch (str) {
            case MENU_SEGURIDAD:
                getMenuSeguridad(listaCarga);
                break;
            case MENU_PRINCIPAL:
                getMenuPrincipal(listaCarga);
                break;
        }
        return listaCarga;
    }

    private void getMenuPrincipal(List<String> listaCarga){
        Arrays.stream(MenuPrincipal.values()).forEach(p -> {
            listaCarga.addAll(listaPermisos.stream()
                    .filter(pu -> pu.getPagina().equals(p.getPagina()))
                    .map(f -> f.getPagina()).collect(Collectors.toList()));
        });
    }

    private void getMenuSeguridad(List<String> listaCarga){
        Arrays.stream(MenuSeguridad.values()).forEach(p -> {
            listaCarga.addAll(listaPermisos.stream()
                    .filter(pu -> pu.getPagina().equals(p.getPagina()))
                    .map(f -> f.getPagina()).collect(Collectors.toList()));
        });
    }

    private void errorLog(Exception ex){
        logger.error(TareaAppApplication.usuario, ex.getMessage(), ex);
        UtilsGeneral.errorEx(ex);
    }
}
