package org.personnal.gestmat.model;

import java.time.LocalDate;

public class Reservation {
    private String idReservation ;
    private long  idEnseignant;
    private long CodeMateriel ;
    private String CodeSalle;
    private LocalDate dateReservation;
    private LocalDate dateFinReservation;

    public Reservation(String idReservation, long idEnseignant, String codeSalle, long codeMateriel, LocalDate dateReservation, LocalDate dateFinReservation) {
        this.idReservation = idReservation;
        this.idEnseignant = idEnseignant;
        CodeSalle = codeSalle;
        CodeMateriel = codeMateriel;
        this.dateReservation = dateReservation;
        this.dateFinReservation = dateFinReservation;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public long getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(long idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public long getCodeMateriel() {
        return CodeMateriel;
    }

    public void setCodeMateriel(long codeMateriel) {
        CodeMateriel = codeMateriel;
    }

    public String getCodeSalle() {
        return CodeSalle;
    }

    public void setCodeSalle(String codeSalle) {
        CodeSalle = codeSalle;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalDate getDateFinReservation() {
        return dateFinReservation;
    }

    public void setDateFinReservation(LocalDate dateFinReservation) {
        this.dateFinReservation = dateFinReservation;
    }
}
