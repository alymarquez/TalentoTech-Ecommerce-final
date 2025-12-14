package com.techlab.ecommerce_api.controllers;

import com.techlab.ecommerce_api.models.Usuario;
import com.techlab.ecommerce_api.services.UsuarioService;
import com.techlab.ecommerce_api.models.dto.PedidoRequestDTO;
import com.techlab.ecommerce_api.models.LineaPedido;
import com.techlab.ecommerce_api.models.Pedido;
import com.techlab.ecommerce_api.models.dto.ErrorResponseDTO;
import com.techlab.ecommerce_api.models.dto.PedidoResponseDTO;
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

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequestDTO request) {
        try {
            if (request.getUsuarioEmail() == null || request.getUsuarioEmail().trim().isEmpty()) {
                ErrorResponseDTO error = new ErrorResponseDTO("Error", "Email de usuario requerido");
                return ResponseEntity.badRequest().body(error);
            }

            if (request.getProductos() == null || request.getProductos().isEmpty()) {
                ErrorResponseDTO error = new ErrorResponseDTO("Error", "Debe especificar productos");
                return ResponseEntity.badRequest().body(error);
            }

            String nombreUsuario = request.getUsuarioNombre() != null ? request.getUsuarioNombre() : "Cliente";

            Usuario usuario = usuarioService.crearObtenerUsuario(
                    nombreUsuario,
                    request.getUsuarioEmail());

            Map<Long, Integer> productosMap = request.getProductos();

            Pedido pedido = pedidoService.crearPedidoConUsuario(productosMap, usuario);

            PedidoResponseDTO response = new PedidoResponseDTO(
                    pedido.getId(),
                    pedido.getFechaCreacion(),
                    pedido.getEstado(),
                    pedido.getTotal(),
                    "Pedido creado exitosamente");

            Map<String, Object> datosExtra = new HashMap<>();
            datosExtra.put("usuarioId", usuario.getId());
            datosExtra.put("usuarioNombre", usuario.getNombre());
            datosExtra.put("usuarioEmail", usuario.getEmail());
            response.setDatosExtra(datosExtra);

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
            response.setProductos(productosInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("Error al crear pedido", e.getMessage());
            return ResponseEntity.badRequest().body(error);
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

    @GetMapping("/usuario/{email}")
public ResponseEntity<?> getPedidosPorUsuario(@PathVariable String email) {
    try {
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        List<Pedido> pedidos = pedidoService.findByUsuarioId(usuario.getId());
        
        return ResponseEntity.ok(pedidos);
        
    } catch (IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }
}
}