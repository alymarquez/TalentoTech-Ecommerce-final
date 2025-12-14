package com.techlab.ecommerce_api.controllers;

import com.techlab.ecommerce_api.models.LineaPedido;
import com.techlab.ecommerce_api.models.Pedido;
import com.techlab.ecommerce_api.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin("*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Integer> productosCantidad = (Map<String, Integer>) request.get("productos");

            if (productosCantidad == null || productosCantidad.isEmpty()) {
                return ResponseEntity.badRequest().body("Debe especificar productos para el pedido");
            }

            Map<Long, Integer> productosMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : productosCantidad.entrySet()) {
                try {
                    Long productoId = Long.parseLong(entry.getKey());
                    productosMap.put(productoId, entry.getValue());
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("ID de producto inválido: " + entry.getKey());
                }
            }

            Pedido pedido = pedidoService.crearPedido(productosMap);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", pedido.getId());
            response.put("fechaCreacion", pedido.getFechaCreacion().toString()); // Convertir a String
            response.put("estado", pedido.getEstado());
            response.put("total", pedido.getTotal());
            response.put("mensaje", "Pedido creado exitosamente");

            List<Map<String, Object>> productosInfo = new ArrayList<>();
            if (pedido.getLineasPedido() != null) {
                for (LineaPedido linea : pedido.getLineasPedido()) {
                    Map<String, Object> productoInfo = new HashMap<>();
                    productoInfo.put("productoId", linea.getProducto().getId());
                    productoInfo.put("nombre", linea.getProducto().getNombre());
                    productoInfo.put("cantidad", linea.getCantidad());
                    productoInfo.put("subtotal", linea.getSubtotal());
                    productosInfo.add(productoInfo);
                }
            }
            response.put("productos", productosInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("mensaje", "Error al crear pedido");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        List<Pedido> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.findById(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        try {
            String nuevoEstado = request.get("estado");
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Debe especificar un estado");
            }

            Pedido pedido = pedidoService.actualizarEstado(id, nuevoEstado.toUpperCase());
            return ResponseEntity.ok(pedido);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> getPedidosPorEstado(@PathVariable String estado) {
        List<Pedido> pedidos = pedidoService.findByEstado(estado.toUpperCase());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Pedido>> getHistorial() {
        List<Pedido> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }
}