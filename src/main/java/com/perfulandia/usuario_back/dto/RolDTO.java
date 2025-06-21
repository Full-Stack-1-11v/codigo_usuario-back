package com.perfulandia.usuario_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

/**
 * DTO para representar un rol en las respuestas de la API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "rolDTOList")
public class RolDTO {
    private Long id;
    private String nombre;
}
