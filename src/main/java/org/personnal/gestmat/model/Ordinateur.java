package org.personnal.gestmat.model;

public class Ordinateur extends Materiel{
    private long CodeMateriel ;
    private boolean disponible;
    private String marque;
    private int capacite;
    private String Ecran;

    public Ordinateur(long codeMateriel, boolean disponible, String marque, int capacite, String ecran) {
        super(codeMateriel, disponible);
        this.marque = marque;
        this.capacite = capacite;
        this.Ecran = ecran;
    }

    @Override
    public long getCodeMateriel() {
        return CodeMateriel;
    }

    @Override
    public void setCodeMateriel(long codeMateriel) {
        CodeMateriel = codeMateriel;
    }

    @Override
    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getEcran() {
        return Ecran;
    }

    public void setEcran(String ecran) {
        Ecran = ecran;
    }
}
