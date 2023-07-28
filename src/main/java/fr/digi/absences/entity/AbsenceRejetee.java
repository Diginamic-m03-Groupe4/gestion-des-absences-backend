package fr.digi.absences.entity;

import fr.digi.absences.consts.TypeConge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbsenceRejetee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private LocalDate dateRejet;
    private TypeConge typeConge;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
