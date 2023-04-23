package com.software.tareasApp.Logger;

import com.software.tareasApp.persistence.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogManagerClass {

    public Logger logger;

    public LogManagerClass(Class<?> className) {
        logger = LogManager.getLogger(className);
    }

    public void error(Usuario nombreUsuario,String error){
        logger.error(getnombreUsuario(nombreUsuario) + error);
    }

    public void error(Usuario nombreUsuario,String error, Exception ex){
        logger.error(getnombreUsuario(nombreUsuario) + error, ex);
    }

    public void info(Usuario nombreUsuario, String info){
        logger.info(getnombreUsuario(nombreUsuario) + info);
    }

    public void warn(String warn){
        logger.warn(warn);
    }

    public void warn(Usuario nombreUsuario,String warn, Exception ex){
        logger.warn(getnombreUsuario(nombreUsuario) + warn, ex);
    }

    private String getnombreUsuario(Usuario usuario){
        if(usuario!=null){
            return "[" + usuario.getNombre() + "] :: ";
        }else{
            return "[ SIN USUARIO LOGUEADO ] :: ";
        }
    }

}
