package fr.digi.absences.dto;

import fr.digi.absences.consts.StatutAbsenceEmployeur;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.entity.Employee;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RTTEmployeurDTO {

    private Long id;
    @NotNull
    private LocalDate date;

    private String libelle;

    private StatutAbsenceEmployeur statutAbsenceEmployeur;

}
