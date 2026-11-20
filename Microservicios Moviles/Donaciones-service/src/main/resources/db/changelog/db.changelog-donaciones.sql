--liquibase formatted sql
--changeset eileen:1
create table donacion (
    id bigint auto_increment primary key,
    usuario_id bigint not null,
    nombre_donante varchar(150) not null,
    correo_donante varchar(150) not null,
    monto int not null,
    fecha_aporte varchar(20) not null,
    acepta_condiciones boolean not null default true,
    recibir_seguimiento boolean not null default false,
    foto_comprobante varchar(500) null,
    region varchar(100) null,
    comuna varchar(100) null,
    estado varchar(30) not null default 'PENDIENTE',
    fecha_registro bigint not null,
    creado_en timestamp default current_timestamp
);
--changeset eileen:2
create index idx_donacion_usuario on donacion(usuario_id);
create index idx_donacion_estado on donacion(estado);