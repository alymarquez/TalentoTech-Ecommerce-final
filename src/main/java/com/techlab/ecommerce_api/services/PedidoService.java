package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.exceptions.StockInsuficienteException;
import com.techlab.ecommerce_api.models.Pedido;
import com.techlab.ecommerce_api.models.Usuario;
import java.util.List;
import java.util.Map;

public interface PedidoService {
    Pedido crearPedido(Map<Long, Integer> productosCantidad) throws StockInsuficienteException;

    Pedido crearPedidoConUsuario(Map<Long, Integer> productosCantidad, Usuario usuario)
            throws StockInsuficienteException;

    List<Pedido> findAll();

    Pedido findById(Long id);

    Pedido actualizarEstado(Long id, String nuevoEstado);

    Pedido cancelarPedido(Long id);

    List<Pedido> findByEstado(String estado);

    List<Pedido> findByUsuarioId(Long usuarioId);
}