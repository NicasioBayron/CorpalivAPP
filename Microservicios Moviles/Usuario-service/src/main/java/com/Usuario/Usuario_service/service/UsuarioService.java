package com.Usuario.Usuario_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Usuario.Usuario_service.model.Usuario;
import com.Usuario.Usuario_service.repository.UsuarioRepository;

@Service 
public class UsuarioService {
    @Autowired 
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private HashService hashService;

    @Autowired 
    private JwtService jwtService;

    public String login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) return null;
        // compare SHA-1 hashes
        String hashedInput = hashService.sha1(password);
        if (!hashedInput.equalsIgnoreCase(usuario.getPassword())) return null;

        return jwtService.generateToken(email);
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public String getRole(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        return usuario != null ? usuario.getRol() : null;
    }

    public String register(String nombre, String email, String password) {
        Usuario existing = usuarioRepository.findByEmail(email);
        if (existing != null) {
            return "Usuario ya existe!";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre != null && !nombre.isBlank() ? nombre : email.split("@")[0]);
        usuario.setEmail(email);
        // store SHA-1 hash of the password
        usuario.setPassword(hashService.sha1(password));
        usuario.setRol("DONANTE");
        usuario.setActivo(true);
        usuario.setCreado_en(LocalDateTime.now());

        usuarioRepository.save(usuario);

        return "Usuario creado exitosamente!";
    }

    public String register(String email, String password) {
        return register(null, email, password);
    }
}
