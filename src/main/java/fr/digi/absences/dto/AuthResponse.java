package fr.digi.absences.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class AuthResponse {
    private EmployeeDto employeeDto;
    private String cookie;
}
