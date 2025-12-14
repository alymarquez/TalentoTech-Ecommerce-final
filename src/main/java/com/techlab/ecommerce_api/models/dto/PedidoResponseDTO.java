package com.techlab.ecommerce_api.models.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime fechaCreacion;
    private String estado;
    private Double total;
    private String mensaje;
    private List<Map<String, Object>> productos;

    public PedidoResponseDTO(Long id, LocalDateTime fechaCreacion, String estado,
            Double total, String mensaje) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.total = total;
        this.mensaje = mensaje;
    }
}