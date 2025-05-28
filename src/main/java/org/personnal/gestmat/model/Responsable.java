package org.personnal.gestmat.model;

public class Responsable extends Enseignant {
    private long  idResposnsable;
    private String nom;
    private String prenom;

    public Responsable(long idResposnsable, String nom, String prenom) {
        super(idResposnsable, nom, prenom);
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
}
