// VentaClient.java
package com.perfulandia.usuario_back.feign;

import com.perfulandia.usuario_back.dto.VentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "venta-service", url = "https://codigo-venta-back.onrender.com") // ajusta el puerto
public interface VentaClient {
    @GetMapping("/api/ventas/usuario/{usuarioId}")
    List<VentaDTO> getVentasByUsuario(@PathVariable("usuarioId") Long usuarioId);
}
