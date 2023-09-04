package fr.digi.absences.consts;

import com.fasterxml.jackson.annotation.JsonValue;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.service.JourFeriesService;

import java.util.List;

public enum EnumFeries {

    JOUR_DE_L_AN("Le jour de l'an"),
    PAQUES("Le lundi de Pâques"),
    FETE_DU_TRAVAIL("La fête du Travail"),
    VICTOIRE_1945("La fête de la victoire"),
    JEUDI_ASCENSION("Le jeudi de l'ascension"),
    LUNDI_PENTECOTE("Le Lundi de la Pentecôte"),
    FETE_NATIONALE("La fête Nationale"),
    ASSOMPTION("La fête de L'Assomption"),
    TOUSSAINT("La fête de la Toussaint"),
    ARMISTICE("Jour de l'Armistice"),
    NOEL("Fête de Noel"),
    VENDREDI_SAINT("Le vendredi Saint"),
    IEM2_JOUR_NOEL("Le deuxième jour de Noel");

    private String enumFerie1;

    EnumFeries(String enumFerie1) {
        this.enumFerie1 = enumFerie1;
    }

    @JsonValue
    public String getEnumFerie1() {
        return enumFerie1;
    }

}
