package fr.digi.absences.mapper;

import fr.digi.absences.dto.EmployeeDto;
import fr.digi.absences.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmployeeMap {

    private final PasswordEncoder passwordEncoder;

    public EmployeeDto toEmployeeDto(Employee employee){
        return EmployeeDto.builder()
                .id(employee.getId())
                .nom(employee.getNom())
                .prenom(employee.getPrenom())
                .email(employee.getEmail())
                .build();
    }

    public Employee toEmployee(EmployeeDto employeeDto){
        return Employee.builder()
                .email(employeeDto.getEmail())
                .password(employeeDto.getPassword())
                .roles(employeeDto.getRoles())
                .prenom(employeeDto.getPrenom())
                .nom(employeeDto.getNom())
                .build();
    }
}
