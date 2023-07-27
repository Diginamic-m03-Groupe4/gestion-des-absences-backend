package fr.digi.absences.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AuthResponse {
    private EmployeeDto employeeDto;
    private String cookie;
}
