package com.perfulandia.usuario_back.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) para representar la información resumida de un pedido.
 * Este objeto es utilizado para enviar datos al cliente sin exponer la estructura completa del modelo.
 *
 * <p>Incluye el ID del pedido, la fecha y una lista con los nombres de los productos asociados.</p>
 */
@Data
public class PedidoDTO {

    /**
     * Identificador único del pedido.
     */
    private Long id;

    /**
     * Fecha en la que se realizó el pedido.
     */
    private LocalDate fecha;

    /**
     * Lista con los nombres de los productos incluidos en el pedido.
     */
    private List<String> nombresProductos;
}
