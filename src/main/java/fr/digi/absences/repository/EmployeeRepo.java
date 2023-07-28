package fr.digi.absences.repository;

import fr.digi.absences.entity.Absence;
import fr.digi.absences.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee,Long>{
    List<Employee> findByAbsencesOrderByAbsences_DateDebutAsc(Absence absences);
    List<Employee> findByAbsences(Absence absences);
    Optional<Employee> findByEmail(String email);
}
