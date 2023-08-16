package fr.digi.absences.entity;

import fr.digi.absences.consts.EnumFeries;
import fr.digi.absences.consts.LibelleJourFerie;
import fr.digi.absences.consts.StatutAbsenceEmployeur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class JourFerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private EnumFeries libelle;
    private LocalDate date;
    private StatutAbsenceEmployeur statutAbsenceEmployeur;
    private boolean isWorked = false;
}