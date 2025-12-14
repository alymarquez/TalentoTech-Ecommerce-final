package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.models.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Usuario save(Usuario usuario);
    Usuario crearObtenerUsuario(String nombre, String email);
}