package fr.digi.absences.controller;

import fr.digi.absences.dto.AuthResponse;
import fr.digi.absences.dto.EmployeCreationDto;
import fr.digi.absences.dto.EmployeeDto;
import fr.digi.absences.dto.LoginDto;
import fr.digi.absences.repository.EmployeeRepo;
import fr.digi.absences.service.EmployeeSrvc;
import fr.digi.absences.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/api/v1/employee")
public class EmployeeCtrl {

    private final EmployeeSrvc employeeSrvc;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        AuthResponse response =  employeeSrvc.getAuthResponse(loginDto);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, response.getCookie()).body(response.getEmployeeDto());
    }
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody EmployeCreationDto utilisateur) {
        AuthResponse authResponse = employeeSrvc.saveUtilisateur(utilisateur);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, authResponse.getCookie()).body(authResponse.getEmployeeDto());
    }
}
