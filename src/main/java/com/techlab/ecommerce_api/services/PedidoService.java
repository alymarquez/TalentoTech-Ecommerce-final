package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.exceptions.StockInsuficienteException;
import com.techlab.ecommerce_api.models.LineaPedido;
import com.techlab.ecommerce_api.models.Pedido;
import com.techlab.ecommerce_api.models.Producto;
import com.techlab.ecommerce_api.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoService productoService;
    
    public Pedido crearPedido(Map<Long, Integer> productosCantidad) {
        if (productosCantidad == null || productosCantidad.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto");
        }
        
        validarStockDisponible(productosCantidad);
        
        Pedido pedido = new Pedido();
        
        for (Map.Entry<Long, Integer> entry : productosCantidad.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();
            
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            
            Producto producto = productoService.findById(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));
            
            productoService.descontarStock(productoId, cantidad);
            
            LineaPedido linea = new LineaPedido(producto, cantidad);
            pedido.agregarLinea(linea);
        }
        
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }
    
    private void validarStockDisponible(Map<Long, Integer> productosCantidad) {
        List<String> errores = new ArrayList<>();
        
        for (Map.Entry<Long, Integer> entry : productosCantidad.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();
            
            if (!productoService.hayStockSuficiente(productoId, cantidad)) {
                productoService.findById(productoId).ifPresent(p -> 
                    errores.add(String.format("%s: Stock disponible %d, solicitado %d", 
                        p.getNombre(), p.getStock(), cantidad))
                );
            }
        }
        
        if (!errores.isEmpty()) {
            throw new StockInsuficienteException("Stock insuficiente: " + String.join(", ", errores));
        }
    }
    
    public List<Pedido> findAll() {
        return pedidoRepository.findAllOrderByFechaDesc();
    }
    
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    }
    
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = findById(id);
        
        if (!esTransicionValida(pedido.getEstado(), nuevoEstado)) {
            throw new IllegalArgumentException("Transición de estado no válida: " + 
                    pedido.getEstado() + " -> " + nuevoEstado);
        }
        
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
    
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = findById(id);
        
        if (!pedido.getEstado().equals("PENDIENTE")) {
            throw new IllegalArgumentException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }
        
        for (LineaPedido linea : pedido.getLineasPedido()) {
            Producto producto = linea.getProducto();
            producto.setStock(producto.getStock() + linea.getCantidad());
            productoService.save(producto);
        }
        
        pedido.setEstado("CANCELADO");
        return pedidoRepository.save(pedido);
    }
    
    private boolean esTransicionValida(String estadoActual, String nuevoEstado) {
        List<String> estadosValidos = List.of("PENDIENTE", "CONFIRMADO", "ENVIADO", "ENTREGADO", "CANCELADO");
        
        if (!estadosValidos.contains(nuevoEstado)) {
            return false;
        }
        
        Map<String, List<String>> transicionesPermitidas = Map.of(
            "PENDIENTE", List.of("CONFIRMADO", "CANCELADO"),
            "CONFIRMADO", List.of("ENVIADO", "CANCELADO"),
            "ENVIADO", List.of("ENTREGADO"),
            "ENTREGADO", List.of(),
            "CANCELADO", List.of()
        );
        
        return transicionesPermitidas.getOrDefault(estadoActual, List.of())
                .contains(nuevoEstado);
    }
    
    public List<Pedido> findByEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
}