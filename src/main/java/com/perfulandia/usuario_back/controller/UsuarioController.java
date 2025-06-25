package com.perfulandia.usuario_back.controller;

import com.perfulandia.usuario_back.assembler.UsuarioModelAssembler;
import com.perfulandia.usuario_back.dto.PedidoDTO;
import com.perfulandia.usuario_back.dto.UsuarioDTO;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.model.Usuario;
import com.perfulandia.usuario_back.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST que gestiona las operaciones CRUD y acciones adicionales
 * relacionadas con los usuarios del sistema.
 *
 * <p>Todas las respuestas se devuelven en formato HATEOAS para cumplir con los
 * principios de REST auto-descubrible.</p>
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioModelAssembler;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioService       servicio de lógica de negocio de usuarios
     * @param usuarioModelAssembler ensamblador que convierte {@link Usuario}
     *                              en modelos HATEOAS ({@link UsuarioDTO})
     */
    public UsuarioController(UsuarioService usuarioService,
                             UsuarioModelAssembler usuarioModelAssembler) {
        this.usuarioService = usuarioService;
        this.usuarioModelAssembler = usuarioModelAssembler;
    }

    // -------------------------------------------------------------------------
    // LISTAR
    // -------------------------------------------------------------------------

    /**
     * Lista todos los usuarios registrados.
     *
     * @return colección HATEOAS de usuarios
     */
    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente")
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listarUsuarios() {
        logger.info("GET /api/usuarios/listar");
        List<EntityModel<UsuarioDTO>> usuarios = usuarioService.obtenerTodos()
                .stream()
                .map(usuarioModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel()));
    }

    /**
     * Lista únicamente los usuarios activos.
     *
     * @return colección HATEOAS de usuarios activos
     */
    @Operation(summary = "Listar usuarios activos")
    @GetMapping("/activos")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listarUsuariosActivos() {
        logger.info("GET /api/usuarios/activos");
        List<EntityModel<UsuarioDTO>> activos = usuarioService.obtenerTodos()
                .stream()
                .filter(Usuario::isActivo)
                .map(usuarioModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                activos,
                linkTo(methodOn(UsuarioController.class).listarUsuariosActivos()).withSelfRel()));
    }

    /**
     * Lista los usuarios que han sido desactivados.
     *
     * @return colección HATEOAS de usuarios desactivados
     */
    @Operation(summary = "Listar usuarios desactivados")
    @GetMapping("/desactivados")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> listarUsuariosDesactivados() {
        logger.info("GET /api/usuarios/desactivados");
        List<EntityModel<UsuarioDTO>> desactivados = usuarioService.obtenerUsuariosDesactivados()
                .stream()
                .map(usuarioModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(
                desactivados,
                linkTo(methodOn(UsuarioController.class).listarUsuariosDesactivados()).withSelfRel()));
    }

    // -------------------------------------------------------------------------
    // CREAR
    // -------------------------------------------------------------------------

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario entidad usuario recibida en el body
     * @return usuario creado en formato HATEOAS
     * @throws ResponseStatusException si el usuario ya existe (409)
     */
    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "409", description = "Usuario ya existe", content = @Content)
    })
    @PostMapping("/crear")
    public ResponseEntity<EntityModel<UsuarioDTO>> crearUsuario(@RequestBody Usuario usuario) {
        logger.info("POST /api/usuarios/crear - {}", usuario.getCorreo());
        try {
            Usuario nuevo = usuarioService.guardar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(usuarioModelAssembler.toModel(nuevo));
        } catch (RuntimeException e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // ACTUALIZAR, DESACTIVAR, ELIMINAR
    // -------------------------------------------------------------------------

    /**
     * Actualiza un usuario existente por ID.
     *
     * @param id      ID del usuario a actualizar
     * @param usuario datos nuevos
     * @return usuario actualizado o 404 si no existe
     */
    @Operation(summary = "Actualizar un usuario por ID")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> actualizarUsuario(@PathVariable Long id,
                                                                     @RequestBody Usuario usuario) {
        logger.info("PUT /api/usuarios/actualizar{}", id);
        Usuario actualizado = usuarioService.updateUsuario(id, usuario);
        if (actualizado == null) {
            logger.warn("Usuario con ID {} no encontrado", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(usuarioModelAssembler.toModel(actualizado));
    }

    /**
     * Desactiva (soft-delete) un usuario por ID.
     *
     * @param id ID del usuario
     * @return usuario desactivado o 404 si no existe
     */
    @Operation(summary = "Desactivar usuario por ID")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<EntityModel<UsuarioDTO>> desactivarUsuario(@PathVariable Long id) {
        logger.info("PATCH /api/usuarios/{}/desactivar", id);
        Usuario desactivado = usuarioService.desactivarUsuario(id);
        if (desactivado == null) {
            logger.warn("Usuario con ID {} no encontrado para desactivar", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(usuarioModelAssembler.toModel(desactivado));
    }

    /**
     * Elimina un usuario de forma permanente.
     *
     * @param id ID del usuario
     * @return 204 si se eliminó, 404 si no existe
     */
    @Operation(summary = "Eliminar usuario por ID")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        logger.info("DELETE /api/usuarios/eliminar{}", id);
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            logger.warn("Usuario con ID {} no encontrado para eliminar", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------------------------
    // ROLES y PEDIDOS
    // -------------------------------------------------------------------------

    /**
     * Asigna un rol a un usuario existente.
     *
     * @param id  ID del usuario
     * @param rol rol a asignar
     * @return usuario con el rol asignado o 404 si usuario/rol no existe
     */
    @Operation(summary = "Asignar rol a un usuario")
    @PostMapping("/{id}/roles")
    public ResponseEntity<EntityModel<UsuarioDTO>> asignarRol(@PathVariable Long id, @RequestBody Rol rol) {
        logger.info("POST /api/usuarios/{}/roles - Asignando rol: {}", id, rol.getNombre());
        Usuario usuarioConRol = usuarioService.asignarRol(id, rol);
        if (usuarioConRol == null) {
            logger.warn("Usuario o rol no encontrado para ID {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario o Rol no encontrado");
        }
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuarioConRol));
    }

    /**
     * Obtiene los pedidos asociados a un usuario.
     *
     * @param id ID del usuario
     * @return lista de pedidos en formato DTO
     */
    @Operation(summary = "Listar pedidos de un usuario por ID")
    @GetMapping("/{id}/pedidos")
    public ResponseEntity<List<PedidoDTO>> getPedidosByUsuario(@PathVariable Long id) {
        logger.info("GET /api/usuarios/{}/pedidos", id);
        return ResponseEntity.ok(usuarioService.obtenerPedidosUsuario(id));
    }

    // -------------------------------------------------------------------------
    // OBTENER POR ID
    // -------------------------------------------------------------------------

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return usuario encontrado en formato HATEOAS o 404 si no existe
     */
    @Operation(summary = "Obtener un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioDTO>> obtenerUsuarioPorId(@PathVariable Long id) {
        logger.info("GET /api/usuarios/{}", id);
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            logger.warn("Usuario con ID {} no encontrado", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
    }
}
