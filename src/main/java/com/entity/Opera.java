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

    @Column(name = "autore", nullable = false, length = 100)
    private String autore;

    @Column(name = "anno", nullable = true)
    private int anno;

    @Column(name = "tecnica", nullable = true, length = 100)
    private String tecnica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @Column(name = "descrizione", length = 500)
    private String descrizione;

    @Column(name = "dimensione", length = 500)
    private String dimensione;

    @Lob
    @Column(name = "immagine", columnDefinition = "LONGBLOB") // Tipo LONGBLOB, file fino a 4 MB
    private byte[] immagine;

    // Costruttore di default (richiesto da Hibernate)
    public Opera() {
    }

    // Costruttore con parametri
    public Opera(String nome, String autore, int anno, String tecnica, User user, String descrizione, byte[] immagine,String dimensione) {
        this.nome = nome;
        this.autore = autore;
        this.anno = anno;
        this.tecnica = tecnica;
        this.user = user;
        this.descrizione = descrizione;
        this.immagine = immagine;
        this.dimensione = dimensione;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user){ this.user = user;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getTecnica() {
        return tecnica;
    }

    public void setTecnica(String tecnica) {
        this.tecnica = tecnica;
    }

    public String getDimensione() {
        return dimensione;
    }

    public void setDimensione(String dimensione) {
        this.dimensione = dimensione;
    }

  /*  public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

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
                ", autore='" + autore + '\'' +
                ", anno=" + anno +
                ", tecnica='" + tecnica + '\'' +
                ", user=" + user +
                ", descrizione='" + descrizione + '\'' +
                ", dimensione='" + dimensione + '\'' +
                '}';
    }
}
