package fr.digi.absences.entity;

public enum TypeConge {
    PAYE("PAYE")
    , RTT_EMPLOYE("RTT_EMPLOYE")
    , RTT_EMPLOYEUR("RTT_EMPLOYEUR")
    , SANS_SOLDE("SANS_SOLDE")
    , FERIE("FERIE");
    private String typeConge;

    TypeConge(String typeConge) {
        this.typeConge= typeConge;
    }

    public String getTypeConge() {
        return typeConge;
    }
}
