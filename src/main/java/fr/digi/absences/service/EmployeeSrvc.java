package fr.digi.absences.service;

import fr.digi.absences.dto.AuthResponse;
import fr.digi.absences.dto.EmployeeDto;
import fr.digi.absences.dto.LoginDto;
import fr.digi.absences.exception.BrokenRuleException;
import fr.digi.absences.exception.DuplicateIdentifierException;
import fr.digi.absences.mapper.EmployeeMap;
import fr.digi.absences.repository.EmployeeRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeSrvc {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMap employeeMap;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse getAuthResponse(LoginDto dto){
        EmployeeDto user = employeeRepo.findByEmail(dto.getEmail())
                .filter(employee -> passwordEncoder.matches(dto.getPassword(), employee.getPassword()))
                .map(employeeMap::toEmployeeDto)
                .orElseThrow(EntityNotFoundException::new);
        return AuthResponse.builder()
                .employeeDto(user)
                .cookie(jwtService.buildJWTCookie(user))
                .build();
    }

    public AuthResponse saveUtilisateur(EmployeeDto utilisateur) {
        validateUtilisateur(utilisateur);
        EmployeeDto dto = employeeMap.toEmployeeDto(employeeRepo.save(employeeMap.toEmployee(utilisateur)));
        return AuthResponse.builder()
                .employeeDto(dto)
                .cookie(jwtService.buildJWTCookie(dto))
                .build();
    }

    private void validateUtilisateur(EmployeeDto utilisateur) {

        if(employeeRepo.findByEmail(utilisateur.getEmail()).isPresent()){
            throw new DuplicateIdentifierException("Le mail de l'utilisateur souhaitant créer un compte existe déjà en base");
        }

        if(isValidPassword(utilisateur.getPassword())){
            throw new BrokenRuleException("Le mot de passe n'est pas assez puissant pour être accepté comme nouveau mot de passe");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() > 8;
    }
}
