package com.techlab.ecommerce_api.controllers;

import com.techlab.ecommerce_api.models.dto.CarritoDTO;
import com.techlab.ecommerce_api.models.dto.CarritoItemDTO;
import com.techlab.ecommerce_api.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin("*")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/calcular")
    public ResponseEntity<CarritoDTO> calcularCarrito(@RequestBody Map<Long, Integer> productos) {
        CarritoDTO carrito = new CarritoDTO();
        List<CarritoItemDTO> items = new ArrayList<>();
        Double total = 0.0;
        Integer cantidadTotal = 0;

        for (Map.Entry<Long, Integer> entry : productos.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            productoService.findById(productoId).ifPresent(producto -> {
                CarritoItemDTO item = new CarritoItemDTO();
                item.setProductoId(producto.getId());
                item.setNombre(producto.getNombre());
                item.setPrecio(producto.getPrecio());
                item.setCantidad(cantidad);
                item.setImagenUrl(producto.getImagenUrl());
                item.setStockDisponible(producto.getStock());
                items.add(item);
            });

            total += productoService.findById(productoId)
                    .map(p -> p.getPrecio() * cantidad)
                    .orElse(0.0);
            cantidadTotal += cantidad;
        }

        carrito.setItems(items);
        carrito.setTotal(total);
        carrito.setCantidadTotal(cantidadTotal);

        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/validar-stock")
    public ResponseEntity<?> validarStock(@RequestBody Map<Long, Integer> productos) {
        List<String> errores = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : productos.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            if (!productoService.hayStockSuficiente(productoId, cantidad)) {
                productoService.findById(productoId)
                        .ifPresent(producto -> errores.add(String.format("%s: Stock disponible %d, solicitado %d",
                                producto.getNombre(), producto.getStock(), cantidad)));
            }
        }

        if (errores.isEmpty()) {
            return ResponseEntity.ok(Map.of("valido", true, "mensaje", "Stock suficiente"));
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("valido", false, "errores", errores));
        }
    }
}
