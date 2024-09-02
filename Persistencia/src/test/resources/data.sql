-- Datos para la entidad Usuario
INSERT INTO usuario (codigo, email, password, cedula, nombre, genero) VALUES
                                                                          (1, "maria@gmail.com", "clave123", "11111111", "Maria Lopez", 'FEMENINO'),
                                                                          (2, "juan@gmail.com", "clave456", "22222222", "Juan Perez", 'MASCULINO'),
                                                                          (3, "anamaria@gmail.com", "clave789", "33333333", "AnaMaria Gomez", 'FEMENINO'),
                                                                          (4, "carlos@gmail.com", "clave321", "44444444", "Carlos Ruiz", 'MASCULINO'),
                                                                          (5, "luis@gmail.com", "clave654", "55555555", "Luis Torres", 'MASCULINO');
-- Datos para la entidad Libro
INSERT INTO libro (isbn, nombre, editorial, anio, disponible, genero) VALUES
                                                              ('9780140449136', 'The Odyssey', 'Penguin Classics', 2003,true, 'LITERATURA'),
                                                              ('9780262033848', 'Introduction to Algorithms', 'MIT Press', 2009,  true, 'INGENIERIA'),
                                                              ('9780553386790', 'A Brief History of Time', 'Bantam Books', 1998, true, 'CIENCIA'),
                                                              ('9780439139595', 'Harry Potter and the Goblet of Fire', 'Scholastic', 2002, true,  'FICCION'),
                                                              ('9781451621709', 'Steve Jobs', 'Simon & Schuster', 2011, true,'HISTORIA');

-- Datos para la entidad Autor
INSERT INTO autor (codigo, nombre, anio) VALUES
                                             (1, 'Homer', -800),
                                             (2, 'Thomas H. Cormen', 2009),
                                             (3, 'Stephen Hawking', 1998),
                                             (4, 'J.K. Rowling', 2002),
                                             (5, 'Walter Isaacson', 2011);

-- Datos para la entidad Reserva
INSERT INTO reserva (codigo, fecha_reserva, fecha_devolucion, anio, genero, usuario_codigo) VALUES
                                                                                                (1, '2024-01-10 10:30:00', '2024-02-10', 2024, 'LITERATURA', 1),
                                                                                                (2, '2024-02-15 11:45:00', '2024-03-15', 2024, 'INGENIERIA', 2),
                                                                                                (3, '2024-03-20 14:20:00', '2024-04-20', 2024, 'CIENCIA', 3),
                                                                                                (4, '2024-04-25 09:15:00', '2024-05-25', 2024, 'FICCION', 4),
                                                                                                (5, '2024-05-30 16:10:00', '2024-06-30', 2024, 'HISTORIA', 5);

-- Datos para la relación Many-to-Many entre Libro y Autor
INSERT INTO libro_autores (libros_isbn, autores_codigo) VALUES
                                                           ('9780140449136', 1),  -- The Odyssey por Homer
                                                           ('9780262033848', 2),  -- Introduction to Algorithms por Thomas H. Cormen
                                                           ('9780553386790', 3),  -- A Brief History of Time por Stephen Hawking
                                                           ('9780439139595', 4),  -- Harry Potter and the Goblet of Fire por J.K. Rowling
                                                           ('9781451621709', 5);  -- Steve Jobs por Walter Isaacson

-- Datos para la relación Many-to-Many entre Reserva y Libro
INSERT INTO reserva_libros (reservas_codigo, libros_isbn) VALUES
                                                            (1, '9780140449136'),
                                                            (2, '9780262033848'),
                                                            (3, '9780553386790'),
                                                            (4, '9780439139595'),
                                                            (5, '9781451621709');
