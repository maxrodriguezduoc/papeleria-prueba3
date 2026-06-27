CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(150) NOT NULL,
    precio_producto INT NOT NULL,
    stock INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);
CREATE TABLE productos (
    id_productos INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_detalles_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    CONSTRAINT chk_precio_positivo CHECK (precio_unitario > 0)
);

CREATE TABLE tipo_producto (
    id_tipo_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio_producto INT NOT NULL,
    stock INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE tipos_productos (
    id_tipos_productos INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_tipo_producto INT NOT NULL,
    CONSTRAINT fk_tipoproductos_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto) ON DELETE CASCADE,
    CONSTRAINT fk_tipoproductos_tipo FOREIGN KEY (id_tipo_producto) REFERENCES tipo_producto(id_tipo_producto) ON DELETE CASCADE
);

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE categorias (
    id_categorias INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_categoria INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT fk_categorias_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto) ON DELETE CASCADE,
    CONSTRAINT fk_categorias_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE CASCADE
);

CREATE TABLE marca (
    id_marcas INT AUTO_INCREMENT PRIMARY KEY,
    nombre_marca VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE marcas (
    id_marcas INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_marca INT NOT NULL,
    CONSTRAINT fk_marcas_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto) ON DELETE CASCADE,
    CONSTRAINT fk_marcas_marca FOREIGN KEY (id_marca) REFERENCES marca(id_marcas) ON DELETE CASCADE
);

CREATE TABLE marcas (
    id_marcas INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_marca INT NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_marca) REFERENCES marca(id_marcas)
);

CREATE TABLE color (
    id_color INT AUTO_INCREMENT PRIMARY KEY,
    nombre_color VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE colores (
    id_colores INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_color INT NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES producto(id_producto),
    FOREIGN KEY (id_color) REFERENCES color(id_color)
);