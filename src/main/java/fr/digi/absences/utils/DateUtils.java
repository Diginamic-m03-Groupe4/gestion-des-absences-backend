package fr.digi.absences.utils;

import fr.digi.absences.consts.DaysByName;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.entity.JourFerie;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.exception.DuplicateIdentifierException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Component
public class DateUtils {

    /**
     * Algorithme permettant de récupérer le nombre de jour entre une date de début et une date de fin
     * Pour chaque jour entre les deux, si la date ne correspond pas à weekend, on incrémente de 1
     * @param dateDebut date de debut de l'absence
     * @param dateFin date de fin de l'absence
     * @return le nombre de jour travaillé entre le début et la fin de l'absence
     * */
    private static int getNbJoursEntreDeuxJours(LocalDate dateDebut, LocalDate dateFin) {
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

    /**
     * Permet de récupérer le nombre de jours travaillés dans une liste d'absence
     * @param absences les absences d'un employé
     * @return le nombre de jours travaillé dans la liste d'absence
     * */
    public static int getNbJoursRestants(List<Absence> absences) {
        int nbConges = 0;
        for (Absence absence : absences) {
            nbConges += DateUtils.getNbJoursEntreDeuxJours(absence.getDateDebut(), absence.getDateFin());
        }
        return nbConges;
    }

    /**
     * La méthode permet de vérifier si la période d'absence
     * choisie n'inclut pas au moins un jour ferié.
     *
     * @param dateDebut
     * @param dateFin
     * @param joursFeries
     * @return
     */
    public static boolean isOnJourFerie(LocalDate dateDebut, LocalDate dateFin, List<JourFerie> joursFeries) {
        if(dateDebut.getYear() != dateFin.getYear()){
            return true;
        }
        Stream<LocalDate> localDateStream = dateDebut.datesUntil(LocalDate.ofEpochDay(dateFin.plusDays(1).toEpochDay()));
        Collection<LocalDate> dateList = localDateStream.filter(DateUtils::isBusinessDay).toList();

        log.info(dateList.toString());

        if (dateList.isEmpty()) {
            return true;
        }

        for (JourFerie joursFerie : joursFeries) {
            if (dateList.stream().anyMatch(date -> date.isEqual(joursFerie.getDate()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode permettant de vérifier si le jour RTT posé n'est pas un jour ferié
     * @param date
     * @param joursFeries
     * @return
     */
    public static boolean isRTTOnJourFerie(LocalDate date, List<JourFerie> joursFeries) {
        for (JourFerie joursFerie : joursFeries) {
            if (date.isEqual(joursFerie.getDate())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode permettant de vérifier si l'absence existe déjà dans la liste.
     * La localDateStream est notre plage de dates qui va nous permettre
     * à chaque itération de la liste des absences de vérifier si chaque date de
     * début de congés n'est pas avant ou égale au moins à une date de la période
     * de congé choisie
     * et si chaque date de fin n'est pas après ou égale au moins à une date de la
     * période de congé choisie
     * Dans le cas contraire nous retournerons une BrokenRuleException
     *
     * @param absences
     * @param dateDebut
     * @param dateFin
     * @return
     */
    public static boolean isAbsenceExist(List<Absence> absences, LocalDate dateDebut, LocalDate dateFin) {

        Stream<LocalDate> localDateStream = dateDebut.datesUntil(LocalDate.ofEpochDay(dateFin.toEpochDay()));
        List<LocalDate> dateList = localDateStream.filter(DateUtils::isBusinessDay).toList();

        if (dateList.isEmpty()) {
            throw new BrokenRuleException("La période de congés choisie doit avoir des jours ouvrés.");
        }

        for (Absence absence : absences) {
            if ((absence.getDateDebut().isBefore(dateList.get(0))
                    || absence.getDateDebut().isEqual(dateList.get(0))
                    || absence.getDateFin().isEqual(dateList.get(0)))
                    && (absence.getDateFin().isAfter(dateList.get(dateList.size() - 1))
                    || absence.getDateFin().isEqual(dateList.get(dateList.size() - 1))
                    || absence.getDateDebut().isEqual(dateList.get(dateList.size() - 1))
            )) {
                throw new DuplicateIdentifierException("Vous ne pouvez pas poser ces jours de congés");
            }
        }
        return true;
    }

    /**
     * Methode permettant de verifier si la date locale en paramètre est bien un jour ouvré (Business Day)
     * On vérifie le jour ouvré en utilisant la classe SimpleDateFormat pour le formatage du nom
     * et on compare ce dernier avec l'énumération Days correspondant à Samedi ou Dimanche.
     *
     * @param localDate
     * @return boolean
     * <p>
     * on retourne un boolean qui est égale à false si la date est un Samedi ou un Dimanche
     */
    public static boolean isBusinessDay(LocalDate localDate) {

        log.info(localDate.toString());
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.FRANCE);

        return !(dateFormat.format(date).equalsIgnoreCase(DaysByName.SAMEDI.getDayByNameMin())
                || dateFormat.format(date).equalsIgnoreCase(DaysByName.DIMANCHE.getDayByNameMin()));
    }

    public static LocalDate findDateDebutAnneeAbsence(Employee employee) {
        LocalDate returnDate = LocalDate.of(
                LocalDate.now().getYear(),
                employee.getDateEmbauche().getMonth(),
                employee.getDateEmbauche().getDayOfMonth());
        return (returnDate.isBefore(LocalDate.now())) ? returnDate : returnDate.plusYears(-1);
    }
}
