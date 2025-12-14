package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.exceptions.StockInsuficienteException;
import com.techlab.ecommerce_api.models.Producto;
import com.techlab.ecommerce_api.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }
    
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }
    
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public void descontarStock(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                producto.getNombre(), 
                producto.getStock(), 
                cantidad
            );
        }
        
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }
    
    public Producto actualizarStock(Long productoId, Integer nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }
    
    public boolean hayStockSuficiente(Long productoId, Integer cantidad) {
        return productoRepository.findById(productoId)
                .map(p -> p.getStock() >= cantidad)
                .orElse(false);
    }
}