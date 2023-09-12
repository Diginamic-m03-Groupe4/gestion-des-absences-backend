package fr.digi.absences.mapper;

import fr.digi.absences.consts.StatutAbsence;
import fr.digi.absences.dto.EmployeCreationDto;
import fr.digi.absences.dto.EmployeeDto;
import fr.digi.absences.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class EmployeeMap {

    private final PasswordEncoder passwordEncoder;
    private final AbsenceMap absenceMap;

    public EmployeeDto toEmployeeDto(Employee employee){
        return EmployeeDto.builder()
                .id(employee.getId())
                .nom(employee.getNom())
                .prenom(employee.getPrenom())
                .email(employee.getEmail())
                .role(List.of(employee.getRole()))
                .build();
    }

    public EmployeeDto toEmployeeDtoWithAbsences(Employee employee){
        EmployeeDto dto = toEmployeeDto(employee);
        dto.setAbsences((employee.getAbsences().stream()
                .filter(absence -> absence.getStatus() == StatutAbsence.ATTENTE_VALIDATION
                        ||  absence.getStatus() == StatutAbsence.VALIDEE)
                .map(absenceMap::toAbsenceDto).toList()));
        dto.setDepartement(employee.getDepartement().getLibelle());
        return dto;
    }

    public Employee toEmployee(EmployeCreationDto employeCreationDto) {
        return Employee.builder()
                .email(employeCreationDto.getEmail())
                .prenom(employeCreationDto.getPrenom())
                .role(employeCreationDto.getRole())
                .nom(employeCreationDto.getNom())
                .dateEmbauche(LocalDate.now())
                .password(passwordEncoder.encode(employeCreationDto.getPassword()))
                .build();
    }

}
