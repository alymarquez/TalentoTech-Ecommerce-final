package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.models.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();
    Optional<Categoria> findById(Long id);
    
    List<Categoria> buscarPorNombre(String nombre);
    
    Categoria save(Categoria categoria);
    void deleteById(Long id);
    List<Categoria> buscarPorNombreConteniendo(String nombre);
}