package org.personnal.gestmat.model;

public class Salle {
    private String CodeSalle;
    private String nomSalle;
    private int nbPlacesDispo;
    private boolean dispo;

    public Salle(String codeSalle, String nomSalle, int nbPlacesDispo, boolean dispo) {
        CodeSalle = codeSalle;
        this.nomSalle = nomSalle;
        this.nbPlacesDispo = nbPlacesDispo;
        this.dispo = dispo;
    }

    public String getCodeSalle() {
        return CodeSalle;
    }

    public void setCodeSalle(String codeSalle) {
        CodeSalle = codeSalle;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public int getNbPlacesDispo() {
        return nbPlacesDispo;
    }

    public void setNbPlacesDispo(int nbPlacesDispo) {
        this.nbPlacesDispo = nbPlacesDispo;
    }

    public boolean isDispo() {
        return dispo;
    }

    public void setDispo(boolean dispo) {
        this.dispo = dispo;
    }
}
