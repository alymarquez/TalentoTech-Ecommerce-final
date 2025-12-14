package com.techlab.ecommerce_api.models.dto;

import lombok.Data;

@Data
public class CarritoItemDTO {
    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private String imagenUrl;
    private Integer stockDisponible;
}