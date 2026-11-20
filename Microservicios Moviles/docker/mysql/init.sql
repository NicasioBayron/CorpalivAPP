CREATE DATABASE IF NOT EXISTS corpaliv_donaciones_db;
CREATE DATABASE IF NOT EXISTS corpaliv_contacto_db;

GRANT ALL PRIVILEGES ON corpaliv_donaciones_db.* TO 'corpaliv_app'@'%';
GRANT ALL PRIVILEGES ON corpaliv_contacto_db.* TO 'corpaliv_app'@'%';
