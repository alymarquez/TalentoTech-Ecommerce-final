package com.techlab.ecommerce_api.models.dto;

import lombok.Data;
import java.util.List;

@Data
public class CarritoDTO {
    private List<CarritoItemDTO> items;
    private Double total;
    private Integer cantidadTotal;
}
