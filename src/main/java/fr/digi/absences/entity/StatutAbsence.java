package fr.digi.absences.entity;

public enum StatutAbsence {

    INITIALE("INITIALE")
    , ATTENTE_VALIDATION("ATTENTE_VALIDATION")
    , VALIDEE("VALIDEE")
    , REJETEE("REJETEE");

    String status;

    StatutAbsence(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

}
