// PedidoClient.java
package com.perfulandia.usuario_back.feign;

import com.perfulandia.usuario_back.dto.PedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "pedido-service", url = "http://localhost:8081") // ajusta el puerto
public interface PedidoClient {
    @GetMapping("/api/pedidos/usuario/{usuarioId}")
    List<PedidoDTO> getPedidosByUsuario(@PathVariable("usuarioId") Long usuarioId);
}
