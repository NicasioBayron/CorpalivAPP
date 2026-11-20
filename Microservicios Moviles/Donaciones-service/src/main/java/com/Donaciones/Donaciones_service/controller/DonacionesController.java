package com.Donaciones.Donaciones_service.controller;

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

import com.Donaciones.Donaciones_service.model.Donaciones;
import com.Donaciones.Donaciones_service.service.DonacionesService;

@RestController
@RequestMapping("/donaciones")
@CrossOrigin(origins = "*")
public class DonacionesController {

    @Autowired
    private DonacionesService donacionesService;

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Donaciones donacion) {
        try {
            Donaciones nueva = donacionesService.registrar(donacion);
            Map<String, Object> resp = new HashMap<>();
            resp.put("status", "ok");
            resp.put("message", "Donación registrada con éxito");
            resp.put("donacion", nueva);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error al registrar donación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping
    public List<Donaciones> listarTodas() {
        return donacionesService.listarTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Donaciones> listarPorUsuario(@PathVariable Long usuarioId) {
        return donacionesService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Donaciones donacion = donacionesService.obtenerPorId(id);
        if (donacion != null) {
            return ResponseEntity.ok(donacion);
        }
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Donación no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "El campo 'estado' es requerido");
            return ResponseEntity.badRequest().body(error);
        }
        Donaciones actualizada = donacionesService.cambiarEstado(id, nuevoEstado);
        if (actualizada != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("status", "ok");
            resp.put("message", "Estado actualizado exitosamente");
            resp.put("donacion", actualizada);
            return ResponseEntity.ok(resp);
        }
        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Donación no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
