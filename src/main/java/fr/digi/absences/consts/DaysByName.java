package fr.digi.absences.consts;

public enum DaysByName {
    LUNDI("lundi", "LUNDI", "Lu"),
    MARDI("mardi", "MARDI", "Mar"),
    MERCREDI("mercredi", "MERCREDI", "Mer"),
    JEUDI("jeudi", "JEUDI", "Jeu"),
    VENDREDI("vendredi", "VENDREDI", "Ven"),
    SAMEDI("samedi", "SAMEDI", "Sam"),
    DIMANCHE("dimanche", "DIMANCHE", "Dim");

    String dayByNameMin;
    String dayByNameMaj;
    String dayByNameAbbrv;

    DaysByName(String dayByNameMin, String dayByNameMaj, String dayByNameAbbrv) {
        this.dayByNameMin = dayByNameMin;
        this.dayByNameMaj = dayByNameMaj;
        this.dayByNameAbbrv = dayByNameAbbrv;
    }

    public String getDayByNameMin() {
        return dayByNameMin;
    }

    public String getDayByNameMaj() {
        return dayByNameMaj;
    }

    public String getDayByNameAbbrv() {
        return dayByNameAbbrv;
    }
}
