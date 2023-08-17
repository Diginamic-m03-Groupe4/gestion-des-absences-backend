package fr.digi.absences.mapper;

import ch.qos.logback.core.net.server.Client;
import fr.digi.absences.dto.AbsenceDto;
import fr.digi.absences.entity.Absence;
import fr.digi.absences.repository.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AbsenceMap {

    private Absence absence;
    private EmployeeRepo employeeRepo;

    public Absence toAbsence(AbsenceDto absenceDto){
        return Absence.builder()
                .motif(absenceDto.getMotif())
                .dateDebut(absenceDto.getDateDebut())
                .dateFin(absenceDto.getDateFin())
                .status(absenceDto.getStatus())
                .typeConge(absenceDto.getTypeConge())
                .employee(this.employeeRepo.findByEmail(absenceDto.getEmail()).orElseThrow(EntityNotFoundException::new))
                .build();
    }

}