package com.Donaciones.Donaciones_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Donaciones.Donaciones_service.model.Donaciones;

@Repository 
public interface DonacionesRepository extends JpaRepository<Donaciones, Long> {

    @Query("SELECT d FROM Donaciones d WHERE d.usuario_id = :usuarioId ORDER BY d.id DESC")
    List<Donaciones> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    List<Donaciones> findByEstado(String estado);
}
