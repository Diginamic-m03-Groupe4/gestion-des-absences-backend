package fr.digi.absences.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class LoginDto {
    private String email;
    private String password;
}
