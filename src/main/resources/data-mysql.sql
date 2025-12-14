-- Insertar categorías
INSERT IGNORE INTO categorias (nombre, descripcion) VALUES
('Electrónica', 'Productos electrónicos y dispositivos'),
('Ropa', 'Ropa y accesorios'),
('Libros', 'Libros y material de lectura'),
('Bebidas', 'Bebidas y líquidos'),
('Deportes', 'Artículos deportivos'),
('Accesorios', 'Accesorios varios');

-- Insertar productos
INSERT IGNORE INTO productos (nombre, descripcion, precio, categoria_id, imagen_url, stock) VALUES
('Café Premium', 'Café de alta calidad tostado artesanalmente', 5.99,
    (SELECT id FROM categorias WHERE nombre = 'Bebidas'),
    'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085', 50),
('Té Verde Orgánico', 'Té verde de hojas sueltas importado de Japón', 3.50,
    (SELECT id FROM categorias WHERE nombre = 'Bebidas'),
    'https://images.unsplash.com/photo-1564890369478-c89ca6d9cde9', 80),
('Laptop Gaming', 'Laptop para gaming con RTX 4060, 16GB RAM', 1299.99,
    (SELECT id FROM categorias WHERE nombre = 'Electrónica'),
    'https://images.unsplash.com/photo-1603302576837-37561b2e2302', 15),
('Mouse Inalámbrico', 'Mouse ergonómico inalámbrico con 6 botones', 29.99,
    (SELECT id FROM categorias WHERE nombre = 'Electrónica'),
    'https://images.unsplash.com/photo-1527814050087-3793815479db', 100),
('Libro: Clean Code', 'Libro de programación de Robert C. Martin', 45.50,
    (SELECT id FROM categorias WHERE nombre = 'Libros'),
    'https://m.media-amazon.com/images/I/41xShlnTZTL._SX376_BO1,204,203,200_.jpg', 30),
('Camiseta de Algodón', 'Camiseta 100% algodón premium, talla M', 19.99,
    (SELECT id FROM categorias WHERE nombre = 'Ropa'),
    'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab', 200);

-- Insertar usuarios
INSERT IGNORE INTO usuarios (nombre, email) VALUES
('Juan Pérez', 'juan@example.com'),
('María García', 'maria@example.com'),
('Carlos López', 'carlos@example.com');