package com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Specifica il nome della tabella nel database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementato
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // Costruttore di default (richiesto da Hibernate)
    public User() {
    }

    // Costruttore con parametri
    public User(String username, String email, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}


