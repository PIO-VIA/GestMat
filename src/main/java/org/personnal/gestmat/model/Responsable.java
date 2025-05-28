package org.personnal.gestmat.model;

public class Responsable extends Enseignant {
    private long  idResposnsable;
    private String nom;
    private String prenom;
    private String password;

    public Responsable(long idResposnsable, String nom, String prenom, String password) {
        super(idResposnsable, nom, prenom);
        this.password=password;
    }

    @Override
    public long getId() {
        return idResposnsable;
    }

    @Override
    public void setId(long id) {
        this.idResposnsable = id;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
