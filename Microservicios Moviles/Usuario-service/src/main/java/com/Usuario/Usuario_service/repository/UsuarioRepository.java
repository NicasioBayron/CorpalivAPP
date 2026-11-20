package com.Usuario.Usuario_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Usuario.Usuario_service.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    
}
