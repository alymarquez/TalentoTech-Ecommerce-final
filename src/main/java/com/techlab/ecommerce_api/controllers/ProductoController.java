package com.techlab.ecommerce_api.controllers;
import com.techlab.ecommerce_api.models.Producto;
import com.techlab.ecommerce_api.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // GET todos los productos
    @GetMapping
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    // GET por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST crear producto
    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // PUT actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto producto) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        producto.setId(id);
        return ResponseEntity.ok(productoRepository.save(producto));
    }

    // DELETE eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}