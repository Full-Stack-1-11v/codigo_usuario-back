package com.perfulandia.usuario_back.repository;

import com.perfulandia.usuario_back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByRut(String rut);

    @Query("SELECT u FROM Usuario u WHERE u.activo = false")
    List<Usuario> findUsuariosDesactivados();
}
