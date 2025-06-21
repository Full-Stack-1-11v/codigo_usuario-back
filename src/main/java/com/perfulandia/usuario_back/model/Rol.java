package com.perfulandia.usuario_back.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un rol dentro del sistema.
 * Los roles definen los permisos o niveles de acceso de los usuarios.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    /**
     * Identificador único del rol.
     * Se genera automáticamente mediante la estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol.
     * Este campo debe ser único y no nulo, con un máximo de 50 caracteres.
     * Ejemplos: ROLE_ADMIN, ROLE_USER, ROLE_CLIENTE.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
