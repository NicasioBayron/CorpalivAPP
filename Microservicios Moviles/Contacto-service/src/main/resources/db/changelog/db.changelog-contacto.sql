--liquibase formatted sql
--changeset nose:1
create table contacto(
    id bigint auto_increment primary key,
    usuario_id bigint null,
    nombre varchar(150) not null,
    correo varchar(150) not null,
    tipo_colaboracion varchar(100) not null,
    mensaje text not null,
    estado varchar(30) not null default 'RECIBIDA',
    respuesta_admin text null,
    fecha_registro bigint not null,
    creado_en timestamp default current_timestamp
);
--changeset nose:2
create index idx_contacto_usuario on contacto(usuario_id);
create index idx_contacto_estado on contacto(estado);