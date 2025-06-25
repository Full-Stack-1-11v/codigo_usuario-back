package com.perfulandia.usuario_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

/**
 * DTO (Data Transfer Object) para representar un rol en las respuestas de la API.
 *
 * <p>Este objeto es utilizado para transferir datos del rol de manera segura entre el backend
 * y el cliente, evitando exponer la entidad completa.</p>
 *
 * <p>Incluye el identificador del rol y su nombre.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "rolDTOList")
public class RolDTO {

    /**
     * Identificador único del rol.
     */
    private Long id;

    /**
     * Nombre del rol (por ejemplo, "ROLE_ADMIN", "ROLE_USER").
     */
    private String nombre;
}
