CREATE TABLE regiones(
    idRegion INT AUTO_INCREMENT PRIMARY KEY,
    nombreRegion VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreRegion, activo) VALUES ('Region Metropolitana',TRUE);