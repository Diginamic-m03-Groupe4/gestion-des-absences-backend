package fr.digi.absences.utils;

import fr.digi.absences.consts.DaysByName;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.exception.BrokenRuleException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@Component
public class DateUtils {

    public static int getNbJoursEntreDeuxJours(LocalDate dateDebut, LocalDate dateFin) {
        int nbJours = 1;
        while (dateDebut.getDayOfYear() != dateFin.getDayOfYear()) {
            dateDebut = dateDebut.plusDays(1);
            if (!dateDebut.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !dateDebut.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                nbJours++;
            }
        }
        return nbJours;
    }

    public static int getNbJoursRestants(List<Absence> absences, int nbCongesRestants, TypeConge typeConge) {
        for (Absence absence : absences) {
            if (absence.getTypeConge().equals(typeConge)) {
                nbCongesRestants -= DateUtils.getNbJoursEntreDeuxJours(absence.getDateDebut(), absence.getDateFin());
            }
        }
        return nbCongesRestants;
    }

    public static int getNbJoursRestants(List<Absence> absences, int nbCongesRestants, TypeConge typeConge, StatutAbsence statutAbsence) {
        for (Absence absence : absences) {
            if (absence.getTypeConge().equals(typeConge) && absence.getStatus() == statutAbsence) {
                nbCongesRestants -= DateUtils.getNbJoursEntreDeuxJours(absence.getDateDebut(), absence.getDateFin());
            }
        }
        return nbCongesRestants;
    }

    /**
     * La méthode permet de vérifier si la période d'absence
     * choisie n'inclut pas un ou des jours feriés.
     * @param dateDebut
     * @param dateFin
     * @param jourFerieHashMap
     * @param statutAbsence
     * @return
     */
    public static boolean isValidAbsence(LocalDate dateDebut, LocalDate dateFin, Map<Integer, JourFerie> jourFerieHashMap, StatutAbsence statutAbsence) {

        // A VOIR SI ON GARDE ICI LA VERIFICATION DE LA METHODE OU SI ON LA SWITCH AILLEURS
        if (!statutAbsence.equals(StatutAbsence.ATTENTE_VALIDATION)) {
            return false;
        }

        Stream<LocalDate> localDateStream = dateDebut.datesUntil(LocalDate.ofEpochDay(dateFin.toEpochDay()));
        Stream<LocalDate> localDateStreamBusinessDay = localDateStream.filter(DateUtils::isNotBusinessDay);

        for (Map.Entry<Integer, JourFerie> entry : jourFerieHashMap.entrySet()) {
            JourFerie jourF = entry.getValue();
            if (localDateStreamBusinessDay.anyMatch(date -> date.equals(jourF.getDate()))) {
                throw new BrokenRuleException("Il y'a un jour ferié sur votre période de congé");
            }
        }
        return true;
    }

    /**
     * Méthode permettant de vérifier si l'absence existe déjà dans la liste.
     * La localDateStream est notre période choisie et elle va nous permettre
     * à chaque itération de la liste des absences de vérifier si la date de
     * début de congés est avant ou égale à la période de congés
     * et si la periode de congés n'est pas avant ou égale à la date de fin
     * Si c'est le cas alors on lance une BrokenRuleException
     * @param absences
     * @param dateDebut
     * @param dateFin
     * @param statutAbsence
     * @return
     */
    public static boolean isAbsenceExist(List<Absence> absences, LocalDate dateDebut, LocalDate dateFin, StatutAbsence statutAbsence) {

        if (!statutAbsence.equals(StatutAbsence.ATTENTE_VALIDATION)) {
            return false;
        }

        Stream<LocalDate> localDateStream = dateDebut.datesUntil(LocalDate.ofEpochDay(dateFin.toEpochDay()));
        Stream<LocalDate> localDateStreamBusinessDay = localDateStream.filter(DateUtils::isNotBusinessDay);

        for (Absence absence : absences) {
            if (localDateStreamBusinessDay.noneMatch(date -> absence.getDateDebut().isBefore(date)
                    || date.isBefore(absence.getDateFin()))) {
                throw new BrokenRuleException("Vous ne pouvez pas poser un congé sur une période de congé déjà existante");
            }
        }
        return true;
    }

    /**
     * Methode permettant de verifier si la date locale en paramètre est bien un jour ouvré (Business Day)
     * On verifie le nom du jour en utilisant la classe SimpleDateFormat pour le formatage en nom
     * et on vérifie que le format rendu est égale à une enumération DaysByName correspondant à
     * Samedi ou Dimanche
     * @param localDate
     * @return on retourne un boolean égale à false si la date en paramètre est Samedi ou Dimanche
     */
    public static boolean isNotBusinessDay(LocalDate localDate){

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.FRANCE);

        return !(dateFormat.format(date).equalsIgnoreCase(DaysByName.SAMEDI.getDayByNameMin())
                || dateFormat.format(date).equalsIgnoreCase(DaysByName.DIMANCHE.getDayByNameMin()));
    }

}
