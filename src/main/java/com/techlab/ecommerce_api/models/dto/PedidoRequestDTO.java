package com.techlab.ecommerce_api.models.dto;

import lombok.Data;
import java.util.Map;

@Data
public class PedidoRequestDTO {
    private String usuarioNombre;
    private String usuarioEmail;
    private Map<Long, Integer> productos;
}