CREATE TABLE locales(
    idLocal INT AUTO_INCREMENT PRIMARY KEY,
    nombreLocal VARCHAR(50) NOT NULL,
    direccionLocal VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreLocal, direccionLocal, activo) VALUES ('Sucursal Plaza Oeste', 'Av. Americo Vespucio 1501', TRUE);