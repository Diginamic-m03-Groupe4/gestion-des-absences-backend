package fr.digi.absences.service;

import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AbsenceSrvc {

    private AbsenceRepo absenceRepo;
    private EmployeeRepo employeeRepo;

    public void createAbsence(AbsenceDto absenceDto){
        if(!LocalDate.now().isBefore(absenceDto.getDateDebut())){
            throw new BrokenRuleException("La Date du début de l'absence n'est pas supérieure à la date du jour");
        }

        if(absenceDto.getDateFin().isBefore(absenceDto.getDateDebut())){
            throw new BrokenRuleException("La date de fin doit être superieure ou égale à la date de début");
        }

        if(absenceDto.getTypeConge().equals(TypeConge.SANS_SOLDE) && absenceDto.getMotif().isBlank()){
            throw new BrokenRuleException("Il faut renseigner le motif du congé");
        }

        // TODO LOGIC METIER date de début doit etre un jour ouvré et ne doit pas etre un jour ferié
        // TODO LOGIC METIER date de fin doit etre un jour ouvré et ne doit pas etre un jour ferié

    }

}
