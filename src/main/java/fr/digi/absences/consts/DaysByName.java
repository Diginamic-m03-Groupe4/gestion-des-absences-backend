package fr.digi.absences.consts;

public enum DaysByName {
    LUNDI("Lundi", "LUNDI", "Lu"),
    MARDI("Mardi", "MARDI", "Mar"),
    MERCREDI("Mercredi", "MERCREDI", "Mer"),
    JEUDI("Jeudi", "JEUDI", "Jeu"),
    VENDREDI("Vendredi", "VENDREDI", "Ven"),
    SAMEDI("Samedi", "SAMEDI", "Sam"),
    DIMANCHE("Dimanche", "DIMANCHE", "Dim");

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
