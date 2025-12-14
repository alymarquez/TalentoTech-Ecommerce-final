package com.techlab.ecommerce_api.services;

import com.techlab.ecommerce_api.models.Usuario;
import com.techlab.ecommerce_api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario crearObtenerUsuario(String nombre, String email) {
        return usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario nuevoUsuario = new Usuario(nombre, email);
                    return usuarioRepository.save(nuevoUsuario);
                });
    }
}