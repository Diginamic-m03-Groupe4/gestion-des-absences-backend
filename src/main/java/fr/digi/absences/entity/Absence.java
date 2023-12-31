package fr.digi.absences.entity;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
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
public class Absence implements hasDebutFin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate dateDemande;
    private String motif;
    @Enumerated
    private StatutAbsence status;
    @Enumerated
    private TypeConge typeConge;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}