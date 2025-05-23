package com.perfulandia.usuario_back.controller;

import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private RolService rolService;

   
    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles() {
        List<Rol> roles = rolService.getAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 si no hay roles
        }
        return ResponseEntity.ok(roles);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerRolPorId(@PathVariable Long id) {
        Optional<Rol> rol = rolService.getRolById(id);
        return rol.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

   
    @PostMapping
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        Rol nuevo = rolService.saveRol(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id) {
        if (rolService.getRolById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Long id, @RequestBody Rol rolActualizado) {
        Optional<Rol> rolExistente = rolService.getRolById(id);
        if (rolExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        rolActualizado.setId(id);
        Rol rolGuardado = rolService.saveRol(rolActualizado);
        return ResponseEntity.ok(rolGuardado);
    }
}
