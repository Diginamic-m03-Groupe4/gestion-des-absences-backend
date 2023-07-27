package fr.digi.absences.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Roles> roles = new ArrayList<>();



}
