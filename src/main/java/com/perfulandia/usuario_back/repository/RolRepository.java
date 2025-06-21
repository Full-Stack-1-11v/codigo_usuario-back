package com.perfulandia.usuario_back.repository;

import com.perfulandia.usuario_back.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Rol}.
 * Proporciona operaciones CRUD y consultas personalizadas sobre la tabla de roles.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * @param nombre el nombre del rol a buscar
     * @return un {@link Optional} que contiene el rol si se encuentra, o vacío si no existe
     */
    Optional<Rol> findByNombre(String nombre);
}
