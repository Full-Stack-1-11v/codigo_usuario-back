package com.perfulandia.usuario_back.service;

import com.perfulandia.usuario_back.dto.PedidoDTO;
import com.perfulandia.usuario_back.feign.PedidoClient;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.model.Usuario;
import com.perfulandia.usuario_back.repository.RolRepository;
import com.perfulandia.usuario_back.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio relacionada con los usuarios del sistema.
 * Permite operaciones como creación, actualización, eliminación, asignación de roles
 * y obtención de pedidos a través de un cliente Feign.
 */
@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PedidoClient pedidoClient;

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return lista de usuarios
     */
    public List<Usuario> obtenerTodos() {
        logger.info("Obteniendo todos los usuarios");
        return usuarioRepo.findAll();
    }

    /**
     * Busca un usuario por su identificador.
     * @param id ID del usuario
     * @return el usuario encontrado o null si no existe
     */
    public Usuario obtenerPorId(Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        return usuarioRepo.findById(id).orElse(null);
    }

    /**
     * Guarda un nuevo usuario validando unicidad de RUT/correo y encriptando la contraseña.
     * @param usuario objeto usuario a guardar
     * @return usuario guardado
     * @throws RuntimeException si ya existe un usuario con el RUT o correo, o si falta contraseña
     */
    public Usuario guardar(Usuario usuario) {
        logger.info("Guardando nuevo usuario con RUT: {}", usuario.getRut());

        if (usuario.getRut() != null && usuarioRepo.findByRut(usuario.getRut()).isPresent()) {
            logger.warn("Ya existe un usuario con el RUT {}", usuario.getRut());
            throw new RuntimeException("El usuario con ese RUT ya existe");
        }

        if (usuario.getCorreo() != null && usuarioRepo.findByCorreo(usuario.getCorreo()).isPresent()) {
            logger.warn("Ya existe un usuario con el correo {}", usuario.getCorreo());
            throw new RuntimeException("El usuario con ese correo ya existe");
        }

        if (usuario.getRawPassword() == null || usuario.getRawPassword().isBlank()) {
            logger.error("rawPassword es nulo o vacío");
            throw new RuntimeException("rawPassword no puede ser nulo o vacío");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getRawPassword()));
        usuario.setActivo(true);
        return usuarioRepo.save(usuario);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param id ID del usuario a actualizar
     * @param usuario objeto con los nuevos datos
     * @return usuario actualizado o null si no existe
     */
    public Usuario updateUsuario(Long id, Usuario usuario) {
        logger.info("Actualizando usuario con ID: {}", id);
        Optional<Usuario> optUsuario = usuarioRepo.findById(id);
        if (optUsuario.isEmpty()) {
            logger.warn("Usuario con ID {} no encontrado", id);
            return null;
        }

        Usuario existente = optUsuario.get();
        existente.setNombre(usuario.getNombre() != null ? usuario.getNombre() : existente.getNombre());
        existente.setApellido(usuario.getApellido() != null ? usuario.getApellido() : existente.getApellido());
        existente.setRut(usuario.getRut() != null ? usuario.getRut() : existente.getRut());
        existente.setCorreo(usuario.getCorreo() != null ? usuario.getCorreo() : existente.getCorreo());
        existente.setDireccion(usuario.getDireccion() != null ? usuario.getDireccion() : existente.getDireccion());

        if (usuario.getRawPassword() != null && !usuario.getRawPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(usuario.getRawPassword()));
        }

        return usuarioRepo.save(existente);
    }

    /**
     * Marca como inactivo a un usuario sin eliminarlo.
     * @param id ID del usuario
     * @return usuario desactivado o null si no existe
     */
    public Usuario desactivarUsuario(Long id) {
        logger.info("Desactivando usuario con ID: {}", id);
        Optional<Usuario> opt = usuarioRepo.findById(id);
        if (opt.isEmpty()) return null;
        Usuario usuario = opt.get();
        usuario.setActivo(false);
        return usuarioRepo.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario
     */
    public void eliminar(Long id) {
        logger.info("Eliminando usuario con ID: {}", id);
        usuarioRepo.deleteById(id);
    }

    /**
     * Asigna un rol a un usuario específico. Si el rol no existe, lo crea.
     * @param usuarioId ID del usuario
     * @param rol rol a asignar
     * @return usuario actualizado o null si el usuario no existe
     */
    public Usuario asignarRol(Long usuarioId, Rol rol) {
        logger.info("Asignando rol '{}' al usuario con ID: {}", rol.getNombre(), usuarioId);
        Optional<Usuario> optUsuario = usuarioRepo.findById(usuarioId);
        if (optUsuario.isEmpty()) return null;

        Usuario usuario = optUsuario.get();
        Optional<Rol> optRol = rolRepo.findByNombre(rol.getNombre());
        Rol rolEncontrado = optRol.orElseGet(() -> rolRepo.save(rol));

        usuario.getRoles().add(rolEncontrado);
        return usuarioRepo.save(usuario);
    }

    /**
     * Obtiene los usuarios desactivados del sistema.
     * @return lista de usuarios inactivos
     */
    public List<Usuario> obtenerUsuariosDesactivados() {
        logger.info("Obteniendo usuarios desactivados");
        return usuarioRepo.findUsuariosDesactivados();
    }

    /**
     * Obtiene los pedidos asociados a un usuario desde el microservicio.
     * @param usuarioId ID del usuario
     * @return lista de pedidos DTO
     */
    public List<PedidoDTO> obtenerPedidosUsuario(Long usuarioId) {
        logger.info("Consultando pedidos del usuario ID: {}", usuarioId);
        return pedidoClient.getPedidosByUsuario(usuarioId);
    }
}
