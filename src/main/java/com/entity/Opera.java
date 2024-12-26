package com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opere") // Specifica il nome della tabella nel database
public class Opera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementato
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

   /* @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    */

    @Column(name = "descrizione", length = 500)
    private String descrizione;

    @Lob
    @Column(name = "immagine", columnDefinition = "LONGBLOB") // Tipo LONGBLOB, file fino a 4 MB
    private byte[] immagine;

    // Costruttore di default (richiesto da Hibernate)
    public Opera() {
    }

    // Costruttore con parametri
    public Opera(String nome, String descrizione, byte[] immagine) {
        this.nome = nome;
      //  this.user = user;
        this.descrizione = descrizione;
        this.immagine = immagine;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

  /*  public User getUser() {
        return user;
    }

   */

   /* public void setUser(User user) {
        this.user = user;
    }

    */

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    @Override
    public String toString() {
        return "Opera{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
              //  ", user=" + user +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }
}

