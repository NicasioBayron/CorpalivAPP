package com.Contacto.Contacto_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Contacto.Contacto_service.model.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    @Query("SELECT c FROM Contacto c WHERE c.usuario_id = :usuarioId ORDER BY c.id DESC")
    List<Contacto> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    List<Contacto> findByCorreo(String correo);

    List<Contacto> findByEstado(String estado);
}
