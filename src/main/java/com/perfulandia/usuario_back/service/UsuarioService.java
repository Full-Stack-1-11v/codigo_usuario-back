package com.perfulandia.usuario_back.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.perfulandia.usuario_back.model.Usuario;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.repository.UsuarioRepository;
import com.perfulandia.usuario_back.repository.RolRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RolRepository rolRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepo.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepo.findById(id).orElse(null);
    }

    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getRut() != null && usuarioRepo.findByRut(usuario.getRut()).isPresent()) {
            throw new RuntimeException("El usuario con ese RUT ya existe");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepo.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {
        Optional<Usuario> optUsuario = usuarioRepo.findById(id);
        if (optUsuario.isEmpty()) return null;

        Usuario existente = optUsuario.get();
        existente.setNombre(usuario.getNombre() != null ? usuario.getNombre() : existente.getNombre());
        existente.setApellido(usuario.getApellido() != null ? usuario.getApellido() : existente.getApellido());
        existente.setRut(usuario.getRut() != null ? usuario.getRut() : existente.getRut());
        existente.setCorreo(usuario.getCorreo() != null ? usuario.getCorreo() : existente.getCorreo());
        existente.setDireccion(usuario.getDireccion() != null ? usuario.getDireccion() : existente.getDireccion());

        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepo.save(existente);
    }

    public Usuario desactivarUsuario(Long id) {
        Optional<Usuario> opt = usuarioRepo.findById(id);
        if (opt.isEmpty()) return null;
        Usuario usuario = opt.get();
        usuario.setActivo(false);
        return usuarioRepo.save(usuario);
    }

    public void deleteUsuario(Long id) {
        usuarioRepo.deleteById(id);
    }

    public Usuario asignarRol(Long usuarioId, Rol rol) {
        Optional<Usuario> optUsuario = usuarioRepo.findById(usuarioId);
        if (optUsuario.isEmpty()) return null;

        Usuario usuario = optUsuario.get();

        Optional<Rol> optRol = rolRepo.findByNombre(rol.getNombre());
        Rol rolEncontrado;
        if (optRol.isPresent()) {
            rolEncontrado = optRol.get();
        } else {
            rolEncontrado = rolRepo.save(rol);
        }

        usuario.getRoles().add(rolEncontrado);
        return usuarioRepo.save(usuario);
    }
}

