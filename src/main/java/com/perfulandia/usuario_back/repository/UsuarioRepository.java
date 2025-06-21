package com.perfulandia.usuario_back.repository;

import com.perfulandia.usuario_back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Usuario}.
 * Proporciona operaciones CRUD y consultas personalizadas para la tabla de usuarios.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo el correo electrónico del usuario
     * @return un {@link Optional} que contiene el usuario si se encuentra, o vacío si no existe
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Busca un usuario por su RUT.
     *
     * @param rut el RUT del usuario
     * @return un {@link Optional} que contiene el usuario si se encuentra, o vacío si no existe
     */
    Optional<Usuario> findByRut(String rut);

    /**
     * Recupera todos los usuarios que están desactivados (campo activo = false).
     *
     * @return una lista de usuarios desactivados
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = false")
    List<Usuario> findUsuariosDesactivados();
}
