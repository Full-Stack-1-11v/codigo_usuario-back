package com.perfulandia.usuario_back.controller;

import com.perfulandia.usuario_back.assembler.RolModelAssembler;
import com.perfulandia.usuario_back.dto.RolDTO;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.service.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST para gestionar operaciones relacionadas con los roles.
 * Permite listar, obtener, crear, actualizar y eliminar roles del sistema.
 * Todas las respuestas están estructuradas con HATEOAS.
 */
@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Operaciones relacionadas con los roles de usuarios")
public class RolController {

    private static final Logger logger = LoggerFactory.getLogger(RolController.class);

    private final RolService rolService;
    private final RolModelAssembler rolModelAssembler;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param rolService servicio encargado de la lógica de negocio de roles.
     * @param rolModelAssembler ensamblador que convierte entidades Rol en modelos HATEOAS.
     */
    public RolController(RolService rolService, RolModelAssembler rolModelAssembler) {
        this.rolService = rolService;
        this.rolModelAssembler = rolModelAssembler;
    }

    /**
     * Lista todos los roles registrados en el sistema.
     *
     * @return respuesta con la colección de roles en formato HATEOAS o 204 si no hay roles.
     */
    @Operation(summary = "Listar todos los roles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles listados correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolDTO.class))),
        @ApiResponse(responseCode = "204", description = "No hay roles disponibles", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RolDTO>>> listarRoles() {
        logger.info("GET /roles");

        List<Rol> roles = rolService.obtenerTodos();
        if (roles.isEmpty()) {
            logger.warn("No se encontraron roles en la base de datos");
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<RolDTO>> rolesModel = roles.stream()
                .map(rolModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RolDTO>> collectionModel = CollectionModel.of(rolesModel,
                linkTo(methodOn(RolController.class).listarRoles()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    /**
     * Obtiene un rol específico según su ID.
     *
     * @param id identificador del rol.
     * @return el rol encontrado en formato HATEOAS o 404 si no existe.
     */
    @Operation(summary = "Obtener un rol por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado correctamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolDTO.class))),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RolDTO>> obtenerRolPorId(@PathVariable Long id) {
        logger.info("GET /roles/{}", id);

        Optional<Rol> rol = rolService.obtenerPorId(id);
        return rol.map(r -> {
            logger.info("Rol encontrado con ID {}", id);
            return ResponseEntity.ok(rolModelAssembler.toModel(r));
        }).orElseGet(() -> {
            logger.warn("Rol con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        });
    }

    /**
     * Crea un nuevo rol en el sistema.
     *
     * @param rolDTO DTO con el nombre del rol a crear.
     * @return el rol creado con enlace HATEOAS.
     */
    @Operation(summary = "Crear un nuevo rol")
    @PostMapping
    public ResponseEntity<EntityModel<RolDTO>> crearRol(@Valid @RequestBody RolDTO rolDTO) {
        logger.info("POST /roles - Creando rol {}", rolDTO.getNombre());
        Rol nuevoRol = rolService.guardar(new Rol(null, rolDTO.getNombre()));
        return ResponseEntity
                .created(linkTo(methodOn(RolController.class).obtenerRolPorId(nuevoRol.getId())).toUri())
                .body(rolModelAssembler.toModel(nuevoRol));
    }

    /**
     * Actualiza el nombre de un rol existente.
     *
     * @param id     ID del rol a actualizar.
     * @param rolDTO DTO con el nuevo nombre del rol.
     * @return el rol actualizado o 404 si no existe.
     */
    @Operation(summary = "Actualizar un rol existente")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<RolDTO>> actualizarRol(@PathVariable Long id, @Valid @RequestBody RolDTO rolDTO) {
        logger.info("PUT /roles/{} - Actualizando rol", id);
        Optional<Rol> rolExistente = rolService.obtenerPorId(id);

        if (rolExistente.isEmpty()) {
            logger.warn("Rol con ID {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }

        Rol actualizado = rolExistente.get();
        actualizado.setNombre(rolDTO.getNombre());
        Rol rolGuardado = rolService.guardar(actualizado);

        return ResponseEntity.ok(rolModelAssembler.toModel(rolGuardado));
    }

    /**
     * Elimina un rol del sistema por su ID.
     *
     * @param id ID del rol a eliminar.
     * @return 204 si fue eliminado, o 404 si no se encontró.
     */
    @Operation(summary = "Eliminar un rol por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        logger.info("DELETE /roles/{}", id);
        if (rolService.obtenerPorId(id).isEmpty()) {
            logger.warn("Rol con ID {} no encontrado para eliminar", id);
            return ResponseEntity.notFound().build();
        }
        rolService.eliminar(id);
        logger.info("Rol con ID {} eliminado", id);
        return ResponseEntity.noContent().build();
    }
}
