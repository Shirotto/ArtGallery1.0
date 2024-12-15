-- ArtGallery MySQL file del database

-- 1. Crea il database
CREATE DATABASE IF NOT EXISTS artgallery;

-- 2. Seleziona il database
USE artgallery;

-- 3. Crea l'utente admin (Sempre che non esista già)
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'password';

-- 4. Dai tutti i privilegi all'utente admin nel database artgallery
GRANT ALL PRIVILEGES ON artgallery.* TO 'admin'@'localhost';

-- 5. Applica i privilegi
FLUSH PRIVILEGES;

