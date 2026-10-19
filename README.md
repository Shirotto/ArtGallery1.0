# ArtGallery

A JavaFX desktop application for managing an art gallery catalogue, backed by MySQL through Hibernate.

## Features
- User registration and login, with hashed passwords
- Catalogue of artworks with images
- User profile page
- Input validation on forms

## Tech stack
Java 21, JavaFX (FXML), Hibernate ORM, MySQL, Maven. A GitHub Actions workflow builds the project with Maven.

## Running it
1. Create the database by importing `src/main/resources/artgallery.sql` into MySQL.
2. Set your database URL, user and password in `src/main/resources/hibernate.cfg.xml`.
3. Start the app:
   ```bash
   ./mvnw javafx:run
   ```

## Team
University team project by [@Shirotto](https://github.com/Shirotto), [@Microchip-Rezet](https://github.com/Microchip-Rezet) and [@Marco-Nico](https://github.com/Marco-Nico).
