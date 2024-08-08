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

INSERT INTO usuario (1,usuario,senha) VALUES
    ('admin','$2a$10$q4Yoxg20mSG7CO3DKbKzlO1NcPWpR9d2uK00gizx4SgGkwGdGRQTO');