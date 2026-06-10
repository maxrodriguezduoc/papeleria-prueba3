CREATE TABLE comunas(
    idComuna INT AUTO_INCREMENT PRIMARY KEY,
    nombreComuna VARCHAR(50) NOT NULL,
    codigoPostal NUMBER(7),
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreComuna, codigoPostal, activo) VALUES ('Cerrillos', 8320000, TRUE);