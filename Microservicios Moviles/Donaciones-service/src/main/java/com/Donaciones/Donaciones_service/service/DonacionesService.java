package com.Donaciones.Donaciones_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Donaciones.Donaciones_service.model.Donaciones;
import com.Donaciones.Donaciones_service.repository.DonacionesRepository;

@Service
public class DonacionesService {

    @Autowired
    private DonacionesRepository donacionesRepository;

    public Donaciones registrar(Donaciones donacion) {
        if (donacion.getEstado() == null || donacion.getEstado().isBlank()) {
            donacion.setEstado("PENDIENTE");
        }
        if (donacion.getFecha_registro() == null) {
            donacion.setFecha_registro(System.currentTimeMillis());
        }
        if (donacion.getCreado_en() == null) {
            donacion.setCreado_en(LocalDateTime.now());
        }
        if (donacion.getAcepta_condiciones() == null) {
            donacion.setAcepta_condiciones(true);
        }
        if (donacion.getRecibir_seguimiento() == null) {
            donacion.setRecibir_seguimiento(false);
        }
        return donacionesRepository.save(donacion);
    }

    public List<Donaciones> listarTodas() {
        return donacionesRepository.findAll();
    }

    public List<Donaciones> listarPorUsuario(Long usuarioId) {
        return donacionesRepository.findByUsuarioId(usuarioId);
    }

    public Donaciones obtenerPorId(Long id) {
        return donacionesRepository.findById(id).orElse(null);
    }

    public Donaciones cambiarEstado(Long id, String nuevoEstado) {
        Optional<Donaciones> opt = donacionesRepository.findById(id);
        if (opt.isPresent()) {
            Donaciones donacion = opt.get();
            donacion.setEstado(nuevoEstado);
            return donacionesRepository.save(donacion);
        }
        return null;
    }
}
