CREATE TABLE cargos(
    idCargo INT AUTO_INCREMENT PRIMARY KEY,
    nombreCargo VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreCargo, activo) VALUES ('Jefe', TRUE);

CREATE TABLE colaborador(
    idColaborador INT AUTO_INCREMENT PRIMARY KEY,
    nombreColaborador VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreCargo, activo) VALUES ('Jim Hallpert', TRUE);

CREATE TABLE colabores(
    idColaboradores INT AUTO_INCREMENT PRIMARY KEY,
    idLocal INT NOT NULL,
    idColaborador INT NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO colaboradores (idColaboradores, idLocal, idColaborador, activo);

CREATE TABLE comunas(
    idComuna INT AUTO_INCREMENT PRIMARY KEY,
    nombreComuna VARCHAR(50) NOT NULL,
    codigoPostal NUMBER(7) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreComuna, codigoPostal, activo) VALUES ('Cerrillos', 8320000, TRUE);

CREATE TABLE locales(
    idLocal INT AUTO_INCREMENT PRIMARY KEY,
    nombreLocal VARCHAR(50) NOT NULL,
    direccionLocal VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreLocal, direccionLocal, activo) VALUES ('Sucursal Plaza Oeste', 'Av. Americo Vespucio 1501', TRUE);

CREATE TABLE regiones(
    idRegion INT AUTO_INCREMENT PRIMARY KEY,
    nombreRegion VARCHAR(50) NOT NULL,
    activo BOOLEAN TRUE
);

INSERT INTO cargos (nombreRegion, activo) VALUES ('Region Metropolitana',TRUE);