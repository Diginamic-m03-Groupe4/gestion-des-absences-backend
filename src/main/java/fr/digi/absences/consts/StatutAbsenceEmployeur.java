package fr.digi.absences.consts;

public enum StatutAbsenceEmployeur {

    INITIALE("INTIALE"), VALIDEE("VALIDEE"), REJETEE("REJETEE");

    private String status;

    StatutAbsenceEmployeur(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
