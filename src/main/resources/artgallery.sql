-- ArtGallery MySQL database file

-- 1. Create the database
CREATE DATABASE IF NOT EXISTS artgallery;

-- 2. Select the database
USE artgallery;

-- 3. Create a user with a password (only if it doesn't already exist)
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'password';

-- 4. Grant all privileges on the artgallery database to the admin user
GRANT ALL PRIVILEGES ON artgallery.* TO 'admin'@'localhost';

-- 5. Apply privilege changes
FLUSH PRIVILEGES;

