package org.personnal.gestmat.model;

public class Materiel {
    private long CodeMateriel ;
    private boolean disponible;

    public Materiel(long codeMateriel, boolean disponible) {
        CodeMateriel = codeMateriel;
        this.disponible = disponible;
    }

    public long getCodeMateriel() {
        return CodeMateriel;
    }

    public void setCodeMateriel(long codeMateriel) {
        CodeMateriel = codeMateriel;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
