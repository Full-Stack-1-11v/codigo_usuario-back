package com.perfulandia.usuario_back.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private LocalDate fecha;
    private List<String> nombresProductos; // Solo los nombres de los productos
}
