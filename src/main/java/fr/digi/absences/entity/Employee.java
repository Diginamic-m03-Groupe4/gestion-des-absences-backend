package fr.digi.absences.entity;

import fr.digi.absences.consts.Days;
import fr.digi.absences.consts.Roles;
import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.consts.TypeConge;
import fr.digi.absences.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String password;
    private LocalDate dateEmbauche;
    @Enumerated
    private Roles role;
    @OneToMany(mappedBy = "employee")
    private List<Absence> absences = new ArrayList<>();
    @OneToMany(mappedBy = "employee")
    private List<AbsenceRejetee> absenceRejetees = new ArrayList<>();
//    @ManyToOne
//    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    public int getNombresJoursRestantsCPAvecNonValides(){
        return DateUtils.getNbJoursRestants(absences, Days.NB_JOURS_CONGES_PAYES_MAX, TypeConge.PAYE);
    }

    public int getNombresJoursRestantsCPSansNonValides(){
        return DateUtils.getNbJoursRestants(absences, Days.NB_JOURS_CONGES_PAYES_MAX, TypeConge.PAYE, StatutAbsence.REJETEE);
    }

    public int getNombresJoursRestantsRTT(){
        return DateUtils.getNbJoursRestants(absences, Days.NB_RTT_EMPLOYEE, TypeConge.RTT_EMPLOYE);
    }

    public String getFullName(){
        return nom.toUpperCase() + " " + prenom;
    }
}
