package com.perfulandia.usuario_back.controller;

import com.perfulandia.usuario_back.assembler.UsuarioModelAssembler;


import com.perfulandia.usuario_back.dto.PedidoDTO;
import com.perfulandia.usuario_back.dto.UsuarioDTO;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.model.Usuario;
import com.perfulandia.usuario_back.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link UsuarioController}.
 * Valida la lógica de los endpoints expuestos usando mocks del servicio y assembler.
 */
class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioModelAssembler usuarioModelAssembler;



    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private EntityModel<UsuarioDTO> entityModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Maykol");
        usuario.setCorreo("maykol@mail.com");
        usuario.setActivo(true);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNombre("Maykol");
        usuarioDTO.setCorreo("maykol@mail.com");
        usuarioDTO.setActivo(true);

        entityModel = EntityModel.of(usuarioDTO);
    }

    @Test
    void debeListarUsuarios() {
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario));
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> response = usuarioController.listarUsuarios();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertTrue(response.getBody().getContent().stream().anyMatch(model -> model.getContent().getCorreo().equals("maykol@mail.com")));
    }

    @Test
    void debeListarUsuariosActivos() {
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario));
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> response = usuarioController.listarUsuariosActivos();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertTrue(response.getBody().getContent().stream().allMatch(u -> u.getContent().isActivo()));
    }

    @Test
    void debeListarUsuariosDesactivados() {
        usuario.setActivo(false);
        usuarioDTO.setActivo(false);
        when(usuarioService.obtenerUsuariosDesactivados()).thenReturn(List.of(usuario));
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<CollectionModel<EntityModel<UsuarioDTO>>> response = usuarioController.listarUsuariosDesactivados();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getContent().isEmpty());
    }

    @Test
    void debeCrearUsuario() {
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<EntityModel<UsuarioDTO>> response = usuarioController.crearUsuario(usuario);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Maykol", response.getBody().getContent().getNombre());
    }

    @Test
    void debeLanzarExcepcionAlCrearUsuarioDuplicado() {
        when(usuarioService.guardar(any(Usuario.class))).thenThrow(new RuntimeException("Duplicado"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> usuarioController.crearUsuario(usuario));

        assertEquals(409, ex.getStatusCode().value());
        assertEquals("409 CONFLICT \"Duplicado\"", ex.getMessage());
    }

    @Test
    void debeActualizarUsuario() {
        when(usuarioService.updateUsuario(eq(1L), any(Usuario.class))).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<EntityModel<UsuarioDTO>> response = usuarioController.actualizarUsuario(1L, usuario);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Maykol", response.getBody().getContent().getNombre());
    }

    @Test
    void debeLanzarExcepcionSiNoExisteAlActualizar() {
        when(usuarioService.updateUsuario(eq(1L), any(Usuario.class))).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioController.actualizarUsuario(1L, usuario));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void debeDesactivarUsuario() {
        when(usuarioService.desactivarUsuario(1L)).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<EntityModel<UsuarioDTO>> response = usuarioController.desactivarUsuario(1L);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().getContent().isActivo());
    }

    @Test
    void debeLanzarExcepcionSiNoExisteAlDesactivar() {
        when(usuarioService.desactivarUsuario(1L)).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioController.desactivarUsuario(1L));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void debeEliminarUsuario() {
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);
        doNothing().when(usuarioService).eliminar(1L);

        ResponseEntity<Void> response = usuarioController.eliminarUsuario(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(usuarioService, times(1)).eliminar(1L);
    }

    @Test
    void debeLanzarExcepcionSiNoExisteAlEliminar() {
        when(usuarioService.obtenerPorId(1L)).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioController.eliminarUsuario(1L));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void debeAsignarRol() {
        Rol rol = new Rol(10L, "ADMIN");

        when(usuarioService.asignarRol(1L, rol)).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<EntityModel<UsuarioDTO>> response = usuarioController.asignarRol(1L, rol);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Maykol", response.getBody().getContent().getNombre());
    }

    @Test
    void debeLanzarExcepcionSiNoSePuedeAsignarRol() {
        when(usuarioService.asignarRol(eq(1L), any(Rol.class))).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioController.asignarRol(1L, new Rol()));

        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void debeObtenerPedidosPorUsuario() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(123L);

        when(usuarioService.obtenerPedidosUsuario(1L)).thenReturn(List.of(pedido));

        ResponseEntity<List<PedidoDTO>> response = usuarioController.getPedidosByUsuario(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(123L, response.getBody().get(0).getId());
    }

    @Test
    void debeObtenerUsuarioPorId() {
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);
        when(usuarioModelAssembler.toModel(usuario)).thenReturn(entityModel);

        ResponseEntity<EntityModel<UsuarioDTO>> response = usuarioController.obtenerUsuarioPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Maykol", response.getBody().getContent().getNombre());
    }

    @Test
    void debeLanzarExcepcionSiUsuarioNoExistePorId() {
        when(usuarioService.obtenerPorId(1L)).thenReturn(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioController.obtenerUsuarioPorId(1L));

        assertEquals(404, ex.getStatusCode().value());
    }
}
