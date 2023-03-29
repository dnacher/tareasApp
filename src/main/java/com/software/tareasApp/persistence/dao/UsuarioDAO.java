package com.software.tareasApp.persistence.dao;

import com.software.tareasApp.Logger.LogManagerClass;
import com.software.tareasApp.TareaAppApplication;
import com.software.tareasApp.exceptions.TareasAppException;
import com.software.tareasApp.exceptions.UAuthException;
import com.software.tareasApp.persistence.model.TipoUsuario;
import com.software.tareasApp.persistence.model.Usuario;
import com.software.tareasApp.persistence.repository.UsuarioRepository;
import com.software.tareasApp.view.constantes.Constantes;
import com.software.tareasApp.view.constantes.ConstantesErrores;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioDAO {

    private final LogManagerClass log = new LogManagerClass(getClass());
    private final UsuarioRepository repository;
    private final Integer LOG_ROUNDS = 10;

    @Autowired
    public UsuarioDAO(UsuarioRepository repository){
        this.repository = repository;
    }

    public List<Usuario> getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        this.repository.findAll().forEach(user -> usuarios.add(user));
        log.info(TareaAppApplication.usuario, "getUsuarios");
        return usuarios;
    }

    public Usuario getUsuarioById(Integer id) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "getUsuarioById " + id);
        return this.repository
                .findById(id)
                .orElseThrow(
                        () -> {
                            String msg = String.format("The user id %s does not exist", id);
                            log.error(TareaAppApplication.usuario, msg);
                            return new TareasAppException(msg);
                        });
    }

    public Usuario saveUsuario(Usuario usuario) throws TareasAppException {
        log.info(TareaAppApplication.usuario, "saveUsuario " + usuario);
        String hashedPassword= BCrypt.hashpw(usuario.getPassword(),BCrypt.gensalt(LOG_ROUNDS));
        usuario.setPassword(hashedPassword);
        return this.repository.save(usuario);
    }

    public Usuario validarUsuario(String nombre, String password) throws UAuthException {
        log.info(TareaAppApplication.usuario, "validaDatosUsuario " + nombre);
        Usuario hashedUsuario = repository.findByNombre(nombre);
        if (hashedUsuario != null) {
          if (BCrypt.checkpw(password, hashedUsuario.getPassword())) {
            return hashedUsuario;
          }
        }
        log.warn(ConstantesErrores.ERROR_CREDENCIALES);
        throw new UAuthException(ConstantesErrores.ERROR_CREDENCIALES);
    }


    public List<Usuario> saveUsuarios(List<Usuario> usuarios) throws TareasAppException {
        List<Usuario> finalList = new ArrayList<>();
        this.repository
                .saveAll(usuarios)
                .forEach(
                        user -> {
                            finalList.add(user);
                        });
        return finalList;
    }

    public void deleteUsuario(Usuario usuario) {
        log.info(TareaAppApplication.usuario, "deleteUsuario " + usuario.toStringLog());
        this.repository.delete(usuario);
    }

    public Usuario updateUsuario(Usuario usuario) throws TareasAppException {
        if (usuario.getId() != null) {
            log.info(TareaAppApplication.usuario, "updateUsuario " + usuario.toStringLog());
            String hashedPassword= BCrypt.hashpw(usuario.getPassword(),BCrypt.gensalt(LOG_ROUNDS));
            usuario.setPassword(hashedPassword);
            return this.repository.save(usuario);
        } else {
            String msg = String.format("Cannot update a user without an Id");
            log.error(TareaAppApplication.usuario, msg);
            throw new TareasAppException(msg);
        }
    }

    public void updateSinPass(String nombreUsuario, TipoUsuario tipoUsuario, Integer id){
        repository.updateUsuarioSinPass(nombreUsuario, tipoUsuario, id);
    }

    public Integer countByNombre(String nombre){
        return this.repository.countByNombre(nombre);
    }
}
