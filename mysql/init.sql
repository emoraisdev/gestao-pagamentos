CREATE DATABASE IF NOT EXISTS msclientes;

USE msclientes;

    -- login.usuario definition

CREATE TABLE `usuario` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `usuario` varchar(255) NOT NULL,
                           `senha` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK5171l57faosmj8myawaucatdw` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO usuario (id,usuario,senha) VALUES
    (1,'adj2','$2a$12$ojb.ngrl83mhT1sur4lkgeukinr1WA4Y9hyOusO5Y/tZ0AUjNlgky');