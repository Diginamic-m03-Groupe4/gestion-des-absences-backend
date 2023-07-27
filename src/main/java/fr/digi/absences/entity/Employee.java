package fr.digi.absences.entity;

import fr.digi.absences.consts.Days;
import fr.digi.absences.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Roles> roles = new ArrayList<>();

    @OneToMany
    private List<Absence> absences = new ArrayList<>();

    public int getNombresJoursRestantsCP(){
        return DateUtils.getNbJoursRestants(absences, Days.NB_JOURS_CONGES_PAYES_MAX, TypeConge.PAYE);
    }

    public int getNombresJoursRestantsRTT(){
        return DateUtils.getNbJoursRestants(absences, Days.NB_RTT_EMPLOYEE, TypeConge.RTT_EMPLOYE);
    }
}
