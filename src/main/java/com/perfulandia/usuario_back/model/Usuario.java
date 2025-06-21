package com.perfulandia.usuario_back.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa a un usuario dentro del sistema Perfulandia.
 * Un usuario puede tener uno o varios roles asignados que determinan sus permisos.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    /**
     * Identificador único del usuario.
     * Se genera automáticamente mediante la estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario.
     * No puede ser nulo.
     */
    @Column(nullable = false)
    private String nombre;

    /**
     * Apellido del usuario.
     * No puede ser nulo.
     */
    @Column(nullable = false)
    private String apellido;

    /**
     * RUT (Rol Único Tributario) del usuario.
     * Debe ser único y no nulo.
     * Longitud máxima: 12 caracteres.
     */
    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    /**
     * Correo electrónico del usuario.
     * Debe ser único y no nulo.
     */
    @Column(nullable = false, unique = true)
    private String correo;

    /**
     * Dirección física del usuario.
     * No puede ser nula.
     */
    @Column(nullable = false)
    private String direccion;

    /**
     * Contraseña del usuario, almacenada de forma segura (generalmente cifrada).
     * Este campo es ignorado en las respuestas JSON para mayor seguridad.
     */
    @JsonIgnore
    @Column(name = "contrasena", nullable = false)
    private String password;

    /**
     * Indica si el usuario está activo o no.
     * Por defecto es verdadero.
     */
    @Column(nullable = false)
    private boolean activo = true;

    /**
     * Conjunto de roles asignados al usuario.
     * La relación es muchos a muchos y se carga de forma inmediata (EAGER).
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    /**
     * Campo transitorio utilizado para almacenar la contraseña sin encriptar temporalmente.
     * No se persiste en la base de datos.
     */
    @Transient
    private String rawPassword;
}
