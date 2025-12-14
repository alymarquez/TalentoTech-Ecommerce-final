package com.techlab.ecommerce_api.controllers;

import com.techlab.ecommerce_api.models.dto.ErrorResponseDTO;
import com.techlab.ecommerce_api.models.dto.ProductoRequestDTO;
import com.techlab.ecommerce_api.models.Categoria;
import com.techlab.ecommerce_api.models.Producto;
import com.techlab.ecommerce_api.services.CategoriaService;
import com.techlab.ecommerce_api.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Producto> getAll() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody ProductoRequestDTO request) {
        try {
            if (request.getPrecio() < 0 || request.getStock() < 0) {
                ErrorResponseDTO error = new ErrorResponseDTO("Validación fallida",
                        "Precio y stock no pueden ser negativos");
                return ResponseEntity.badRequest().body(error);
            }

            Categoria categoria;
            List<Categoria> categoriasExistentes = categoriaService.buscarPorNombre(request.getCategoriaNombre());

            if (!categoriasExistentes.isEmpty()) {
                categoria = categoriasExistentes.get(0);
            } else {
                categoria = new Categoria(request.getCategoriaNombre(),
                        "Descripción para " + request.getCategoriaNombre());
                categoria = categoriaService.save(categoria);
            }

            Producto producto = new Producto(
                    request.getNombre(),
                    request.getDescripcion(),
                    request.getPrecio(),
                    categoria,
                    request.getImagenUrl(),
                    request.getStock());

            Producto savedProducto = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);

        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("Error al crear producto", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductoRequestDTO request) {
        try {
            Producto productoExistente = productoService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));

            Categoria categoria;
            List<Categoria> categoriasExistentes = categoriaService.buscarPorNombre(request.getCategoriaNombre());

            if (!categoriasExistentes.isEmpty()) {
                categoria = categoriasExistentes.get(0);
            } else {
                categoria = new Categoria(request.getCategoriaNombre(),
                        "Descripción para " + request.getCategoriaNombre());
                categoria = categoriaService.save(categoria);
            }

            productoExistente.setNombre(request.getNombre());
            productoExistente.setDescripcion(request.getDescripcion());
            productoExistente.setPrecio(request.getPrecio());
            productoExistente.setCategoria(categoria);
            productoExistente.setImagenUrl(request.getImagenUrl());
            productoExistente.setStock(request.getStock());

            Producto actualizado = productoService.save(productoExistente);
            return ResponseEntity.ok(actualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            ErrorResponseDTO error = new ErrorResponseDTO("Error al actualizar producto", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Producto producto = productoService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

            productoService.deleteById(id);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Producto eliminado exitosamente",
                    "id", id));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}