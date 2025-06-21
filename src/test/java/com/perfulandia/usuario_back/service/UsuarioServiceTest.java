package com.perfulandia.usuario_back.service;

import com.perfulandia.usuario_back.dto.PedidoDTO;
import com.perfulandia.usuario_back.feign.PedidoClient;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.model.Usuario;
import com.perfulandia.usuario_back.repository.RolRepository;
import com.perfulandia.usuario_back.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PedidoClient pedidoClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void testObtenerPorId_Existente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void testObtenerPorId_NoExistente() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        Usuario resultado = usuarioService.obtenerPorId(2L);

        assertThat(resultado).isNull();
    }

    @Test
    void testGuardarUsuarioNuevo() {
        Usuario nuevoUsuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .rut("12345678-9")
                .correo("juan@correo.com")
                .direccion("Calle Falsa 123")
                .rawPassword("clave123")
                .build();

        when(usuarioRepository.findByRut("12345678-9")).thenReturn(Optional.empty());
        when(usuarioRepository.findByCorreo("juan@correo.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("clave123")).thenReturn("encoded123");
        when(usuarioRepository.save(any())).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        Usuario resultado = usuarioService.guardar(nuevoUsuario);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getCorreo()).isEqualTo("juan@correo.com");
        assertThat(resultado.isActivo()).isTrue();
    }

    @Test
    void testGuardarUsuarioRutDuplicado() {
        Usuario duplicado = new Usuario();
        duplicado.setRut("12345678-9");

        when(usuarioRepository.findByRut("12345678-9")).thenReturn(Optional.of(duplicado));

        Usuario usuario = new Usuario();
        usuario.setRut("12345678-9");
        usuario.setRawPassword("clave123");

        assertThatThrownBy(() -> usuarioService.guardar(usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("RUT");
    }

    @Test
    void testGuardarUsuarioCorreoDuplicado() {
        Usuario duplicado = new Usuario();
        duplicado.setCorreo("correo@correo.com");

        when(usuarioRepository.findByRut(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByCorreo("correo@correo.com")).thenReturn(Optional.of(duplicado));

        Usuario usuario = new Usuario();
        usuario.setRut("11111111-1");
        usuario.setCorreo("correo@correo.com");
        usuario.setRawPassword("clave123");

        assertThatThrownBy(() -> usuarioService.guardar(usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("correo");
    }

    @Test
    void testGuardarUsuarioSinPassword() {
        Usuario usuario = new Usuario();
        usuario.setRut("11111111-1");
        usuario.setCorreo("nuevo@correo.com");

        when(usuarioRepository.findByRut(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByCorreo(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.guardar(usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("rawPassword");
    }

    @Test
    void testActualizarUsuario() {
        Usuario existente = Usuario.builder()
                .id(1L)
                .nombre("Antiguo")
                .build();

        Usuario actualizado = Usuario.builder()
                .nombre("Nuevo")
                .rawPassword("claveNueva")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("claveNueva")).thenReturn("encodedNew");
        when(usuarioRepository.save(any())).thenReturn(existente);

        Usuario result = usuarioService.updateUsuario(1L, actualizado);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Nuevo");
    }

    @Test
    void testDesactivarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setActivo(true);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        Usuario resultado = usuarioService.desactivarUsuario(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.isActivo()).isFalse();
    }

    @Test
    void testEliminarUsuario() {
        usuarioService.eliminar(1L);
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testAsignarRolExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRoles(new HashSet<>());

        Rol rol = new Rol(1L, "ROLE_TEST");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findByNombre("ROLE_TEST")).thenReturn(Optional.of(rol));
        when(usuarioRepository.save(any())).thenReturn(usuario);

        Usuario result = usuarioService.asignarRol(1L, rol);

        assertThat(result).isNotNull();
        assertThat(result.getRoles()).contains(rol);
    }

    @Test
    void testObtenerUsuariosDesactivados() {
        Usuario inactivo = new Usuario();
        inactivo.setActivo(false);

        when(usuarioRepository.findUsuariosDesactivados()).thenReturn(List.of(inactivo));

        List<Usuario> resultado = usuarioService.obtenerUsuariosDesactivados();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).isActivo()).isFalse();
    }

    @Test
    void testObtenerPedidosUsuario() {
        List<PedidoDTO> pedidos = List.of(new PedidoDTO(), new PedidoDTO());

        when(pedidoClient.getPedidosByUsuario(1L)).thenReturn(pedidos);

        List<PedidoDTO> resultado = usuarioService.obtenerPedidosUsuario(1L);

        assertThat(resultado).hasSize(2);
    }
}
