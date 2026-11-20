package com.Contacto.Contacto_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contacto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = true)
    private Long usuario_id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String correo;

    @Column(name = "tipo_colaboracion", nullable = false)
    private String tipo_colaboracion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false)
    private String estado = "RECIBIDA";

    @Column(name = "respuesta_admin", columnDefinition = "TEXT", nullable = true)
    private String respuesta_admin;

    @Column(name = "fecha_registro", nullable = false)
    private Long fecha_registro;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creado_en;
}
