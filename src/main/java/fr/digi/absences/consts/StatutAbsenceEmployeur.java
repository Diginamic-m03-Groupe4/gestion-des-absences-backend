package fr.digi.absences.consts;

public enum StatutAbsenceEmployeur {

    INITIALE("INTIALE"), VALIDEE("VALIDEE");

    private String status;

    StatutAbsenceEmployeur(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
