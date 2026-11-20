--liquibase formatted sql
--changeset Bayron:1
create table usuario (
    id bigint auto_increment primary key,
    nombre varchar(150) not null,
    correo varchar(150) not null unique,
    password varchar(255) not null,
    rol varchar(30) not null default 'DONANTE',
    foto_perfil varchar(500) null,
    activo boolean not null default true,
    creado_en timestamp default current_timestamp
);
--changeset Bayron:2
insert into usuario (nombre, correo, password, rol) values 
('Carlos Abarzua', 'carlos.abarzua@example.com', 'Nose123', 'DONANTE'),
('Maria Lopez', 'maria.lopez@admin.com', 'Nose123', 'ADMIN');
--changeset Bayron:3
update usuario set password = SHA1(password);