package fr.digi.absences.dto;

import fr.digi.absences.entity.Roles;
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
    private String password;
    private List<Roles> roles =new ArrayList<>();

}

