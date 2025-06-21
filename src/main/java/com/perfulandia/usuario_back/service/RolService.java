package com.perfulandia.usuario_back.service;

import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.repository.RolRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio relacionada con los roles de usuarios.
 * Proporciona operaciones CRUD utilizando el repositorio de roles.
 */
@Service
public class RolService {

    /**
     * Logger para registrar información durante la ejecución del servicio.
     */
    private static final Logger logger = LoggerFactory.getLogger(RolService.class);

    /**
     * Repositorio que permite interactuar con la base de datos de roles.
     */
    @Autowired
    private RolRepository rolRepository;

    /**
     * Obtiene todos los roles registrados en el sistema.
     *
     * @return una lista de objetos {@link Rol}
     */
    public List<Rol> obtenerTodos() {
        logger.info("Obteniendo todos los roles");
        return rolRepository.findAll();
    }

    /**
     * Busca un rol por su identificador único.
     *
     * @param id identificador del rol
     * @return un {@link Optional} que puede contener el rol si existe
     */
    public Optional<Rol> obtenerPorId(Long id) {
        logger.info("Buscando rol con ID: {}", id);
        return rolRepository.findById(id);
    }

    /**
     * Guarda un nuevo rol o actualiza uno existente en la base de datos.
     *
     * @param rol objeto {@link Rol} a guardar
     * @return el rol guardado
     */
    public Rol guardar(Rol rol) {
        logger.info("Guardando rol: {}", rol.getNombre());
        return rolRepository.save(rol);
    }

    /**
     * Elimina un rol de la base de datos por su ID.
     *
     * @param id identificador del rol a eliminar
     */
    public void eliminar(Long id) {
        logger.info("Eliminando rol con ID: {}", id);
        rolRepository.deleteById(id);
    }
}
