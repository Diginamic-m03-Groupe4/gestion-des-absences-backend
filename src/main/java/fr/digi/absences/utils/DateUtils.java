package fr.digi.absences.utils;

import fr.digi.absences.entity.Absence;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
public class DateUtils {

    public static int getNbJoursEntreDeuxJours(LocalDate dateDebut, LocalDate dateFin){
        int nbJours = 1;
        while(dateDebut.getDayOfYear() != dateFin.getDayOfYear()){
            dateDebut = dateDebut.plusDays(1);
            if(!dateDebut.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && !dateDebut.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                nbJours++;
            }
        }
        return nbJours;
    }

    public static int getNbJoursRestants(List<Absence> absences, int nbCongesRestants, TypeConge typeConge){
        for (Absence absence : absences) {
            if(absence.getTypeConge().equals(typeConge)){
                nbCongesRestants -= DateUtils.getNbJoursEntreDeuxJours(absence.getDateDebut(), absence.getDateFin());
            }
        }
        return nbCongesRestants;
    }

    public static int getNbJoursRestants(List<Absence> absences, int nbCongesRestants, TypeConge typeConge, StatutAbsence statutAbsence){
        for (Absence absence : absences) {
            if(absence.getTypeConge().equals(typeConge) && absence.getStatus() == statutAbsence){
                nbCongesRestants -= DateUtils.getNbJoursEntreDeuxJours(absence.getDateDebut(), absence.getDateFin());
            }
        }
        return nbCongesRestants;
    }

    public static boolean isValidAbsence(LocalDate dateDebut, LocalDate dateFin){
        return false;
    }
}
