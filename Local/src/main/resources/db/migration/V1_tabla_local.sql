-- ===================== CARGOS =====================
CREATE TABLE cargos (
    idCargo INT AUTO_INCREMENT PRIMARY KEY,
    nombreCargo VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

INSERT INTO cargos (nombreCargo, activo) VALUES ('Jefe', TRUE);


CREATE TABLE colaborador (
    idColaborador INT AUTO_INCREMENT PRIMARY KEY,
    nombreColaborador VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

INSERT INTO colaborador (nombreColaborador, activo)
VALUES ('Jim Halpert', TRUE);


CREATE TABLE locales (
    idLocal INT AUTO_INCREMENT PRIMARY KEY,
    nombreLocal VARCHAR(50) NOT NULL,
    direccionLocal VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

INSERT INTO locales (nombreLocal, direccionLocal, activo)
VALUES ('Sucursal Plaza Oeste', 'Av. Americo Vespucio 1501', TRUE);


CREATE TABLE regiones (
    idRegion INT AUTO_INCREMENT PRIMARY KEY,
    nombreRegion VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

INSERT INTO regiones (nombreRegion, activo)
VALUES ('Región Metropolitana', TRUE);


CREATE TABLE comunas (
    idComuna INT AUTO_INCREMENT PRIMARY KEY,
    nombreComuna VARCHAR(50) NOT NULL,
    codigoPostal INT,
    activo BOOLEAN DEFAULT TRUE
);

INSERT INTO comunas (nombreComuna, codigoPostal, activo)
VALUES ('Cerrillos', 8320000, TRUE);

CREATE TABLE colaboradores (
    idColaboradores INT AUTO_INCREMENT PRIMARY KEY,
    idLocal INT NOT NULL,
    idColaborador INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);