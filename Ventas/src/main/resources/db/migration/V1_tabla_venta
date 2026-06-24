CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL CHECK (cantidad >= 1),
    total_venta INT NOT NULL CHECK (total_venta >= 0),
    fecha_venta DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    id_producto INT NOT NULL,
    
    -- Clave foránea hacia la tabla productos (ajusta el nombre según tu modelo real)
    CONSTRAINT fk_ventas_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);
CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    
    id_venta INT NOT NULL,
    forma_pago VARCHAR(30) NOT NULL,
    
    id_tarjeta INT NULL,
    id_transferencia INT NULL,
    
    monto INT NOT NULL CHECK (monto > 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Relaciones
    CONSTRAINT fk_pagos_venta FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    CONSTRAINT fk_pagos_tarjeta FOREIGN KEY (id_tarjeta) REFERENCES tarjetas(id_tarjeta),
    CONSTRAINT fk_pagos_transferencia FOREIGN KEY (id_transferencia) REFERENCES transferencias(id_transferencia)
);
CREATE TABLE tarjetas (
    id_tarjeta INT AUTO_INCREMENT PRIMARY KEY,
    
    es_debito BOOLEAN NOT NULL, -- true = débito, false = crédito
    
    numero_tarjeta VARCHAR(19) NOT NULL,
    nombre_banco VARCHAR(50) NOT NULL,
    nombre_titular VARCHAR(100) NOT NULL,
    fecha_expiracion VARCHAR(5) NOT NULL, -- formato MM/YY
    cvv CHAR(3) NOT NULL,
    
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Restricciones adicionales según validaciones
    CONSTRAINT chk_numero_tarjeta CHECK (CHAR_LENGTH(numero_tarjeta) BETWEEN 13 AND 19),
    CONSTRAINT chk_nombre_banco CHECK (CHAR_LENGTH(nombre_banco) BETWEEN 3 AND 50),
    CONSTRAINT chk_nombre_titular CHECK (CHAR_LENGTH(nombre_titular) BETWEEN 3 AND 100),
    CONSTRAINT chk_cvv CHECK (CHAR_LENGTH(cvv) = 3)
);
CREATE TABLE transferencias (
    id_transferencia INT AUTO_INCREMENT PRIMARY KEY,
    
    banco_origen VARCHAR(100) NOT NULL,
    numero_cuenta_origen VARCHAR(20) NOT NULL,
    
    banco_destino VARCHAR(100) NOT NULL,
    numero_cuenta_destino VARCHAR(20) NOT NULL,
    
    monto INT NOT NULL CHECK (monto > 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    
    -- Restricciones adicionales según validaciones
    CONSTRAINT chk_banco_origen CHECK (CHAR_LENGTH(banco_origen) BETWEEN 3 AND 100),
    CONSTRAINT chk_numero_cuenta_origen CHECK (CHAR_LENGTH(numero_cuenta_origen) BETWEEN 10 AND 20),
    CONSTRAINT chk_banco_destino CHECK (CHAR_LENGTH(banco_destino) BETWEEN 3 AND 100),
    CONSTRAINT chk_numero_cuenta_destino CHECK (CHAR_LENGTH(numero_cuenta_destino) BETWEEN 10 AND 20)
);
