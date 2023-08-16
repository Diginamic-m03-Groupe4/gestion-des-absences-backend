package fr.digi.absences.entity;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String motif;
    private StatutAbsence status;
    private TypeConge typeConge;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


}