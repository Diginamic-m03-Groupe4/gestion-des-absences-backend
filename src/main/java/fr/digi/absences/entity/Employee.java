package fr.digi.absences.entity;

import fr.digi.absences.consts.Roles;
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
    @ManyToOne
    private Departement departement;

    public String getFullName(){
        return nom.toUpperCase() + " " + prenom;
    }
}
