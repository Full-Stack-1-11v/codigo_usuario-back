package com.perfulandia.usuario_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.usuario_back.assembler.RolModelAssembler;
import com.perfulandia.usuario_back.dto.RolDTO;
import com.perfulandia.usuario_back.model.Rol;
import com.perfulandia.usuario_back.service.RolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
@AutoConfigureMockMvc(addFilters = false)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolService rolService;

    @MockBean
    private RolModelAssembler rolModelAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    /* ---------- LISTAR ---------- */
    @Test
    void testListar() throws Exception {
        Rol rol1 = new Rol(1L, "ROLE_ADMIN");
        Rol rol2 = new Rol(2L, "ROLE_USER");
        RolDTO dto1 = new RolDTO(1L, "ROLE_ADMIN");
        RolDTO dto2 = new RolDTO(2L, "ROLE_USER");

        when(rolService.obtenerTodos()).thenReturn(List.of(rol1, rol2));
        when(rolModelAssembler.toModel(rol1)).thenReturn(EntityModel.of(dto1));
        when(rolModelAssembler.toModel(rol2)).thenReturn(EntityModel.of(dto2));

        mockMvc.perform(get("/roles"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$._embedded.rolDTOList", hasSize(2)))
               .andExpect(jsonPath("$._embedded.rolDTOList[0].id", is(1)))
               .andExpect(jsonPath("$._embedded.rolDTOList[0].nombre", is("ROLE_ADMIN")))
               .andExpect(jsonPath("$._embedded.rolDTOList[1].id", is(2)))
               .andExpect(jsonPath("$._embedded.rolDTOList[1].nombre", is("ROLE_USER")));
    }

    /* ---------- OBTENER POR ID ---------- */
    @Test
    void testObtenerPorId_Existente() throws Exception {
        Rol rol = new Rol(1L, "ROLE_USER");
        RolDTO dto = new RolDTO(1L, "ROLE_USER");

        when(rolService.obtenerPorId(1L)).thenReturn(Optional.of(rol));
        when(rolModelAssembler.toModel(rol)).thenReturn(EntityModel.of(dto));

        mockMvc.perform(get("/roles/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.nombre", is("ROLE_USER")));
    }

    @Test
    void testObtenerPorId_NoExistente() throws Exception {
        when(rolService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/roles/99"))
               .andExpect(status().isNotFound());
    }

    /* ---------- CREAR ---------- */
    @Test
    void testCrear() throws Exception {
        RolDTO nuevoDTO = new RolDTO(null, "ROLE_NEW");
        Rol rolGuardado = new Rol(5L, "ROLE_NEW");
        RolDTO dtoGuardado = new RolDTO(5L, "ROLE_NEW");

        when(rolService.guardar(any())).thenReturn(rolGuardado);
        when(rolModelAssembler.toModel(rolGuardado)).thenReturn(EntityModel.of(dtoGuardado));

        mockMvc.perform(post("/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(nuevoDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(5)))
               .andExpect(jsonPath("$.nombre", is("ROLE_NEW")));
    }

    /* ---------- ACTUALIZAR ---------- */
    @Test
    void testActualizarExistente() throws Exception {
        RolDTO peticion = new RolDTO(null, "ROLE_MANAGER");       // lo que llega del cliente
        Rol rolExistente = new Rol(1L, "ROLE_USER");              // lo que ya está en BD
        Rol rolActualizado = new Rol(1L, "ROLE_MANAGER");         // lo que devuelve el servicio
        RolDTO dtoActualizado = new RolDTO(1L, "ROLE_MANAGER");   // lo que responderá el assembler

        when(rolService.obtenerPorId(1L)).thenReturn(Optional.of(rolExistente));
        when(rolService.guardar(any(Rol.class))).thenReturn(rolActualizado);
        when(rolModelAssembler.toModel(rolActualizado)).thenReturn(EntityModel.of(dtoActualizado));

        mockMvc.perform(put("/roles/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(peticion)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.nombre", is("ROLE_MANAGER")));
    }

    @Test
    void testActualizar_NoExistente() throws Exception {
        RolDTO peticion = new RolDTO(null, "ROLE_MANAGER");

        when(rolService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/roles/99")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(peticion)))
               .andExpect(status().isNotFound());
    }

    /* ---------- ELIMINAR ---------- */
    @Test
    void testEliminarExistente() throws Exception {
        Rol rol = new Rol(1L, "ROLE_ADMIN");

        when(rolService.obtenerPorId(1L)).thenReturn(Optional.of(rol));
        doNothing().when(rolService).eliminar(1L);

        mockMvc.perform(delete("/roles/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarNoExistente() throws Exception {
        when(rolService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/roles/99"))
               .andExpect(status().isNotFound());
    }
}
