package fr.digi.absences.service;

import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.repository.AbsenceRepo;
import fr.digi.absences.repository.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AbsenceSrvc {

    private AbsenceRepo absenceRepo;
    private EmployeeRepo employeeRepo;

    public void createAbsence(AbsenceDto absenceDto){
    }

}
