package com.Donaciones.Donaciones_service.model;

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
@Table(name = "donacion")
@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class Donaciones {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuario_id;

    @Column(name = "nombre_donante", nullable = false)
    private String nombre_donante;

    @Column(name = "correo_donante", nullable = false)
    private String correo_donante;
    
    @Column(nullable = false)
    private Integer monto;
    
    @Column(name = "fecha_aporte", nullable = false)
    private String fecha_aporte;
    
    @Column(name = "acepta_condiciones", nullable = false)
    private Boolean acepta_condiciones;
    
    @Column(name = "recibir_seguimiento", nullable = false)
    private Boolean recibir_seguimiento;
    
    @Column(name = "foto_comprobante", nullable = true)
    private String foto_comprobante;
    
    @Column(nullable = true)
    private String region;
    
    @Column(nullable = true)
    private String comuna;
    
    @Column(nullable = false)
    private String estado = "PENDIENTE";
    
    @Column(name = "fecha_registro", nullable = false)
    private Long fecha_registro;
    
    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creado_en;

}
