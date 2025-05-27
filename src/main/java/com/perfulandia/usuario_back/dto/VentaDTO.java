package com.perfulandia.usuario_back.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class VentaDTO {
    private Long id;
    private String cliente;
    private Double total;
    private LocalDate fecha;
}
