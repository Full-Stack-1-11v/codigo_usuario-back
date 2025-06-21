package com.perfulandia.usuario_back.assembler;

import com.perfulandia.usuario_back.controller.RolController;
import com.perfulandia.usuario_back.dto.RolDTO;
import com.perfulandia.usuario_back.model.Rol;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Ensamblador que transforma una entidad {@link Rol} en un {@link EntityModel} con su representación
 * {@link RolDTO} y enlaces HATEOAS.
 * <p>
 * Este componente es utilizado para enriquecer la salida de la API REST con hipervínculos que permiten
 * la navegación entre recursos relacionados.
 * </p>
 */
@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<RolDTO>> {

    /**
     * Convierte una entidad {@link Rol} en un modelo {@link EntityModel} que contiene su DTO
     * y los enlaces necesarios para cumplir con HATEOAS.
     *
     * @param rol La entidad {@code Rol} a convertir.
     * @return Un {@code EntityModel<RolDTO>} con los datos del rol y enlaces navegables.
     */
    @Override
    public EntityModel<RolDTO> toModel(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());

        return EntityModel.of(dto,
                linkTo(methodOn(RolController.class).obtenerRolPorId(rol.getId())).withSelfRel(),
                linkTo(methodOn(RolController.class).listarRoles()).withRel("roles")
        );
    }
}
