package com.perfulandia.usuario_back.feign;

import com.perfulandia.usuario_back.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * Cliente Feign para comunicarse con el microservicio de pedidos.
 *
 * <p>Esta interfaz permite realizar peticiones HTTP al servicio de pedidos desde
 * el microservicio de usuarios, utilizando la anotación {@code @FeignClient}.</p>
 *
 * <p>El nombre lógico del servicio es {@code pedido-service} y la URL por defecto
 * es {@code http://localhost:8081}. Puedes ajustar esta URL según tu entorno de despliegue.</p>
 */
@FeignClient(name = "pedido-service", url = "http://localhost:8081")
public interface PedidoClient {

    /**
     * Obtiene la lista de pedidos asociados a un usuario por su ID.
     *
     * @param usuarioId el ID del usuario del cual se desean obtener los pedidos
     * @return una lista de objetos {@link PedidoDTO} que representan los pedidos del usuario
     */
    @GetMapping("/api/pedidos/usuario/{usuarioId}")
    List<PedidoDTO> getPedidosByUsuario(@PathVariable("usuarioId") Long usuarioId);
}
