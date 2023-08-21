package fr.digi.absences.consts;

public enum EnumFeries {

    JOUR_DE_L_AN("Le jour de l'an", "1er jour de l'an"),
    PAQUES("Le lundi de Pâques", "Fête de Pâques"),
    FETE_DU_TRAVAIL("La fête du Travail", "Le 1er Mai"),
    VICTOIRE_1945("La fête de la victoire", "La Victoire"),
    JEUDI_ASCENSION("Le jeudi de l'ascension", "L'ascension"),
    LUNDI_PENTECOTE("Le Lundi de la Pentecôte", "Pentecôte"),
    FETE_NATIONALE("La fête Nationale", "Le 14 Juillet"),
    ASSOMPTION("La fête de L'Assomption", "L'Assomption"),
    TOUSSAINT("La fête de la Toussaint", "Toussaint"),
    ARMISTICE("Jour de l'Armistice", "L'Armistice"),
    NOEL("Fête de Noel", "Noel"),
    VENDREDI_SAINT("Le vendredi Saint", "Le Saint Vendredi"),
    IEM2_JOUR_NOEL("Le deuxième jour de Noel", "Deuxième jour Noel");

    private String enumFerie1;
    private String enumFerie2;

    EnumFeries(String enumFerie1, String enumFerie2) {
        this.enumFerie1 = enumFerie1;
        this.enumFerie2 = enumFerie2;
    }

    public String getEnumFerie1() {
        return enumFerie1;
    }

    public String getEnumFerie2() {
        return enumFerie2;
    }


}
