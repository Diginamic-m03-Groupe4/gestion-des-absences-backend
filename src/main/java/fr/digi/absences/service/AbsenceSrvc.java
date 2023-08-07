package fr.digi.absences.service;

import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.mapper.AbsenceMap;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.utils.DateUtils;
import fr.digi.absences.utils.JoursOuvresFrance;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbsenceSrvc {

    private EmployeeRepo employeeRepo;
    private AbsenceRepo absenceRepo;

    public Absence getAbsences(AbsenceDto absenceDto){
        return this.absenceRepo.findByMotif(absenceDto.getMotif());
    }

    public Absence createAbsence(AbsenceDto absenceDto){
        // TODO LOGIC METIER
        if(!LocalDate.now().isBefore(absenceDto.getDateDebut())){
            throw new BrokenRuleException("La Date du début de l'absence n'est pas supérieure à la date du jour");
        }

        // TODO LOGIC METIER
        if(absenceDto.getDateFin().isBefore(absenceDto.getDateDebut())){
            throw new BrokenRuleException("La date de fin doit être superieure ou égale à la date de début");
        }

        // TODO LOGIC METIER
        if(absenceDto.getTypeConge().equals(TypeConge.SANS_SOLDE) && absenceDto.getMotif().isBlank()){
            throw new BrokenRuleException("Il faut renseigner le motif du congé");
        }

        // TODO LOGIC METIER date de début doit etre un jour ouvré et ne doit pas etre un jour ferié
        // TODO LOGIC METIER date de fin doit etre un jour ouvré et ne doit pas etre un jour ferié
        DateUtils.isValidAbsence(absenceDto.getDateDebut(), absenceDto.getDateFin(), JoursOuvresFrance.joursFeries(2023));

        // TODO LOGIC METIER la période d'absence choisie existe déjà dans la liste des absences de l'employé
        Optional<Employee> employeeOptional = this.employeeRepo.findByEmail(absenceDto.getEmail());
        Employee employee = employeeOptional.orElseThrow(EntityNotFoundException::new);
        DateUtils.isAbsenceExist(employee.getAbsences(), absenceDto.getDateDebut(), absenceDto.getDateFin());

        // CREATION DE l'ABSENCE AVEC LE MAPPER ABSENCE
        AbsenceMap absenceMap = new AbsenceMap();
        Absence absence = absenceMap.toAbsence(absenceDto);

        return this.absenceRepo.save(absence);

    }

}
