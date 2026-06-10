CREATE TABLE cargos(
    idCargo INT AUTO_INCREMENT PRIMARY KEY,
    nombreCargo VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreCargo, activo) VALUES ('Jefe', TRUE);