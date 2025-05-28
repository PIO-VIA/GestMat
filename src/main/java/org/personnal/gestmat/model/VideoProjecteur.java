package org.personnal.gestmat.model;

public class VideoProjecteur extends Materiel{
    private long CodeMateriel ;
    private boolean disponible;
    private String marque;
    private String resolution;

    public VideoProjecteur(long codeMateriel, boolean disponible, String marque, String resolution) {
        super(codeMateriel, disponible);
        this.marque = marque;
        this.resolution = resolution;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
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
}
