package com.Contacto.Contacto_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Contacto.Contacto_service.model.Contacto;
import com.Contacto.Contacto_service.repository.ContactoRepository;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public Contacto registrar(Contacto contacto) {
        if (contacto.getEstado() == null || contacto.getEstado().isBlank()) {
            contacto.setEstado("RECIBIDA");
        }
        if (contacto.getFecha_registro() == null) {
            contacto.setFecha_registro(System.currentTimeMillis());
        }
        if (contacto.getCreado_en() == null) {
            contacto.setCreado_en(LocalDateTime.now());
        }
        return contactoRepository.save(contacto);
    }

    public List<Contacto> listarTodos() {
        return contactoRepository.findAll();
    }

    public List<Contacto> listarPorUsuario(Long usuarioId) {
        return contactoRepository.findByUsuarioId(usuarioId);
    }

    public Contacto obtenerPorId(Long id) {
        return contactoRepository.findById(id).orElse(null);
    }

    public Contacto responder(Long id, String respuestaAdmin, String nuevoEstado) {
        Optional<Contacto> opt = contactoRepository.findById(id);
        if (opt.isPresent()) {
            Contacto contacto = opt.get();
            contacto.setRespuesta_admin(respuestaAdmin);
            if (nuevoEstado != null && !nuevoEstado.isBlank()) {
                contacto.setEstado(nuevoEstado);
            } else {
                contacto.setEstado("RESPONDIDA");
            }
            return contactoRepository.save(contacto);
        }
        return null;
    }
}
