package fr.digi.absences.service;

import fr.digi.absences.consts.Roles;
import fr.digi.absences.dto.AuthResponse;
import fr.digi.absences.dto.EmployeCreationDto;
import fr.digi.absences.dto.EmployeeDto;
import fr.digi.absences.dto.LoginDto;
import fr.digi.absences.entity.Employee;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.exception.DuplicateIdentifierException;
import fr.digi.absences.mapper.EmployeeMap;
import fr.digi.absences.repository.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeSrvc {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMap employeeMap;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * @param dto
     * @return
     */
    @Transactional
    public AuthResponse getAuthResponse(LoginDto dto) {
        log.info(dto.getEmail());
        log.info(dto.getPassword());
        EmployeeDto user = employeeRepo.findByEmail(dto.getEmail())
                .filter(employee ->  dto.getPassword().equals(employee.getPassword()))
                .map(employeeMap::toEmployeeDto)
                .orElseThrow(() -> new EntityNotFoundException("L’adresse e-mail ou le mot de passe que vous avez entré n’est pas valide. Veuillez réessayer."));
        return AuthResponse.builder()
                .employeeDto(user)
                .cookie(jwtService.buildJWTCookie(user))
                .build();
    }

    /**
     * @param utilisateur
     * @return
     */
    public AuthResponse saveUtilisateur(EmployeCreationDto utilisateur) {
        validateUtilisateur(utilisateur);
        utilisateur.setRole(Roles.EMPLOYEE);
        EmployeeDto dto = employeeMap.toEmployeeDto(employeeRepo.save(employeeMap.toEmployee(utilisateur)));
        return AuthResponse.builder()
               .employeeDto(dto)
               .cookie(jwtService.buildJWTCookie(dto))
               .build();
    }

    /**
     * @param utilisateur
     */
    private void validateUtilisateur(EmployeCreationDto utilisateur) {
        if (employeeRepo.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new DuplicateIdentifierException("Le mail de l'utilisateur souhaitant créer un compte existe déjà en base");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() > 8;
    }

    public List<EmployeeDto> getByDepartement(String email) {
        Employee employee = employeeRepo.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return employeeRepo.findByDepartement(employee.getDepartement())
                .stream()
                .map(employeeMap::toEmployeeDtoWithAbsences)
                .toList();
    }
}
