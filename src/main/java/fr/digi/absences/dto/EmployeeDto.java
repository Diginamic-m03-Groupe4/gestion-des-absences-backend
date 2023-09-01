package fr.digi.absences.dto;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.entity.Absence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeDto {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private Roles role;
    private String Departement;
    private List<Absence> absences = new ArrayList<>();
    private int nombresJoursRestantsCP;
    private int nombresJoursRestantsRTT;
}