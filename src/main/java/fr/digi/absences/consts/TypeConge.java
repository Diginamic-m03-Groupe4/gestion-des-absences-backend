package fr.digi.absences.consts;

public enum TypeConge {
    PAYE("PAYE")
    , RTT_EMPLOYE("RTT_EMPLOYE")
    , SANS_SOLDE("SANS_SOLDE");
    private String typeConge;

    TypeConge(String typeConge) {
        this.typeConge= typeConge;
    }

    public String getTypeConge() {
        return typeConge;
    }
}
