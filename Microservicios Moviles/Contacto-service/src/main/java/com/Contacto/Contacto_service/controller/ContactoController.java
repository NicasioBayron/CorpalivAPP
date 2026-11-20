package com.Contacto.Contacto_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Contacto.Contacto_service.model.Contacto;
import com.Contacto.Contacto_service.service.ContactoService;

@RestController
@RequestMapping("/contacto")
@CrossOrigin(origins = "*")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Contacto contacto) {
        try {
            Contacto guardado = contactoService.registrar(contacto);
            Map<String, Object> resp = new HashMap<>();
            resp.put("status", "ok");
            resp.put("message", "Solicitud de contacto enviada con éxito");
            resp.put("contacto", guardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error al guardar contacto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping
    public List<Contacto> listarTodos() {
        return contactoService.listarTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Contacto> listarPorUsuario(@PathVariable Long usuarioId) {
        return contactoService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Contacto contacto = contactoService.obtenerPorId(id);
        if (contacto != null) {
            return ResponseEntity.ok(contacto);
        }
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Solicitud de contacto no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PutMapping("/{id}/responder")
    public ResponseEntity<?> responder(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String respuestaAdmin = body.get("respuesta_admin");
        String nuevoEstado = body.get("estado");
        Contacto actualizado = contactoService.responder(id, respuestaAdmin, nuevoEstado);
        if (actualizado != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("status", "ok");
            resp.put("message", "Solicitud respondida exitosamente");
            resp.put("contacto", actualizado);
            return ResponseEntity.ok(resp);
        }
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Solicitud de contacto no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
