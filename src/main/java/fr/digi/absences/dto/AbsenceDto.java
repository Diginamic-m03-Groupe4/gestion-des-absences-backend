package fr.digi.absences.dto;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@Data
public class AbsenceDto {
    @NotNull
    private LocalDate dateDebut; // Instant librairie JS => moment.js

    @NotNull
    private LocalDate dateFin;

    private String motif;

    private StatutAbsence status;

    @NotNull
    private TypeConge typeConge;
    // TODO passer par JWT + tard...

    @NotNull
    private String email;
}
