package com.Usuario.Usuario_service.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Usuario.Usuario_service.model.Usuario;
import com.Usuario.Usuario_service.service.JwtService;
import com.Usuario.Usuario_service.service.UsuarioService;

@RestController 
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired 
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null) email = request.get("correo");

        String password = request.get("password");
        if (password == null) password = request.get("clave");

        String token = usuarioService.login(email, password);

        Map<String, Object> resp = new HashMap<>();
        if (token == null) {
            resp.put("status", "error");
            resp.put("message", "Credenciales incorrectas");
            resp.put("token", "");
        } else {
            resp.put("status", "ok");
            resp.put("token", token);
            Usuario u = usuarioService.obtenerPorEmail(email);
            if (u != null) {
                resp.put("id", u.getId());
                resp.put("nombre", u.getNombre());
                resp.put("email", u.getEmail());
                resp.put("rol", u.getRol());
                resp.put("foto_perfil", u.getFoto_perfil());
            }
        }
        return resp;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String email = request.get("email");
        if (email == null) email = request.get("correo");

        String password = request.get("password");
        if (password == null) password = request.get("clave");

        String resultado = usuarioService.register(nombre, email, password);

        Map<String, String> resp = new HashMap<>();
        resp.put("message", resultado);
        resp.put("status", resultado.contains("exitosamente") ? "ok" : "error");
        return resp;
    }

    @GetMapping
    public ResponseEntity<?> obtenerTodos(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        ResponseEntity<Map<String, String>> authError = validarAdmin(authorization);
        if (authError != null) return authError;

        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        ResponseEntity<Map<String, String>> authError = validarAdmin(authorization);
        if (authError != null) return authError;

        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    private ResponseEntity<Map<String, String>> validarAdmin(String authorization) {
        String email = jwtService.getEmailFromToken(authorization);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Token JWT inválido o ausente"));
        }

        String rol = usuarioService.getRole(email);
        if (rol == null || !rol.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("status", "error", "message", "Se requiere rol ADMIN"));
        }
        return null;
    }
}
