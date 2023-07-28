package fr.digi.absences.dto;

import fr.digi.absences.consts.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeCreationDto {
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private Roles role;
    private String password;
}
