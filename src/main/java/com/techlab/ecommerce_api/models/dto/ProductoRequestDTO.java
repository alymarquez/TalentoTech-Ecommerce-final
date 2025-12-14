package com.techlab.ecommerce_api.models.dto;

import lombok.Data;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private String descripcion;
    private Double precio;
    private String categoriaNombre;
    private String imagenUrl;
    private Integer stock;
}
