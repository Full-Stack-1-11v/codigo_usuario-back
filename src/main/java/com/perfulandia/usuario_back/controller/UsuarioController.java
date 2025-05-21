package com.perfulandia.usuario_back.controller;

import com.perfulandia.usuario_back.model.Usuario;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.saveUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.updateUsuario(id, usuario);
        if (actualizado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id) {
        Usuario desactivado = usuarioService.desactivarUsuario(id);
        if (desactivado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(desactivado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<Usuario> asignarRol(@PathVariable Long id, @RequestBody Rol rol) {
        Usuario usuarioConRol = usuarioService.asignarRol(id, rol);
        if (usuarioConRol == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario o Rol no encontrado");
        }
        return ResponseEntity.ok(usuarioConRol);
    }
}
