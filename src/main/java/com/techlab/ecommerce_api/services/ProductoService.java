package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.exceptions.StockInsuficienteException;
import com.techlab.ecommerce_api.models.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();

    Optional<Producto> findById(Long id);

    Producto save(Producto producto);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Producto> buscarPorNombre(String nombre);

    void descontarStock(Long productoId, Integer cantidad) throws StockInsuficienteException;

    Producto actualizarStock(Long productoId, Integer nuevoStock);

    boolean hayStockSuficiente(Long productoId, Integer cantidad);
}